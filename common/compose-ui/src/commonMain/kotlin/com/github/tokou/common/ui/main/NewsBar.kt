package com.github.tokou.common.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsBar(onRefresh: () -> Unit) = TopAppBar(
    title = { Title() },
    actions = { Actions(onRefresh) },
    navigationIcon = { Navigation() }
)

@Composable
private fun Title() {
    Column(modifier = Modifier.clickable { }.padding(6.dp)) {
        Text("Hacker News", style = MaterialTheme.typography.subtitle1)
        Text("Frontpage", style = MaterialTheme.typography.subtitle2)
    }
}

@Composable
private fun Actions(onRefresh: () -> Unit) {
    IconButton(onClick = onRefresh) {
        Icon(Icons.Filled.Sync, contentDescription = "Refresh")
    }
    IconButton(onClick = { }) {
        Icon(Icons.Filled.MoreVert, contentDescription = "More")
    }
}

@Composable
private fun Navigation() {
    IconButton(onClick = {}) {
        Icon(Icons.Filled.MenuOpen, contentDescription = "Menu")
    }
}
