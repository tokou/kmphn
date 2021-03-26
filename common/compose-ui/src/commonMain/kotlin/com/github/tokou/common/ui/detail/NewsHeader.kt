package com.github.tokou.common.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.utils.UserId

@Composable
fun NewsHeader(
    modifier: Modifier = Modifier,
    header: NewsDetail.Header,
    onLinkClicked: (String, Boolean) -> Unit,
    onUserClicked: (UserId) -> Unit
) {
    Surface(
        color = MaterialTheme.colors.primarySurface,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            with(header) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(Modifier.height(14.dp))
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Row {
                            Surface(color = MaterialTheme.colors.primaryVariant.copy(ContentAlpha.medium)) {
                                Text(
                                    text = points,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(4.dp).align(Alignment.CenterVertically)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = user,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.align(Alignment.CenterVertically).clickable { onUserClicked(user) }
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = time,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(Modifier.width(12.dp))
                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                Icon(
                                    Icons.Filled.Comment, null, Modifier.size(16.dp).align(
                                        Alignment.CenterVertically))
                            }
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = commentsCount,
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                    link?.let {
                        Row {
                            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                Icon(
                                    Icons.Filled.OpenInNew, null, Modifier.size(16.dp).align(
                                        Alignment.CenterVertically))
                            }
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = it,
                                style = MaterialTheme.typography.body1,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.clickable { onLinkClicked(it, false) }.align(
                                    Alignment.CenterVertically)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    Row {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            Icon(Icons.Filled.OpenInNew, null, Modifier.size(16.dp).align(Alignment.CenterVertically))
                        }
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = hnLink,
                            style = MaterialTheme.typography.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.clickable { onLinkClicked(hnLink, true) }.align(
                                Alignment.CenterVertically)
                        )
                    }
                }
                text?.let {
                    Surface(
                        color = MaterialTheme.colors.surface.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colors.onSurface
                    ) {
                        RichText(modifier = Modifier.padding(16.dp), text = it, onLinkClicked = { _, _ -> })
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
