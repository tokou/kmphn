package com.github.tokou.common.detail

import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.Comment
import com.github.tokou.common.database.Item
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ItemId
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class NewsDetailRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) : NewsDetailStoreProvider.Repository {

    private fun Item.asNewsDetail() = NewsDetailStore.News(
        id = id,
        title = title.orEmpty(),
        link = link,
        user = user.orEmpty(),
        time = 0,
        comments = kids.map { NewsDetailStore.Comment.Loading(it) },
        points = 0,
        descendants = 0
    )

    private fun Comment.asNewsComment() = NewsDetailStore.Comment.Content(
        id = id,
        user = user.orEmpty(),
        time = 0,
        text = content.orEmpty(),
        comments = kids.map { NewsDetailStore.Comment.Loading(it) }
    )

    private val _state = MutableStateFlow<NewsDetailStore.News?>(null)
    override val updates: Flow<NewsDetailStore.News> = _state.filterNotNull()

    override suspend fun load(id: Long) {

        val dbItem = database.itemQueries.selectById(id).executeAsOneOrNull()
        _state.value = dbItem?.asNewsDetail()

        val apiItem = api.fetchItem(id) ?: return
        val updatedDbItem = apiItem.asDbItem()
        database.itemQueries.insert(updatedDbItem)

        var item = updatedDbItem.asNewsDetail()
        _state.value = item

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
            _state.value = item
        }

        loadComments(item.comments, ::update)
    }

    private suspend fun loadComments(
        comments: List<NewsDetailStore.Comment>,
        update: (comment: NewsDetailStore.Comment.Content) -> Unit
    ): Unit = coroutineScope {

        for (l in comments.filterIsInstance<NewsDetailStore.Comment.Loading>()) {
            launch {
                val dbComment = database.commentQueries.selectById(l.id).executeAsOneOrNull()
                if (dbComment != null) {
                    val comment = dbComment.asNewsComment()
                    update(comment)
                    loadComments(comment.comments, update)
                    return@launch
                }

                val apiComment = api.fetchItem(l.id) as? NewsApi.Item.Comment ?: return@launch
                val updatedDbComment = apiComment.asDbComment()
                database.commentQueries.insert(updatedDbComment)

                val comment = updatedDbComment.asNewsComment()
                update(comment)
                loadComments(comment.comments, update)
            }
        }
    }


    private fun NewsApi.Item.Comment.asDbComment(): Comment {
        return Comment(
            id = id,
            user = by,
            created = time.toString(),
            content = text,
            parentId = 0,
            kids = kids
        )
    }

    private fun NewsApi.Item.asDbItem(): Item {
        val content = when (this) {
            is NewsApi.Item.Comment -> text
            is NewsApi.Item.Job -> text
            is NewsApi.Item.Poll -> text
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
        return Item(
            id = id,
            user = by,
            created = time.toString(),
            content = content,
            title = title,
            link = link,
            kids = kids,
            type = this::class.simpleName!!
        )
    }
}
