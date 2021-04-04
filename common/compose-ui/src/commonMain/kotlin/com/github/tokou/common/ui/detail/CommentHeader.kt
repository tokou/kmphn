package com.github.tokou.common.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.utils.UserId

@Composable
fun CommentHeader(
    comment: NewsDetail.Comment.Content,
    onUserClicked: (UserId) -> Unit,
    content: @Composable (RowScope) -> Unit = {}
) {
    Row {
        CommentUser(comment.user, comment.isOp, onUserClicked)
        Spacer(Modifier.width(16.dp))
        CommentTime(comment.time)
        content(this)
    }
}

@Composable
private fun RowScope.CommentUser(
    user: UserId,
    isOp: Boolean = false,
    onUserClicked: (UserId) -> Unit = {}
) {
    val color = if (isOp) MaterialTheme.colors.primary else Color.Transparent
    val textColor = if (isOp) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
    val padding = if (isOp) 4.dp else 0.dp

    if (!isOp) {
        Icon(
            imageVector = Icons.Filled.PermIdentity,
            contentDescription = null,
            modifier = Modifier.size(20.dp).align(Alignment.CenterVertically),
            tint = MaterialTheme.colors.primary.copy(alpha = LocalContentAlpha.current)
        )
        Spacer(Modifier.width(4.dp))
    }

    Surface(color = color) {
        Text(
            text = user,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(padding)
                .clickable { onUserClicked(user) }
                .align(Alignment.CenterVertically),
            color = textColor
        )
    }
}

@Composable
private fun RowScope.CommentTime(time: String) {
    Icon(
        imageVector = Icons.Filled.HourglassBottom,
        contentDescription = null,
        modifier = Modifier.size(16.dp).align(Alignment.CenterVertically)
    )
    Spacer(Modifier.width(4.dp))
    Text(
        text = time,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.align(Alignment.CenterVertically)
    )
}
