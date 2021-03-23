package com.github.tokou.common.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.github.tokou.common.root.NewsRoot

@Composable
fun NewsRoot(component: NewsRoot) {
    Children(routerState = component.routerState) { child, _ -> when (child) {
        is NewsRoot.Child.Main -> NewsListScreen(component = child.component)
        is NewsRoot.Child.Detail -> NewsDetailScreen()
    } }
}
