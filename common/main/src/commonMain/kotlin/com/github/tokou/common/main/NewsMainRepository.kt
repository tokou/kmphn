package com.github.tokou.common.main

import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.Item
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ItemId
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
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
        time = Instant.fromEpochSeconds(created),
        points = score ?: 0,
        descendants = descendants ?: 0,
    )

    // TODO: find a way to express "empty"
    private val _state = MutableStateFlow<Result<List<NewsMainStore.News>>>(Result.failure(
        NewsMainStoreProvider.NoContent
    ))
    override val updates: Flow<Result<List<NewsMainStore.News>>> = _state

    override suspend fun load(loaded: Set<ItemId>, loadCount: Int) = try {

        val itemIds = api.fetchTopStoriesIds().filterNot { it in loaded }.take(loadCount)

        val loadedItems = mutableMapOf<ItemId, NewsMainStore.News>()

        coroutineScope {
            itemIds.map { id ->
                launch {
                    val dbItem = database.itemQueries.selectById(id).executeAsOneOrNull()
                    dbItem?.asNewsMain()?.let { loadedItems[id] = it }

                    val apiItem = api.fetchItem(id) ?: return@launch
                    val updatedDbItem = apiItem.asDbItem()
                    database.itemQueries.insert(updatedDbItem)

                    val item = updatedDbItem.asNewsMain()
                    loadedItems[id] = item
                }
            }.joinAll()
        }
        _state.value = Result.success(loadedItems.values.toList())

    } catch (e: Throwable) {
        println(e)
        e.printStackTrace()
        _state.value = Result.failure(e)
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
            created = time,
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
