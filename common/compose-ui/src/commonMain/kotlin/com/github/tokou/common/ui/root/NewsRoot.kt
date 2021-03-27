package com.github.tokou.common.ui.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.github.tokou.common.root.NewsRoot
import com.github.tokou.common.ui.detail.NewsDetailScreen
import com.github.tokou.common.ui.main.NewsListScreen
import com.github.tokou.common.ui.theme.slideIn

@Composable
fun NewsRoot(component: NewsRoot) {
    Children(routerState = component.routerState, animation = slideIn()) { child, _ -> when (child) {
        is NewsRoot.Child.Main -> NewsListScreen(component = child.component)
        is NewsRoot.Child.Detail -> NewsDetailScreen(component = child.component)
    } }
}
