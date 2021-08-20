package com.github.tokou.web

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.api.createApi
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.inMemoryDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import kotlinx.browser.window
import react.*

class App : RComponent<PropsWithChildren, State>() {

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
        renderableChild(NewsRootR::class, root)
    }
}
