package com.github.tokou.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.ContentAlpha.disabled
import androidx.compose.material.ContentAlpha.high
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetail.*
import com.github.tokou.common.platform.VerticalScrollbar
import com.github.tokou.common.platform.rememberScrollbarAdapter
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId

@Composable
fun NewsDetailScreen(modifier: Modifier = Modifier, component: NewsDetail) {
    val model by component.models.collectAsState(Model.Loading)

    NewsDetailScaffold(
        modifier = modifier,
        onBack = component::onBack,
    ) {
        val m = model
        when (m) {
            Model.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Model.Error -> Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    Text("Error", style = MaterialTheme.typography.h6)
                    Button(onClick = component::onRetry) {
                        Text("Retry", style = MaterialTheme.typography.button)
                    }
                }
            }
            is Model.Content -> NewsDetailContent(
                content = m,
                onCommentClicked = component::onCommentClicked,
                onUserClicked = component::onUserClicked,
                onLinkClicked = component::onLinkClicked,
            )
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
    var showMenu by remember { mutableStateOf(false) }
    val onMore = { showMenu = true }
    Scaffold(
        modifier = modifier,
        topBar = { NewsDetailBar(showBack = showBack, onBack = onBack, onMore = onMore) },
        content = content
    )

    Box(Modifier.fillMaxSize().wrapContentSize(Alignment.TopEnd)) {
        if (showMenu) PopupMenu(
            menuItems = listOf("Hello", "World"),
            onClickCallbacks = listOf({}, {}),
            showMenu = showMenu,
            onDismiss = { showMenu = false }
        )
    }
}

@Composable
fun PopupMenu(
    modifier: Modifier = Modifier,
    menuItems: List<String>,
    onClickCallbacks: List<() -> Unit>,
    showMenu: Boolean,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = showMenu,
        modifier = modifier.background(MaterialTheme.colors.primarySurface),
        onDismissRequest = { onDismiss() }
    ) {
        CompositionLocalProvider(LocalContentAlpha provides medium) {
            menuItems.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onDismiss()
                    onClickCallbacks[index]
                }) {
                    val color = MaterialTheme.colors.contentColorFor(
                        MaterialTheme.colors.primarySurface
                    ).copy(alpha = medium)
                    Text(text = item, color = color)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewsDetailBar(showBack: Boolean = true, onBack: () -> Unit, onMore: () -> Unit) = TopAppBar(
    title = {},
    navigationIcon = {
        AnimatedVisibility(showBack) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    },
    actions = {
        IconButton(onClick = {}) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }
        IconButton(onClick = onMore) {
            Icon(Icons.Filled.MoreVert, contentDescription = "More")
        }
    },
)


@Composable
fun NewsDetailContent(
    content: Model.Content,
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

@Composable
fun NewsHeader(
    modifier: Modifier = Modifier,
    header: Header,
    onLinkClicked: (String, Boolean) -> Unit,
    onUserClicked: (UserId) -> Unit
) {
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
                Spacer(Modifier.height(14.dp))
                CompositionLocalProvider(LocalContentAlpha provides medium) {
                    Row {
                        Surface(color = MaterialTheme.colors.primaryVariant.copy(medium)) {
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
                        CompositionLocalProvider(LocalContentAlpha provides medium) {
                            Icon(Icons.Filled.Comment, null, Modifier.size(16.dp).align(Alignment.CenterVertically))
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
                        CompositionLocalProvider(LocalContentAlpha provides medium) {
                            Icon(Icons.Filled.OpenInNew, null, Modifier.size(16.dp).align(Alignment.CenterVertically))
                        }
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.body1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.clickable { onLinkClicked(it, false) }.align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
                Row {
                    CompositionLocalProvider(LocalContentAlpha provides medium) {
                        Icon(Icons.Filled.OpenInNew, null, Modifier.size(16.dp).align(Alignment.CenterVertically))
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = hnLink,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { onLinkClicked(hnLink, true) }.align(Alignment.CenterVertically)
                    )
                }
                text?.let {
                    Spacer(Modifier.height(16.dp))
                    Surface(color = MaterialTheme.colors.primary.copy(0.2f)) {
                        Text(it, style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}

fun LazyListScope.commentTree(
    comments: List<Comment>,
    padding: Dp = 0.dp,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit,
) {
    comments.forEach { c ->
        commentRow(c, padding, onCommentClicked, onUserClicked, onLinkClicked)
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
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onCommentClicked(comment.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalContentAlpha provides disabled) {
            CommentHeader(comment, onUserClicked) {
                Spacer(Modifier.width(16.dp))
                CompositionLocalProvider(LocalContentAlpha provides high) {
                    Text(
                        text = comment.childrenCount,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
fun CommentHeader(
    comment: Comment.Content,
    onUserClicked: (UserId) -> Unit,
    content: @Composable (RowScope) -> Unit = {}
) {
    Row {
        val color = if (comment.isOp) MaterialTheme.colors.primarySurface else Color.Transparent
        val textColor = if (comment.isOp) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
        val padding = if (comment.isOp) 4.dp else 0.dp
        if (!comment.isOp) {
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
                text = comment.user,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(padding).clickable { onUserClicked(comment.user) }.align(Alignment.CenterVertically),
                color = textColor
            )
        }
        Spacer(Modifier.width(16.dp))
        Icon(Icons.Filled.HourglassBottom, null, Modifier.size(16.dp).align(Alignment.CenterVertically))
        Spacer(Modifier.width(4.dp))
        Text(
            text = comment.time,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        content(this)
    }
}

@Composable
fun CommentExpanded(
    comment: Comment.Content.Expanded,
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
        CompositionLocalProvider(LocalContentAlpha provides medium) {
            CommentHeader(comment, onUserClicked)
        }
        Spacer(Modifier.height(8.dp))
        val urlAnnotation = "url"
        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
        val pressedLink = remember { mutableStateOf<String?>(null) }
        val underline = SpanStyle(color = MaterialTheme.colors.primary, textDecoration = TextDecoration.Underline)
        val emphasis = SpanStyle(fontStyle = FontStyle.Italic)
        val code = SpanStyle(fontFamily = FontFamily.Monospace)
        val background = SpanStyle(background = MaterialTheme.colors.primary.copy(alpha = 0.1f))

        val text = buildAnnotatedString {
            for (t in comment.text) {
                when (t) {
                    is Text.Plain -> append(t.text)
                    is Text.Emphasis -> {
                        pushStyle(emphasis)
                        append(t.text)
                        pop()
                    }
                    is Text.Link -> {
                        pushStyle(underline)
                        if (t.link == pressedLink.value) pushStyle(background)
                        pushStringAnnotation(urlAnnotation, t.link)
                        append(t.text)
                        pop()
                        if (t.link == pressedLink.value) pop()
                        pop()
                    }
                    is Text.Code -> {
                        pushStyle(code)
                        append(t.text)
                        pop()
                    }
                }
            }
        }
        fun Offset.correspondingAnnotation(f: (AnnotatedString.Range<String>) -> Unit) {
            layoutResult.value?.let {
                val position = it.getOffsetForPosition(this)
                text
                    .getStringAnnotations(position, position)
                    .firstOrNull()
                    ?.let(f)
            }
        }
        val handleLinkPress: suspend PressGestureScope.(Offset) -> Unit = { pos ->
            pos.correspondingAnnotation { if (it.tag == urlAnnotation) pressedLink.value = it.item }
        }
        fun handleLinkTap(pos: Offset) { pos.correspondingAnnotation {
            pressedLink.value = null
            if (it.tag == urlAnnotation) onLinkClicked(it.item, false)
        } }
        Text(
            text = text,
            onTextLayout = { layoutResult.value = it },
            style = MaterialTheme.typography.body1,
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = ::handleLinkTap, onPress = handleLinkPress)
            }
        )
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
fun CommentRow(padding: Dp = 0.dp, isSelected: Boolean = false, content: @Composable () -> Unit) {
    Row(modifier = Modifier.height(IntrinsicSize.Max).fillMaxWidth()) {
        CommentPadding(padding)
        Box(Modifier) {
            Column {
                Divider(color = Color.Black.copy(alpha = 0.09f))
                content()
            }
            if (isSelected) Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .fillMaxHeight()
                    .width(4.dp)
            )
        }
    }
}

val step = 16.dp

fun LazyListScope.commentRow(
    comment: Comment,
    padding: Dp,
    onCommentClicked: (ItemId) -> Unit,
    onUserClicked: (UserId) -> Unit,
    onLinkClicked: (String, Boolean) -> Unit,
) {
    val key = when (comment) {
        Comment.Loading -> null
        is Comment.Content.Collapsed -> comment.id
        is Comment.Content.Expanded -> comment.id
    }
    item(key) {
        when (comment) {
            Comment.Loading -> CommentRow(padding) { CommentLoader() }
            is Comment.Content.Collapsed -> CommentRow(padding, comment.isSelected) { CommentCollapsed(comment, onCommentClicked, onUserClicked) }
            is Comment.Content.Expanded -> CommentRow(padding, comment.isSelected) {
                CommentExpanded(comment, onCommentClicked, onUserClicked, onLinkClicked)
            }
        }
    }
    if (comment is Comment.Content.Expanded) commentTree(
        comments = comment.children,
        padding = padding + step,
        onCommentClicked = onCommentClicked,
        onUserClicked = onUserClicked,
        onLinkClicked = onLinkClicked,
    )
}

@Composable
fun NewsComments(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
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
    }
}

