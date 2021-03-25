package com.github.tokou.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetail.*
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
                itemCount = content.comments.size, // TODO: improve count
                averageItemSize = 16.dp
            )
        )
    }

}

@Composable
fun NewsHeader(modifier: Modifier = Modifier, header: Header) {
    Surface(
        color = MaterialTheme.colors.primarySurface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            with(header) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1
                )
                Row {
                    Text(points)
                    Spacer(Modifier.width(12.dp))
                    Text(user)
                    Spacer(Modifier.width(12.dp))
                    Text(time)
                    Spacer(Modifier.width(12.dp))
                    Text(commentsCount)
                }
                text?.let { Text(it) }
                link?.let { Text(it) }
                Text(hnLink)
            }
        }
    }
}

fun LazyListScope.commentTree(
    comments: List<Comment>,
    padding: Dp = 0.dp,
    onCommentClicked: (Comment.Content) -> Unit
) {
    comments.forEach { c ->
        commentRow(c, padding, onCommentClicked)
    }
}

@Composable
fun CommentLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .padding(vertical = 4.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun CommentCollapsed(
    comment: Comment.Content.Collapsed,
    onCommentClicked: (Comment.Content) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onCommentClicked(comment) }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = comment.user)
        Spacer(Modifier.width(32.dp))
        Text(text = comment.time)
        Spacer(Modifier.width(16.dp))
        Text(text = comment.childrenCount)
    }
}

@Composable
fun CommentExpanded(comment: Comment.Content.Expanded, onCommentClicked: (Comment.Content) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onCommentClicked(comment) }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row {
            Text(text = comment.user)
            Spacer(Modifier.width(32.dp))
            Text(text = comment.time)
        }
        Spacer(Modifier.height(16.dp))
        Text(text = comment.text)
    }
}

@Composable
fun CommentPadding(padding: Dp) {
    var r = padding
    while (r > 0.dp) {
        Box(modifier = Modifier
            .width(1.dp)
            .fillMaxHeight()
        )
        Box(modifier = Modifier
            .width(step - 1.dp)
            .fillMaxHeight()
            .background(Color.Black.copy(alpha = 0.05f))
        )
        r -= step
    }
}

@Composable
fun CommentRow(padding: Dp = 0.dp, content: @Composable () -> Unit) {
    Row(modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth()) {
        CommentPadding(padding)
        content()
    }
}

val step = 16.dp

fun LazyListScope.commentRow(
    comment: Comment,
    padding: Dp,
    onCommentClicked: (Comment.Content) -> Unit
) {
    item {
        CommentRow(padding) {
            Column {
                Divider(color = Color.Black.copy(alpha = 0.09f))
                when (comment) {
                    is Comment.Loading -> CommentLoader()
                    is Comment.Content.Collapsed -> CommentCollapsed(comment, onCommentClicked)
                    is Comment.Content.Expanded -> CommentExpanded(comment, onCommentClicked)
                }
            }
        }
    }
    if (comment is Comment.Content.Expanded) commentTree(comment.children, padding + step, onCommentClicked)
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

    LazyColumn(modifier = modifier, state = state) {
        item { header() }
        commentTree(comments, onCommentClicked = onCommentClicked)
    }
}

