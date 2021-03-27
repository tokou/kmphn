package com.github.tokou.common.main

import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.Item
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.logger
import com.github.tokou.common.utils.runLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


class NewsMainRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) : NewsMainStoreProvider.Repository {

    private fun Item.asNewsMain() = NewsMainStore.News(
        id = id,
        title = title,
        link = link,
        user = user,
        time = created,
        points = score ?: 0,
        descendants = descendants ?: 0,
    )

    private val _state = MutableSharedFlow<Result<List<NewsMainStore.News>>>(1)
    override val updates: Flow<Result<List<NewsMainStore.News>>> = _state

    override suspend fun load(loaded: Set<ItemId>, loadCount: Int) = try {

        val itemIds = api.fetchTopStoriesIds().filterNot { it in loaded }.take(loadCount)

        fun update(items: List<NewsMainStore.News>) {
            val result = items.sortedBy { itemIds.indexOf(it.id) }
            _state.tryEmit(Result.success(result))
        }

        suspend fun loadFromDb(ids: List<ItemId>) = runLogging("loadFromDb", "Error while selecting items") {
            val dbItems = database.itemQueries.selectByIds(ids).executeAsList()
            if (dbItems.size < ids.size) return@runLogging
            update(dbItems.map { it.asNewsMain() })
        }

        suspend fun loadFromApi(id: ItemId): NewsMainStore.News? {
            val apiItem = runLogging("loadFromApi", "Error while fetching item") {
                api.fetchItem(id)
            } ?: return null
            val updatedDbItem = apiItem.asDbItem()
            runLogging("loadFromApi", "Error while upserting item") {
                database.itemQueries.insert(updatedDbItem)
            }
            return updatedDbItem.asNewsMain()
        }

        suspend fun loadFromApi(ids: List<ItemId>) = coroutineScope {
            val items = ids
                .map { id -> async { loadFromApi(id) } }
                .awaitAll()
                .filterNotNull()
            update(items)
        }

        loadFromDb(itemIds)
        loadFromApi(itemIds)
    } catch (e: Throwable) {
        logger.e("REPO", e) { "Error while loading" }
        _state.tryEmit(Result.failure(e))
        Unit
    }

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
