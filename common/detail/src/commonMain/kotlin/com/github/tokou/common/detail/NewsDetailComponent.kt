package com.github.tokou.common.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId
import com.github.tokou.common.utils.format
import com.github.tokou.common.utils.getStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    api: NewsApi,
    database: NewsDatabase,
    itemId: Long,
    private val onOutput: (NewsDetail.Output) -> Unit
): NewsDetail, ComponentContext by componentContext {

    private fun NewsDetailStore.News.asHeader() = NewsDetail.Header(
        id = id,
        title = title.orEmpty(),
        text = text?.parseText(),
        link = link,
        user = user.orEmpty(),
        time = time.format(),
        commentsCount = descendants.toString(),
        points = points.toString()
    )

    private fun NewsDetailStore.Comment.asModel(
        selectedItem: ItemId?,
        collapsedIds: Set<ItemId>,
        op: UserId?
    ): NewsDetail.Comment = when (this) {
        is NewsDetailStore.Comment.Loading -> NewsDetail.Comment.Loading
        is NewsDetailStore.Comment.Content -> {
            if (collapsedIds.contains(id)) NewsDetail.Comment.Content.Collapsed(
                id = id,
                user = user,
                isOp = user == op,
                isSelected = selectedItem == id,
                time = time.format(),
                childrenCount = if (childrenCount > 0) childrenCount.toString() else "",
            )
            else NewsDetail.Comment.Content.Expanded(
                id = id,
                user = user,
                time = time.format(),
                isOp = user == op,
                isSelected = selectedItem == id,
                children = comments
                    .withoutDeleted()
                    .map { c -> c.asModel(selectedItem, collapsedIds, op) }
                    .withSingleLoading(),
                text = text.parseText(),
            )
        }
    }

    private fun List<NewsDetail.Comment>.withSingleLoading(): List<NewsDetail.Comment> =
        if (none { it is NewsDetail.Comment.Loading }) this
        else filterIsInstance<NewsDetail.Comment.Content>() + NewsDetail.Comment.Loading

    private fun List<NewsDetailStore.Comment>.withoutDeleted(): List<NewsDetailStore.Comment> =
        filterNot { if (it is NewsDetailStore.Comment.Content) it.deleted else false }

    private val stateToModel: suspend (NewsDetailStore.State) -> NewsDetail.Model = { when (it) {
        is NewsDetailStore.State.Content -> NewsDetail.Model.Content(
            header = it.news.asHeader(),
            comments = it.news.comments
                .withoutDeleted()
                .map { c -> c.asModel(it.selectedComment, it.collapsedComments, it.news.user) }
                .withSingleLoading()
        )
        NewsDetailStore.State.Loading -> NewsDetail.Model.Loading
        NewsDetailStore.State.Error -> NewsDetail.Model.Error
    } }

    private val store =
        instanceKeeper.getStore {
            NewsDetailStoreProvider(
                storeFactory = storeFactory,
                repository = NewsDetailRepository(database, api),
                id = itemId
            ).provide()
        }

    override val models: Flow<NewsDetail.Model> = store.states.map(stateToModel)

    override fun onBack() {
        onOutput(NewsDetail.Output.Back)
    }

    override fun onRetry() {
        store.accept(NewsDetailStore.Intent.Refresh)
    }

    override fun onCommentClicked(id: ItemId) {
        store.accept(NewsDetailStore.Intent.ToggleComment(id))
    }

    override fun onUserClicked(id: UserId) {
        // TODO: implement user screen
        onOutput(NewsDetail.Output.Link("https://news.ycombinator.com/user?id=$id"))
    }

    override fun onLinkClicked(uri: String, forceExternal: Boolean) {
        val hnLink = "https?://news\\.ycombinator\\.com/item\\?id=(\\d+)".toRegex()
        val itemId = hnLink.find(uri)?.groupValues?.get(1)?.toLongOrNull()
        val output =
            if (forceExternal || itemId == null) NewsDetail.Output.Link(uri)
            else NewsDetail.Output.Item(itemId)
        onOutput(output)
    }
}
