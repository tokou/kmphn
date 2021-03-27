package com.github.tokou.common.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        // Seems to cause jankiness, disable for now
        /*
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
        )
         */
        Text(
            text = "...",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .padding(vertical = 4.dp)
        )
    }
}
