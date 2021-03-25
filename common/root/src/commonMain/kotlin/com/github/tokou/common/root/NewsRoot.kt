package com.github.tokou.common.root

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetailComponent
import com.github.tokou.common.main.NewsMain
import com.github.tokou.common.main.NewsMainComponent
import com.github.tokou.common.root.NewsRoot.Child
import com.arkivanov.decompose.ComponentContext

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
    private val newsDetail: (ComponentContext, itemId: Long, (NewsDetail.Output) -> Unit) -> NewsDetail,
): NewsRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        database: NewsDatabase
    ) : this(
        componentContext = componentContext,
        newsMain = { context, output -> NewsMainComponent(context, output) },
        newsDetail = { context, itemId, output ->
            NewsDetailComponent(
                componentContext = context,
                storeFactory = storeFactory,
                database = database,
                itemId = itemId,
                onOutput = output,
            )
        }
    )

    private val router = router(
        initialConfiguration = { Configuration.Main },
        handleBackButton = true,
        componentFactory = ::createChild,
        configurationClass = Configuration::class
    )

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child = when (configuration) {
        Configuration.Main -> Child.Main(newsMain(componentContext, mainOutput()))
        is Configuration.Detail -> Child.Detail(newsDetail(componentContext, configuration.itemId, detailOutput()))
    }

    private fun mainOutput() = { output: NewsMain.Output -> when (output) {
        is NewsMain.Output.Selected -> router.push(Configuration.Detail(output.id))
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
