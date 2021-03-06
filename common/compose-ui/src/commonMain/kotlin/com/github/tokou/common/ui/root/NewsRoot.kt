package com.github.tokou.common.ui.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.github.tokou.common.root.NewsRoot
import com.github.tokou.common.ui.detail.NewsDetailScreen
import com.github.tokou.common.ui.main.NewsListScreen

@Composable
fun NewsRoot(component: NewsRoot) {
    Children(routerState = component.routerState) { (_: Any, child: NewsRoot.Child) -> when (child) {
        is NewsRoot.Child.Main -> NewsListScreen(component = child.component)
        is NewsRoot.Child.Detail -> NewsDetailScreen(component = child.component)
    } }
}
