package com.github.tokou.common.platform

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.ScrollbarAdapter as ComposeScrollbarAdapter
import androidx.compose.foundation.VerticalScrollbar as ComposeVerticalScrollbar
import androidx.compose.foundation.rememberScrollbarAdapter as composeRememberScrollbarAdapter

actual val MARGIN_SCROLLBAR: Dp = 8.dp

actual typealias ScrollbarAdapter = ComposeScrollbarAdapter

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun rememberScrollbarAdapter(
    scrollState: LazyListState,
    itemCount: Int,
    averageItemSize: Dp
): ScrollbarAdapter =
    composeRememberScrollbarAdapter(
        scrollState = scrollState,
    )

@Composable
actual fun VerticalScrollbar(
    modifier: Modifier,
    adapter: ScrollbarAdapter
) {
    ComposeVerticalScrollbar(
        modifier = modifier,
        adapter = adapter
    )
}
