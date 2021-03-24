package com.github.tokou.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetail.*
import androidx.compose.runtime.getValue

@Composable
fun NewsDetailScreen(modifier: Modifier = Modifier, component: NewsDetail) {
    val model by component.models.asState()

    Scaffold(
        modifier = modifier,
        topBar = { NewsDetailBar(onBack = component::onBack) },
    ) {
        Column {
            Text(text = model.maxitem.toString())
            model.news?.let { NewsDetail(news = it) } ?: Text("No news")
        }
    }
}

@Composable
fun NewsDetailBar(onBack: () -> Unit) = TopAppBar(
    title = {},
    navigationIcon = {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.clickable(onClick = onBack).padding(4.dp)
        )
    },
    actions = {
        Icon(Icons.Filled.Share, contentDescription = "Share", modifier = Modifier.clickable {  }.padding(4.dp))
        Icon(Icons.Filled.MoreVert, contentDescription = "More", modifier = Modifier.clickable {  }.padding(4.dp))
    },
)

@Composable
fun NewsDetail(news: News) {
    Text(text = news.toString())
}
