package com.github.tokou.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.tokou.common.main.NewsMain
import com.github.tokou.common.main.NewsMain.Model
import com.github.tokou.common.main.NewsMain.News
import com.github.tokou.common.platform.MARGIN_SCROLLBAR
import com.github.tokou.common.platform.VerticalScrollbar
import com.github.tokou.common.platform.rememberScrollbarAdapter

@Composable
fun NewsListScreen(modifier: Modifier = Modifier, component: NewsMain) {
    val model by component.models.collectAsState(Model.Loading)

    Scaffold(
        topBar = { NewsBar(onRefresh = component::onRefresh) },
        modifier = modifier,
    ) {
        val m = model
        when (m) {
            Model.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Model.Error -> Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text("Error", style = MaterialTheme.typography.h6)
                    Button(onClick = component::onRefresh) {
                        Text("Retry", style = MaterialTheme.typography.button)
                    }
                }
            }
            is Model.Content -> NewsList(
                news = m.news,
                isLoadingMore = m.isLoadingMore,
                onLinkClick = { component.onNewsSelected(it.id, it.link) },
                onItemClick = { component.onNewsSecondarySelected(it.id) },
                onLoadMore = component::onLoadMoreSelected
            )

        }
    }
}

@Composable
fun NewsBar(onRefresh: () -> Unit) = TopAppBar(
    title = {
        Column(modifier = Modifier.clickable { }.padding(8.dp)) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("Hacker News", style = MaterialTheme.typography.subtitle1)
                Text("Frontpage", style = MaterialTheme.typography.subtitle2)
            }
        }
    },
    actions = {
        IconButton(onClick = onRefresh) {
            Icon(Icons.Filled.Sync, contentDescription = "Refresh")
        }
        IconButton(onClick = { }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "More")
        }
  },
    navigationIcon = {
        IconButton(onClick = {}) {
            Icon(Icons.Filled.MenuOpen, contentDescription = "Menu")
        }
    }
)

@Composable
fun NewsList(
    news: List<News>,
    isLoadingMore: Boolean,
    onLinkClick: (News) -> Unit,
    onItemClick: (News) -> Unit,
    onLoadMore: () -> Unit,
) {

    Box(Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(modifier = Modifier.fillMaxWidth(), state = state) {
            items(items = news) { item ->
                NewsRow(
                    item = item,
                    onLinkClick = onLinkClick,
                    onItemClick = onItemClick,
                )
                Divider()
            }
            item {
                    Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
                        val boxModifier = Modifier.fillMaxWidth().height(64.dp)
                        val loadModifier =
                            if (isLoadingMore) boxModifier
                            else boxModifier.clickable(onClick = onLoadMore)
                        CompositionLocalProvider(LocalContentAlpha provides medium) {
                            Box(modifier = loadModifier) {
                                val modifier = Modifier.align(Alignment.Center)
                                if (isLoadingMore) CircularProgressIndicator(modifier = modifier)
                                else Text(
                                    text = "Load More",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = modifier
                                )
                            }
                        }
                    }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state,
                itemCount = news.size,
                averageItemSize = 96.dp
            )
        )
    }
}

@Composable
fun NewsRow(
    modifier: Modifier = Modifier,
    item: News,
    onItemClick: (News) -> Unit = {},
    onLinkClick: (News) -> Unit = {},
) {
    Row(modifier = modifier.height(IntrinsicSize.Max)) {
        Column(
            modifier = Modifier
                .clickable { onLinkClick(item) }
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
        Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
            Column(
                modifier = Modifier
                    .clickable { onItemClick(item) }
                    .width(64.dp)
                    .fillMaxHeight()
                    .padding(vertical = 16.dp)
                    .padding(end = MARGIN_SCROLLBAR)
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = item.comments,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primarySurface,
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
    }
}
