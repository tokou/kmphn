package com.github.tokou.common.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId

@Composable
fun CommentExpanded(
    comment: NewsDetail.Comment.Content.Expanded,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .clickable { onCommentClicked(comment.id) }
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 16.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            CommentHeader(comment, onUserClicked)
        }
        Spacer(Modifier.height(8.dp))
        RichText(text = comment.text, onLinkClicked = onLinkClicked)
    }
}
