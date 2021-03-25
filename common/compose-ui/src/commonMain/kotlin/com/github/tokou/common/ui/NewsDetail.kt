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
import com.github.tokou.common.utils.UserId

@Composable
fun NewsDetailScreen(modifier: Modifier = Modifier, component: NewsDetail) {
    val model by component.models.collectAsState(Model.Loading)

    NewsDetailScaffold(
        modifier = modifier,
        onBack = component::onBack
    ) {
        val m = model
        when (m) {
            Model.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is Model.Content -> NewsDetailContent(m, component::onCommentClicked)
        }
    }
}

@Composable
fun NewsDetailScaffold(
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    onBack: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = { NewsDetailBar(showBack = showBack, onBack = onBack) },
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsDetailBar(showBack: Boolean = true, onBack: () -> Unit) = TopAppBar(
    title = {},
    navigationIcon = {
        AnimatedVisibility(showBack) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable(onClick = onBack).padding(16.dp)
            )
        }
    },
    actions = {
        Icon(Icons.Filled.Share, contentDescription = "Share", modifier = Modifier.clickable {  }.padding(16.dp))
        Icon(Icons.Filled.MoreVert, contentDescription = "More", modifier = Modifier.clickable {  }.padding(16.dp))
    },
)


@Composable
fun NewsDetailContent(content: Model.Content, onCommentClicked: (Comment.Content) -> Unit) {
    val state = rememberLazyListState()

    Box(Modifier.fillMaxSize()) {
        NewsComments(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            comments = content.comments,
            onCommentClicked = onCommentClicked,
            onUserClicked = {},
            onLinkClicked = {},
        ) {
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
    onCommentClicked: (Comment.Content) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String) -> Unit,
    header: @Composable () -> Unit = {}
) {

    fun LazyListScope.commentTree(comments: List<Comment>, padding: Dp = 0.dp) {
        comments.forEach { c ->
            when (c) {
                is Comment.Loading -> item {
                    Text(
                        text = "Loading comments...",
                        modifier = Modifier
                            .padding(start = padding + 16.dp)
                            .fillMaxWidth()
                    )
                }
                is Comment.Content.Collapsed -> item {
                    Text(
                        text = "Commend collapsed ${c.id} ${c.childrenCount}",
                        modifier = Modifier
                            .padding(start = padding)
                            .clickable { onCommentClicked(c) }
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
                is Comment.Content.Expanded -> {
                    item {
                        Text(
                            text = "Comment expanded ${c.id}",
                            modifier = Modifier
                                .padding(start = padding)
                                .clickable { onCommentClicked(c) }
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                    }
                    commentTree(c.children, padding + 16.dp)
                }
            }
        }
    }

    LazyColumn(modifier = modifier, state = state) {
        item { header() }
        commentTree(comments)
    }
}

