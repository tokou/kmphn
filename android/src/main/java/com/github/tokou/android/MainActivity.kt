package com.github.tokou.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.api.createApi
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.peristentDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.root.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.ensureRuntimeTheme(this)
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val rootComponent = rememberRootComponent {
                    NewsRootComponent(
                        componentContext = it,
                        uriHandler = { uri -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri))) },
                        uriHandler = tabsHelper.createUriHandler(color),
                        storeFactory = LoggingStoreFactory(DefaultStoreFactory),
                        api = createApi(),
                        database = createDatabase(peristentDatabaseDriver(this))
                    )
                }
                NewsRoot(component = rootComponent)
            }
        }
    }
}
