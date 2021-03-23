package com.github.tokou.common.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.main.NewsMain
import com.github.tokou.common.main.NewsMainComponent
import com.github.tokou.common.root.NewsRoot.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel

interface NewsRoot {
    val routerState: Value<RouterState<*, Child>>

    sealed class Child {
        data class Main(val component: NewsMain) : Child()
        data class Detail(val component: NewsDetail) : Child()
    }
}

class NewsRootComponent(
    componentContext: ComponentContext,
    private val newsMain: (ComponentContext, SendChannel<NewsMain.Output>) -> NewsMain,
    private val newsDetail: (ComponentContext, SendChannel<NewsDetail.Output>) -> NewsDetail,
): NewsRoot, ComponentContext by componentContext {

    constructor(componentContext: ComponentContext) : this(
        componentContext = componentContext,
        newsMain = { context, _ ->
            NewsMainComponent(context)
        },
        newsDetail = { _, _ ->
            object : NewsDetail {
                override val models: Value<NewsDetail.Model>
                    get() = MutableValue(NewsDetail.Model(NewsDetail.News(1, "", null, "", "", "", "")))
            }
        }
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

    private fun mainOutput(): SendChannel<NewsMain.Output> {
        val c = Channel<NewsMain.Output>()
        return c
    }

    private fun detailOutput(): SendChannel<NewsDetail.Output> {
        val c = Channel<NewsDetail.Output>()
        return c
    }

    override val routerState: Value<RouterState<*, Child>> = router.state

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Main : Configuration()

        @Parcelize
        data class Detail(val itemId: Long) : Configuration()
    }
}
