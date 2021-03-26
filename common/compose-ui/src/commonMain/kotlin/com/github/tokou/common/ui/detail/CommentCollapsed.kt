package com.github.tokou.common.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetail.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId

@Composable
fun CommentCollapsed(
    comment: Comment.Content.Collapsed,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onCommentClicked(comment.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            CommentHeader(comment, onUserClicked) {
                Spacer(Modifier.width(16.dp))
                CommentChildrenCount(comment.childrenCount)
            }
        }
    }
}

@Composable
fun RowScope.CommentChildrenCount(count: String) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
        Text(
            text = count,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
