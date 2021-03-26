package com.github.tokou.common.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxWithSelection(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
        if (isSelected) Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxHeight()
                .width(4.dp)
        )
    }
}
