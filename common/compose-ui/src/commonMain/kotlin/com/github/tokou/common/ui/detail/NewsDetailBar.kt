package com.github.tokou.common.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsDetailBar(showBack: Boolean = true, onBack: () -> Unit, onMore: () -> Unit) = TopAppBar(
    title = {},
    navigationIcon = {
        AnimatedVisibility(showBack) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    },
    actions = {
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }
        IconButton(onClick = onMore) {
            Icon(Icons.Filled.MoreVert, contentDescription = "More")
        }
    },
)
