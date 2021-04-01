package com.github.tokou.common.main

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.main.NewsMain.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.asValue
import com.github.tokou.common.utils.format
import com.github.tokou.common.utils.getStore
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class NewsMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    api: NewsApi,
    database: NewsDatabase,
    private val onOutput: (Output) -> Unit
): NewsMain, ComponentContext by componentContext {

    private fun NewsMainStore.News.asModel(): Item = Item(
        id = id,
        title = title.orEmpty(),
        link = link,
        user = user.orEmpty(),
        time = time.format(),
        comments = descendants.toString(),
        points = points.toString()
    )

    private val stateToModel: (NewsMainStore.State) -> Model = { when (it) {
        is NewsMainStore.State.Content -> Model.Content(
            items = it.news.map { n -> n.asModel() },
            isLoadingMore = it.isLoadingMore,
            isRefreshing = it.isRefreshing,
            canLoadMore = it.canLoadMore,
        )
        NewsMainStore.State.Loading -> Model.Loading
        NewsMainStore.State.Error -> Model.Error
    } }

    private val store =
        instanceKeeper.getStore {
            NewsMainStoreProvider(
                storeFactory = storeFactory,
                repository = NewsMainRepository(database, api),
            ).provide()
        }

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onNewsSelected(id: ItemId, link: String?) {
        if (link != null) onOutput(Output.Link(link))
        else onOutput(Output.Selected(id))
    }

    override fun onNewsSecondarySelected(id: ItemId, ) {
        onOutput(Output.Selected(id))
    }

    override fun onLoadMoreSelected() {
        store.accept(NewsMainStore.Intent.LoadMore)
    }

    override fun onRefresh(fromPull: Boolean) {
        store.accept(NewsMainStore.Intent.Refresh(keepContent = fromPull))
    }
}
