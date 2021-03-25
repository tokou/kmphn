package com.github.tokou.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.github.tokou.common.database.createDatabase
import com.github.tokou.common.database.peristentDatabaseDriver
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val rootComponent = rememberRootComponent {
                    NewsRootComponent(
                        componentContext = it,
                        storeFactory = LoggingStoreFactory(DefaultStoreFactory),
                        database = createDatabase(peristentDatabaseDriver(this))
                    )
                }
                NewsRoot(component = rootComponent)
            }
        }
    }
}
