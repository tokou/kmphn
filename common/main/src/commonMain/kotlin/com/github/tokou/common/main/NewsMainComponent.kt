package com.github.tokou.common.main

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.decompose.ComponentContext
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.format
import com.github.tokou.common.utils.getStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class NewsMainComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    api: NewsApi,
    database: NewsDatabase,
    private val onOutput: (NewsMain.Output) -> Unit
): NewsMain, ComponentContext by componentContext {

    private fun NewsMainStore.News.asModel(): NewsMain.News = NewsMain.News(
        id = id,
        title = title.orEmpty(),
        link = link,
        user = user.orEmpty(),
        time = time.format(),
        comments = descendants.toString(),
        points = points.toString()
    )

    private val stateToModel: suspend (NewsMainStore.State) -> NewsMain.Model = { when (it) {
        is NewsMainStore.State.Content -> NewsMain.Model.Content(it.news.map { n -> n.asModel() }, it.isLoadingMore)
        NewsMainStore.State.Loading -> NewsMain.Model.Loading
        NewsMainStore.State.Error -> NewsMain.Model.Error
    } }

    private val store =
        instanceKeeper.getStore {
            NewsMainStoreProvider(
                storeFactory = storeFactory,
                repository = NewsMainRepository(database, api),
            ).provide()
        }

    override val models: Flow<NewsMain.Model> = store.states.map(stateToModel)

    override fun onNewsSelected(id: ItemId, link: String?) {
        if (link != null) onOutput(NewsMain.Output.Link(link))
        else onOutput(NewsMain.Output.Selected(id))
    }

    override fun onNewsSecondarySelected(id: ItemId, ) {
        onOutput(NewsMain.Output.Selected(id))
    }

    override fun onLoadMoreSelected() {
        store.accept(NewsMainStore.Intent.LoadMore)
    }

    override fun onRefresh() {
        store.accept(NewsMainStore.Intent.Refresh)
    }
}
