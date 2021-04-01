package com.github.tokou.common.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
            Model.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Model.Error -> Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text("Error", style = MaterialTheme.typography.h6)
                    Button(onClick = component::onRefresh) {
                        Text("Retry", style = MaterialTheme.typography.button)
                    }
                }
            }
            is Model.Content -> NewsList(
                item = m.items,
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
