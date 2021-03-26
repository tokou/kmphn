package com.github.tokou.common.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.platform.VerticalScrollbar
import com.github.tokou.common.platform.rememberScrollbarAdapter
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId

@Composable
fun NewsDetailContent(
    content: NewsDetail.Model.Content,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit
) {
    val state = rememberLazyListState()

    Box(Modifier.fillMaxSize()) {
        NewsComments(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            comments = content.comments,
            onCommentClicked = onCommentClicked,
            onUserClicked = onUserClicked,
            onLinkClicked = onLinkClicked,
        ) {
            NewsHeader(
                header = content.header,
                onUserClicked = onUserClicked,
                onLinkClicked = onLinkClicked
            )
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
