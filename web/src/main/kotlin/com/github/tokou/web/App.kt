package com.github.tokou.web

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.destroy
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.ccfraser.muirwik.components.mContainer
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.styles.Breakpoint
import com.github.tokou.common.api.createApi
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.inMemoryDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import kotlinx.browser.window
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState

class App : RComponent<RProps, RState>() {

    private val lifecycle = LifecycleRegistry()
    private val componentContext = DefaultComponentContext(lifecycle = lifecycle)

    private val root = NewsRootComponent(
        componentContext = componentContext,
        uriHandler = { url -> window.open(url, "_blank") },
        storeFactory = DefaultStoreFactory,
        api = createApi(),
        database = createDatabase(inMemoryDatabaseDriver()),
    )

    override fun componentDidMount() {
        lifecycle.resume()
    }

    override fun componentWillUnmount() {
        lifecycle.destroy()
    }

    override fun RBuilder.render() {
        mCssBaseline()

        mContainer {
            renderableChild(NewsRootR::class, root)
        }
    }
}
