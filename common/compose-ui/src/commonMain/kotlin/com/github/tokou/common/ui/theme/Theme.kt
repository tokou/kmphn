package com.github.tokou.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.tokou.common.platform.PlatformTheme

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    PlatformTheme {
        MaterialTheme(
            typography = AppTypography(),
            content = content,
        )
    }
}
