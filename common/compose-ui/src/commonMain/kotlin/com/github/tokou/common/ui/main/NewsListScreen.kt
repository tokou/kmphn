package com.github.tokou.common.ui.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import com.github.tokou.common.main.NewsMain
import com.github.tokou.common.main.NewsMain.Item
import com.github.tokou.common.main.NewsMain.Model

typealias Callback = (Item) -> Unit

@Composable
fun NewsListScreen(modifier: Modifier = Modifier, component: NewsMain) {
    val model by component.models.asState()

    Scaffold(
        topBar = { NewsBar(onRefresh = component::onRefresh) },
        modifier = modifier,
    ) {
        val m = model
        when (m) {
            Model.Loading -> Loader()
            Model.Error -> ErrorLayout(text = "Error", onClick = component::onRefresh)
            is Model.Content -> NewsList(
                items = m.items,
                isLoadingMore = m.isLoadingMore,
                isRefreshing = m.isRefreshing,
                canLoadMore = m.canLoadMore,
                onLinkClick = { component.onNewsSelected(it.id, it.link) },
                onItemClick = { component.onNewsSecondarySelected(it.id) },
                onLoadMore = component::onLoadMoreSelected,
                onRefresh = component::onRefresh
            )

        }
    }
}
