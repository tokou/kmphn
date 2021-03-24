package com.github.tokou.common.detail

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ComponentContext
import com.github.tokou.common.utils.UserId
import com.github.tokou.common.utils.asValue
import com.github.tokou.common.utils.getStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus


class NewsDetailComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: NewsDatabase,
    itemId: Long,
    private val onOutput: (NewsDetail.Output) -> Unit
): NewsDetail, ComponentContext by componentContext {

    private fun NewsDetailStore.News.asHeader() = NewsDetail.Header(
        id = id,
        title = title.orEmpty(),
        link = link,
        user = user.orEmpty(),
        time = time.toString(),
        commentsCount = descendants.toString(),
        points = points.toString()
    )

    private fun NewsDetailStore.Comment.asModel(op: UserId?): NewsDetail.Comment = when (this) {
        is NewsDetailStore.Comment.Loading -> NewsDetail.Comment.Loading(id)
        is NewsDetailStore.Comment.Content -> NewsDetail.Comment.Content.Expanded(
            id = id,
            user = user,
            time = time.toString(),
            isOp = user == op,
            children = comments.map { c -> c.asModel(op) },
            text = text,
        )
    }

    private val stateToModel: (NewsDetailStore.State) -> NewsDetail.Model = { when (it) {
        is NewsDetailStore.State.Content -> NewsDetail.Model.Content(
            header = it.news.asHeader(),
            comments = it.news.comments.map { c -> c.asModel(it.news.user) }
        )
        else -> NewsDetail.Model.Empty
    } }

    private val store =
        instanceKeeper.getStore {
            NewsDetailStoreProvider(
                storeFactory = storeFactory,
                repository = NewsDetailRepository(database, NewsApi),
                id = itemId
            ).provide()
        }

    override val models: Value<NewsDetail.Model> = store
        .asValue(this + Dispatchers.Main)
        .map(stateToModel)

    override fun onBack() {
        onOutput(NewsDetail.Output.Back)
    }
}
