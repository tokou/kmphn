package com.github.tokou.common.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.tokou.common.platform.PlatformTheme

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    PlatformTheme {
        MaterialTheme(
            colors = if (darkTheme) AppDarkColors else AppColors,
            typography = AppTypography(),
            content = content,
        )
    }
}
