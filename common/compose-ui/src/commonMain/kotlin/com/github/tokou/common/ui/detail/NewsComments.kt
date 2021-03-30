package com.github.tokou.common.ui.detail

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId


@Composable
fun NewsComments(
    modifier: Modifier = Modifier,
    comments: List<NewsDetail.Comment>,
    state: LazyListState = rememberLazyListState(),
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit,
    header: @Composable () -> Unit = {}
) {

    LazyColumn(modifier = modifier, state = state) {
        item { header() }
        commentTree(
            comments = comments,
            onCommentClicked = onCommentClicked,
            onUserClicked = onUserClicked,
            onLinkClicked = onLinkClicked
        )
        item { CommentDivider() }
    }
}

fun LazyListScope.commentTree(
    comments: List<NewsDetail.Comment>,
    padding: Dp = 0.dp,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit,
) {
    comments.forEach { c ->
        commentRow(c, padding, onCommentClicked, onUserClicked, onLinkClicked)
    }
}

fun LazyListScope.commentRow(
    comment: NewsDetail.Comment,
    padding: Dp,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit,
) {
    val key = when (comment) {
        NewsDetail.Comment.Loading -> null
        is NewsDetail.Comment.Content.Collapsed -> comment.id
        is NewsDetail.Comment.Content.Expanded -> comment.id
    }
    item(key) {
        when (comment) {
            NewsDetail.Comment.Loading -> CommentRow(padding) { CommentLoader() }
            is NewsDetail.Comment.Content.Collapsed -> CommentRow(padding, comment.isSelected) { CommentCollapsed(comment, onCommentClicked, onUserClicked) }
            is NewsDetail.Comment.Content.Expanded -> CommentRow(padding, comment.isSelected) {
                CommentExpanded(comment, onCommentClicked, onUserClicked, onLinkClicked)
            }
        }
    }
    if (comment is NewsDetail.Comment.Content.Expanded) commentTree(
        comments = comment.children,
        padding = padding + commentPaddingStep,
        onCommentClicked = onCommentClicked,
        onUserClicked = onUserClicked,
        onLinkClicked = onLinkClicked,
    )
}
