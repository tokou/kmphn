package com.github.tokou.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.api.createApi
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.peristentDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.root.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme
import com.github.tokou.common.utils.logger

class MainActivity : AppCompatActivity() {

    private val tabsHelper = CustomTabsHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.ensureRuntimeTheme(this)
        super.onCreate(savedInstanceState)
        tabsHelper.bind()
        setContent {
            AppTheme {
                val color = MaterialTheme.colors.primarySurface
                val rootComponent = rememberRootComponent {
                    NewsRootComponent(
                        componentContext = it,
                        uriHandler = tabsHelper.createUriHandler(color),
                        storeFactory = DefaultStoreFactory.logging(),
                        api = createApi(),
                        database = createDatabase(peristentDatabaseDriver(this))
                    )
                }
                NewsRoot(component = rootComponent)
            }
        }
    }
}

private val storeLogger = object : Logger {
    override fun log(text: String) {
        logger.d("STORE") { text }
    }
}

private fun StoreFactory.logging(): LoggingStoreFactory = LoggingStoreFactory(this, storeLogger)
