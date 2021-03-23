package com.github.tokou.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.github.tokou.common.root.NewsRootComponent
import com.github.tokou.common.ui.NewsRoot
import com.github.tokou.common.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val rootComponent = rememberRootComponent { NewsRootComponent(it, lifecycleScope) }
                NewsRoot(component = rootComponent)
            }
        }
    }
}
