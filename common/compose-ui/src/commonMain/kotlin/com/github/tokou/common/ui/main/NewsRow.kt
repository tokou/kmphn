package com.github.tokou.common.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.tokou.common.main.NewsMain.Item
import com.github.tokou.common.platform.MARGIN_SCROLLBAR


private val Item = compositionLocalOf<Item> { error("No current item") }
private val LinkClick = compositionLocalOf<Callback> { {} }
private val ItemClick = compositionLocalOf<Callback> { {} }

@Composable
fun NewsRow(
    modifier: Modifier = Modifier,
    item: Item,
    onItemClick: Callback = {},
    onLinkClick: Callback = {},
) {
    CompositionLocalProvider(Item provides item) {
        Row(modifier = modifier.height(IntrinsicSize.Max)) {
            CompositionLocalProvider(LinkClick provides onLinkClick) {
                LeftColumn()
            }
            Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
                CompositionLocalProvider(ItemClick provides onItemClick) {
                    RightColumn()
                }
            }
        }
    }
}

@Composable
private fun RowScope.LeftColumn() {
    val item = Item.current
    val linkClick = LinkClick.current
    Column(
        modifier = Modifier
            .clickable { linkClick(item) }
            .weight(1f)
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.subtitle1,
        )
        Spacer(Modifier.height(16.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row {
                Text(
                    text = item.user,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2,
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = item.time,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
            item.link?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2,
                )
            }
        }
    }
}

@Composable
private fun RightColumn() {
    val item = Item.current
    val itemClick = ItemClick.current
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Column(
            modifier = Modifier
                .clickable { itemClick(item) }
                .width(64.dp)
                .fillMaxHeight()
                .padding(vertical = 16.dp)
                .padding(end = MARGIN_SCROLLBAR)
        ) {
            Text(
                text = item.comments,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = item.points,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle2,
            )
        }
    }
}
