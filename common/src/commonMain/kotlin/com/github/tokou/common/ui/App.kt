package com.github.tokou.common.ui

import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.*
import com.github.tokou.common.getPlatformName
import com.github.tokou.common.ui.theme.AppTheme

@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    AppTheme {
        Button(onClick = {
            text = "Hello, ${getPlatformName()}"
        }) {
            Text(text)
        }
    }
}
