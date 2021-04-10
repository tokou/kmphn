package com.github.tokou.common.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.detail.NewsDetail.*
import com.github.tokou.common.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
class NewsDetailComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    api: NewsApi,
    database: NewsDatabase,
    itemId: Long,
    private val onOutput: (Output) -> Unit
): NewsDetail, ComponentContext by componentContext {

    private fun NewsDetailStore.News.asHeader() = Header(
        id = id,
        title = title,
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
    ): Comment = when (this) {
        is NewsDetailStore.Comment.Loading -> Comment.Loading
        is NewsDetailStore.Comment.Content -> {
            if (collapsedIds.contains(id)) Comment.Content.Collapsed(
                id = id,
                user = user,
                isOp = user == op,
                isSelected = selectedItem == id,
                time = time.format(),
                childrenCount = if (childrenCount > 0) childrenCount.toString() else "",
            )
            else Comment.Content.Expanded(
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

    private fun List<Comment>.withSingleLoading(): List<Comment> =
        if (none { it is Comment.Loading }) this
        else filterIsInstance<Comment.Content>() + Comment.Loading

    private fun List<NewsDetailStore.Comment>.withoutDeleted(): List<NewsDetailStore.Comment> =
        filterNot { if (it is NewsDetailStore.Comment.Content) it.deleted else false }

    private val stateToModel: (NewsDetailStore.State) -> Model = { when (it) {
        is NewsDetailStore.State.Content -> Model.Content(
            header = it.news.asHeader(),
            comments = it.news.comments
                .withoutDeleted()
                .map { c -> c.asModel(it.selectedComment, it.collapsedComments, it.news.user) }
                .withSingleLoading()
        )
        NewsDetailStore.State.Loading -> Model.Loading
        NewsDetailStore.State.Error -> Model.Error
    } }

    // TODO: Going back to same detail page hangs is this is uncommented
    private val store = //instanceKeeper.getStore {
        NewsDetailStoreProvider(
            storeFactory = storeFactory,
            repository = NewsDetailRepository(database, api),
            id = itemId
        ).provide()
    //}

    override val models: Value<Model> = store.asValue().map(stateToModel)

    override fun onBack() {
        onOutput(Output.Back)
    }

    override fun onRetry() {
        store.accept(NewsDetailStore.Intent.Refresh)
    }

    override fun onCommentClicked(id: ItemId) {
        store.accept(NewsDetailStore.Intent.ToggleComment(id))
    }

    override fun onUserClicked(id: UserId) {
        // TODO: implement user screen
        onOutput(Output.Link("https://news.ycombinator.com/user?id=$id"))
    }

    override fun onLinkClicked(uri: String, forceExternal: Boolean) {
        val hnLink = "https?://news\\.ycombinator\\.com/item\\?id=(\\d+)".toRegex()
        val itemId = hnLink.find(uri)?.groupValues?.get(1)?.toLongOrNull()
        val output =
            if (forceExternal || itemId == null) Output.Link(uri)
            else Output.Item(itemId)
        onOutput(output)
    }
}
