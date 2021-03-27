package com.github.tokou.common.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.tokou.common.main.NewsMain.Item
import com.github.tokou.common.platform.VerticalScrollbar
import com.github.tokou.common.platform.rememberScrollbarAdapter


@Composable
fun NewsList(
    item: List<Item>,
    isLoadingMore: Boolean,
    canLoadMore: Boolean,
    onLinkClick: Callback,
    onItemClick: Callback,
    onLoadMore: () -> Unit,
) {

    Box(Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(modifier = Modifier.fillMaxWidth(), state = state) {
            items(items = item) { item ->
                NewsRow(item = item, onLinkClick = onLinkClick, onItemClick = onItemClick)
                Divider()
            }
            if (canLoadMore) item {
                LoadMore(isLoadingMore = isLoadingMore, onLoadMore = onLoadMore)
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state,
                itemCount = item.size,
                averageItemSize = 96.dp
            )
        )
    }
}


@Composable
private fun LoadMore(
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit
) {
    Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
        val boxModifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .composed {
                if (isLoadingMore) this
                else clickable(onClick = onLoadMore)
            }
        CompositionLocalProvider(LocalContentAlpha provides medium) {
            Box(modifier = boxModifier) {
                val modifier = Modifier.align(Alignment.Center)
                if (isLoadingMore) CircularProgressIndicator(modifier = modifier)
                else Text(
                    text = "Load More",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = modifier
                )
            }
        }
    }
}
