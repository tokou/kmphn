package com.github.tokou.common.platform

import androidx.compose.runtime.Composable

@Composable
expect fun SwipeToRefreshLayout(
    refreshingState: Boolean,
    onRefresh: () -> Unit,
    refreshIndicator: @Composable () -> Unit,
    content: @Composable () -> Unit
)
