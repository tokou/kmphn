package com.github.tokou.common.ui.detail

import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun PopupMenu(
    modifier: Modifier = Modifier,
    menuItems: List<String>,
    onClickCallbacks: List<() -> Unit>,
    showMenu: Boolean,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = showMenu,
        modifier = modifier.background(MaterialTheme.colors.primarySurface),
        onDismissRequest = { onDismiss() }
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            menuItems.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onDismiss()
                    onClickCallbacks[index]
                }) {
                    val color = MaterialTheme.colors.contentColorFor(
                        MaterialTheme.colors.primarySurface
                    ).copy(alpha = ContentAlpha.medium)
                    Text(text = item, color = color)
                }
            }
        }
    }
}
