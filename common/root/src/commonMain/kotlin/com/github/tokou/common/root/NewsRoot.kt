package com.github.tokou.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetailComponent
import com.github.tokou.common.main.NewsMain
import com.github.tokou.common.main.NewsMainComponent
import com.github.tokou.common.root.NewsRoot.*

interface NewsRoot {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: NewsMain) : Child()
        data class Detail(val component: NewsDetail) : Child()
    }
}

class NewsRootComponent(
    componentContext: ComponentContext,
    private val newsMain: (ComponentContext, (NewsMain.Output) -> Unit) -> NewsMain,
    private val newsDetail: (ComponentContext, (NewsDetail.Output) -> Unit) -> NewsDetail,
): NewsRoot, ComponentContext by componentContext {

    constructor(componentContext: ComponentContext) : this(
        componentContext = componentContext,
        newsMain = { context, output -> NewsMainComponent(context, output) },
        newsDetail = { context, output -> NewsDetailComponent(context, output) }
    )

    private val router = router<Configuration, Child>(
        initialConfiguration = Configuration.Main,
        handleBackButton = true,
        componentFactory = ::createChild
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
