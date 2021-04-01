package com.github.tokou.common.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorLayout(
    modifier: Modifier = Modifier,
    text: String,
    actionTitle: String = "Retry",
    onClick: () -> Unit
) = Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.align(Alignment.Center)) {
        Text(text, style = MaterialTheme.typography.h6)
        Button(onClick = onClick) {
            Text(actionTitle, style = MaterialTheme.typography.button)
        }
    }
}
