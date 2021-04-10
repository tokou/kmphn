package com.github.tokou.common.detail

import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.Comment
import com.github.tokou.common.database.Item
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.logger
import com.github.tokou.common.utils.runLogging
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


@OptIn(ExperimentalTime::class)
class NewsDetailRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) : NewsDetailStoreProvider.Repository {

    private fun Item.asNewsDetail() = NewsDetailStore.News(
        id = id,
        title = title,
        text = content,
        link = link,
        user = user.orEmpty(),
        time = created,
        comments = kids.map { NewsDetailStore.Comment.Loading(it) },
        points = score,
        descendants = descendants,
    )

    private fun Comment.asNewsComment() = NewsDetailStore.Comment.Content(
        id = id,
        user = user.orEmpty(),
        time = created,
        text = content.orEmpty(),
        comments = kids.map { NewsDetailStore.Comment.Loading(it) },
        deleted = deleted,
    )

    private val _state = MutableSharedFlow<Result<NewsDetailStore.News>>(1)
    override val updates: Flow<Result<NewsDetailStore.News>> = _state

    private fun List<NewsDetailStore.Comment>.refreshFrom(
        comments: Map<ItemId, NewsDetailStore.Comment.Content>
    ): List<NewsDetailStore.Comment> = map { old ->
        comments[old.id]?.let { it.copy(comments = it.comments.refreshFrom(comments)) } ?: old
    }

    private suspend fun loadFromDb(id: ItemId) {
        val dbItem = runLogging("loadFromDb", "Error loading item") {
            database.itemQueries.selectById(id).executeAsOneOrNull()
        }
        val item = dbItem?.asNewsDetail() ?: return
        _state.tryEmit(Result.success(item))

        val dbComments = runLogging("loadFromDb", "Error loading comments") {
            database.commentQueries.selectByItem(id).executeAsList()
        }
        val comments = dbComments?.map { it.asNewsComment() }?.associateBy { it.id } ?: emptyMap()

        val withComments = item.copy(comments = item.comments.refreshFrom(comments))
        _state.tryEmit(Result.success(withComments))
    }

    override suspend fun load(id: ItemId) = try {
        loadFromDb(id)

        val apiItem = runLogging("load", "Error while fetching item $id") {
            api.fetchItem(id) ?: throw NoSuchElementException()
        }!!

        val updatedDbItem = apiItem.asDbItem()
        runLogging("load", "Error while inserting item") {
            database.itemQueries.insert(updatedDbItem)
        }

        var item = updatedDbItem.asNewsDetail()
        _state.tryEmit(Result.success(item))

        val loadedComments = mutableMapOf<ItemId, NewsDetailStore.Comment.Content>()

        fun updateTree(comments: List<NewsDetailStore.Comment>): List<NewsDetailStore.Comment> = comments.map { c ->
            if (loadedComments.containsKey(c.id)) {
                val cc = loadedComments[c.id]!!
                cc.copy(comments = updateTree(cc.comments))
            } else c
        }

        fun update(comment: NewsDetailStore.Comment.Content) {
            loadedComments[comment.id] = comment
            item = item.copy(comments = updateTree(item.comments))
            _state.tryEmit(Result.success(item))
        }

        loadComments(item.comments, ::update, id)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        logger.e("REPO", e) { "Error while loading" }
        _state.tryEmit(Result.failure(e))
        Unit
    }

    private val commentStaleDuration = 5.toDuration(DurationUnit.MINUTES)

    private suspend fun loadComments(
        comments: List<NewsDetailStore.Comment>,
        update: (comment: NewsDetailStore.Comment.Content) -> Unit,
        itemId: ItemId
    ): Unit = coroutineScope {

        for (l in comments.filterIsInstance<NewsDetailStore.Comment.Loading>()) {
            if (isActive) launch {
                val dbComment = runLogging("loadComments", "Error while getting comment") {
                    database.commentQueries.selectById(l.id).executeAsOneOrNull()
                }
                if (dbComment != null) {
                    val comment = dbComment.asNewsComment()
                    update(comment)
                    val age = Clock.System.now() - dbComment.updated
                    if (age < commentStaleDuration) {
                        loadComments(comment.comments, update, itemId)
                        return@launch
                    }
                }

                val apiComment = runLogging("loadComments", "Error while fetching comment") {
                    api.fetchItem(l.id) as? NewsApi.Item.Comment
                } ?: return@launch

                val updatedDbComment = apiComment.asDbComment(itemId)
                runLogging("loadComments", "Error while inserting updated comment") {
                    database.commentQueries.insert(updatedDbComment)
                }

                val comment = updatedDbComment.asNewsComment()
                update(comment)
                loadComments(comment.comments, update, itemId)
            }
        }
    }

    private fun NewsApi.Item.Comment.asDbComment(itemId: ItemId): Comment = Comment(
        id = id,
        itemId = itemId,
        parentId = if (parent != itemId) parent else null,
        user = by,
        created = Instant.fromEpochSeconds(time),
        updated = Clock.System.now(),
        content = text,
        deleted = deleted,
        dead = dead,
        kids = kids
    )

    private fun NewsApi.Item.asDbItem(): Item {
        val content = when (this) {
            is NewsApi.Item.Comment -> text
            is NewsApi.Item.Job -> text
            is NewsApi.Item.Poll -> text
            is NewsApi.Item.Story -> text
            else -> null
        }
        val title = when (this) {
            is NewsApi.Item.Story -> title
            is NewsApi.Item.Job -> title
            is NewsApi.Item.Poll -> title
            else -> null
        }
        val link = when (this) {
            is NewsApi.Item.Story -> url
            is NewsApi.Item.Job -> url
            else -> null
        }
        val score = when (this) {
            is NewsApi.Item.Story -> score
            is NewsApi.Item.Poll -> score
            is NewsApi.Item.PollOption -> score
            else -> null
        }
        val descendants = when (this) {
            is NewsApi.Item.Story -> descendants
            is NewsApi.Item.Poll -> descendants
            else -> null
        }
        return Item(
            id = id,
            user = by,
            created = Instant.fromEpochSeconds(time),
            updated = Clock.System.now(),
            content = content,
            title = title,
            link = link,
            kids = kids,
            score = score,
            descendants = descendants,
            type = this::class.simpleName!!
        )
    }
}
