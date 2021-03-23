package com.github.tokou.common.root

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetailComponent
import com.github.tokou.common.main.NewsMain
import com.github.tokou.common.main.NewsMainComponent
import com.github.tokou.common.root.NewsRoot.Child
import com.github.tokou.common.utils.ComponentContext
import com.github.tokou.common.utils.router
import kotlinx.coroutines.CoroutineScope
import com.arkivanov.decompose.ComponentContext as DecomposeComponentContext

interface NewsRoot {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: NewsMain) : Child()
        data class Detail(val component: NewsDetail) : Child()
    }
}

class NewsRootComponent(
    componentContext: DecomposeComponentContext,
    coroutineScope: CoroutineScope,
    private val newsMain: (ComponentContext, (NewsMain.Output) -> Unit) -> NewsMain,
    private val newsDetail: (ComponentContext, (NewsDetail.Output) -> Unit) -> NewsDetail,
): NewsRoot, ComponentContext, DecomposeComponentContext by componentContext, CoroutineScope by coroutineScope {

    constructor(componentContext: DecomposeComponentContext, coroutineScope: CoroutineScope) : this(
        componentContext = componentContext,
        coroutineScope = coroutineScope,
        newsMain = { context, output -> NewsMainComponent(context, output) },
        newsDetail = { context, output -> NewsDetailComponent(context, output) }
    )

    private val router = router(
        initialConfiguration = { Configuration.Main },
        handleBackButton = true,
        componentFactory = ::createChild,
        configurationClass = Configuration::class
    )

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child = when (configuration) {
        Configuration.Main -> Child.Main(newsMain(componentContext, mainOutput()))
        is Configuration.Detail -> Child.Detail(newsDetail(componentContext, detailOutput()))
    }

    private fun mainOutput() = { output: NewsMain.Output -> when (output) {
        is NewsMain.Output.Selected -> router.push(Configuration.Detail(1))
    } }

    private fun detailOutput() = { output: NewsDetail.Output -> when (output) {
        is NewsDetail.Output.Back -> router.pop()
    } }

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Main : Configuration()

        @Parcelize
        data class Detail(val itemId: Long) : Configuration()
    }
}
