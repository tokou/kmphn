package com.github.tokou.common.detail

import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.Item
import com.github.tokou.common.database.NewsDatabase

class NewsDetailRepository(
    private val database: NewsDatabase,
    private val api: NewsApi
) : NewsDetailStoreProvider.Repository {

    private fun Item.asNewsDetail(): NewsDetail.News = NewsDetail.News(
        id = id,
        title = title.orEmpty(),
        link = link,
        user = user.orEmpty(),
        time = created,
        comments = "0",
        points = "0"
    )

    override suspend fun load(id: Long): NewsDetail.News? {
        val queries = database.itemQueries
        val dbItem = queries.selectById(id).executeAsOneOrNull()
        if (dbItem != null) return dbItem.asNewsDetail()
        val apiItem = api.fetchItem(id)
        val item = apiItem?.asDbItem()?.also { queries.insert(it) }
        return item?.asNewsDetail()
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
