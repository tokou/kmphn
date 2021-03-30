package com.github.tokou.common.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val commentPaddingStep = 16.dp

@Composable
fun CommentPadding(padding: Dp) {
    var r = padding
    while (r > 0.dp) {
        Box(modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
        )
        Box(modifier = Modifier
            .width(commentPaddingStep - 1.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colors.onBackground.copy(alpha = 0.05f))
        )
        r -= commentPaddingStep
    }
}
