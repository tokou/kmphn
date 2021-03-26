package com.github.tokou.common.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NewsDetailScaffold(
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    onBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val onMore = { showMenu = true }
    Scaffold(
        modifier = modifier,
        topBar = { NewsDetailBar(showBack = showBack, onBack = onBack, onMore = onMore) },
        content = content
    )

    Box(Modifier.fillMaxSize().wrapContentSize(Alignment.TopEnd)) {
        if (showMenu) PopupMenu(
            menuItems = listOf("Hello", "World"),
            onClickCallbacks = listOf({}, {}),
            showMenu = showMenu,
            onDismiss = { showMenu = false }
        )
    }
}
