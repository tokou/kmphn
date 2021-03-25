package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.arkivanov.decompose.ComponentContext
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId
import com.github.tokou.common.utils.getStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.periodUntil
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.Clock

// https://news.ycombinator.com/formatdoc
fun String.parseText(): List<NewsDetail.Text> {
    val encoding = mapOf(
        "<p>" to "\n\n",
        "</p>" to "",
        "</pre>" to "</pre>",
        "&#x27;" to "'",
        "&gt;" to ">",
        "&lt;" to "<",
        "&quot;" to "'",
        "&amp;" to "&",
        "&#x2F;" to "/",
    )
    val decoded = encoding.entries.fold(this) { t, (k, v) -> t.replace(k, v) }
    val tags = "(<pre><code>((?:.|\n)*?)</code></pre>|<i>(.*?)</i>|<a href=\"(.*?)\" rel=\"nofollow\">(.*?)</a>)".toRegex()
    val matches = tags.findAll(decoded)
    val formatted = matches.map {
        val values = it.groupValues.drop(2)
        it.range to when {
            values[1].isNotBlank() -> NewsDetail.Text.Emphasis(values[1])
            values[0].isNotBlank() -> NewsDetail.Text.Code(values[0])
            else -> NewsDetail.Text.Link(values[3], values[2])
        }
    }
    var current = 0
    val all = formatted.flatMap { (range, element) ->
        val p = NewsDetail.Text.Plain(decoded.subSequence(current, range.first).toString())
        val prev = if (current < range.first) listOf(p) else emptyList()
        current = range.last + 1
        val isLast = formatted.last().second == element
        val n = NewsDetail.Text.Plain(decoded.subSequence(current, decoded.length).toString())
        val next = if (isLast && current < decoded.length) listOf(n) else emptyList()
        prev + listOf(element) + next
    }
    return all.toList().ifEmpty { listOf(NewsDetail.Text.Plain(decoded)) }
}

fun Instant.format(): String {
    val period = periodUntil(Clock.System.now(), TimeZone.currentSystemDefault())
    fun plural(n: Int) = if (n == 1) "" else "s"
    return when {
        period.years > 0 -> period.years.let { "$it year${plural(it)}" }
        period.months > 0 -> period.months.let { "$it month${plural(it)}" }
        period.days > 0 -> period.days.let { "$it day${plural(it)}" }
        period.hours > 0 -> period.hours.let { "$it hour${plural(it)}" }
        else -> period.minutes.let { "$it minute${plural(it)}" }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
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
        text = text,
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
                repository = NewsDetailRepository(database, NewsApi),
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
