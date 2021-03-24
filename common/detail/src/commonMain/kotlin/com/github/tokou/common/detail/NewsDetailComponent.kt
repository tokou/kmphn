package com.github.tokou.common.detail

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ComponentContext
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

    private val stateToModel: (NewsDetailStore.State) -> NewsDetail.Model = { when (it) {
        is NewsDetailStore.State.Content -> NewsDetail.Model(it.news)
        else -> NewsDetail.Model()
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
        .map { it.copy(maxitem = itemId) }

    override fun onBack() {
        onOutput(NewsDetail.Output.Back)
    }
}
