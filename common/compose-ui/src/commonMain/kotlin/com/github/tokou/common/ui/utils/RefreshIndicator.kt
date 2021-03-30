package com.github.tokou.common.ui.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RefreshIndicator() = Surface(elevation = 10.dp, shape = CircleShape) {
    CircularProgressIndicator(
        modifier = Modifier
            .size(44.dp)
            .padding(8.dp)
    )
}
