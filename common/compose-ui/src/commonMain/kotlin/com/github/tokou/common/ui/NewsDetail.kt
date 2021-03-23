package com.github.tokou.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsDetailScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { NewsDetailBar() },
    ) {
        NewsDetail()
    }
}

@Composable
fun NewsDetailBar() = TopAppBar(
    title = {},
    actions = {
        Icon(Icons.Filled.Share, contentDescription = "Share", modifier = Modifier.clickable {  }.padding(4.dp))
        Icon(Icons.Filled.MoreVert, contentDescription = "More", modifier = Modifier.clickable {  }.padding(4.dp))
    },
)

@Composable
fun NewsDetail() {

}
