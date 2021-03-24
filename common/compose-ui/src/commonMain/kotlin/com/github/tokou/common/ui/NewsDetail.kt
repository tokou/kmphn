package com.github.tokou.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetail.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import com.github.tokou.common.platform.VerticalScrollbar
import com.github.tokou.common.platform.rememberScrollbarAdapter

@Composable
fun NewsDetailScreen(modifier: Modifier = Modifier, component: NewsDetail) {
    val model by component.models.collectAsState(Model.Empty)

    Scaffold(
        modifier = modifier,
        topBar = { NewsDetailBar(onBack = component::onBack) },
    ) {
        val m = model
        when (m) {
            Model.Empty -> Text("No Story Selected")
            Model.Loading -> Text("Loading...")
            is Model.Content -> NewsDetailContent(m)
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
fun NewsDetailContent(content: Model.Content) {
    val state = rememberLazyListState()

    Box(Modifier.fillMaxSize()) {
        NewsComments(modifier = Modifier.fillMaxWidth(), state = state, comments = content.comments) {
            NewsHeader(header = content.header)
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state,
                itemCount = content.comments.size,
                averageItemSize = 16.dp
            )
        )
    }

}

@Composable
fun NewsHeader(header: Header) {
    Text(text = header.toString())
}

@Composable
fun NewsComments(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
    state: LazyListState = rememberLazyListState(),
    header: @Composable () -> Unit = {}
) {
    LazyColumn(modifier = modifier, state = state) {
        item { header() }
        commentTree(comments)
    }
}

private fun LazyListScope.commentTree(comments: List<Comment>, padding: Dp = 0.dp) {
    comments.forEach { c ->
        when (c) {
            is Comment.Loading -> item { Text("Loading comment ${c.id}", modifier = Modifier.padding(start = padding)) }
            is Comment.Content.Collapsed -> item { Text("Commend collapsed ${c.id}", modifier = Modifier.padding(start = padding)) }
            is Comment.Content.Expanded -> {
                item { Text("Comment expanded ${c.id}", modifier = Modifier.padding(start = padding)) }
                commentTree(c.children, padding + 16.dp)
            }
        }
    }
}
