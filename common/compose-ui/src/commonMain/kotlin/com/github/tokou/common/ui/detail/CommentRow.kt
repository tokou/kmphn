package com.github.tokou.common.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CommentRow(
    padding: Dp = 0.dp,
    isSelected: Boolean = false,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth()) {
        CommentPadding(padding)
        BoxWithSelection(isSelected = isSelected) {
            Column {
                CommentDivider()
                content()
            }
        }
    }
}

@Composable
fun CommentDivider() {
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.09f))
}
