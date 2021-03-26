package com.github.tokou.common.ui.detail

import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import com.github.tokou.common.detail.NewsDetail

@Composable
fun RichText(
    modifier: Modifier = Modifier,
    text: List<NewsDetail.Text>,
    onLinkClicked: (String, Boolean) -> Unit
) {
    val urlAnnotation = "url"
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val pressedLink = remember { mutableStateOf<String?>(null) }
    val underline = SpanStyle(color = MaterialTheme.colors.primary, textDecoration = TextDecoration.Underline)
    val emphasis = SpanStyle(fontStyle = FontStyle.Italic)
    val code = SpanStyle(fontFamily = FontFamily.Monospace)
    val background = SpanStyle(background = MaterialTheme.colors.primary.copy(alpha = 0.1f))

    val annotatedString = buildAnnotatedString {
        for (t in text) {
            when (t) {
                is NewsDetail.Text.Plain -> append(t.text)
                is NewsDetail.Text.Emphasis -> {
                    pushStyle(emphasis)
                    append(t.text)
                    pop()
                }
                is NewsDetail.Text.Link -> {
                    pushStyle(underline)
                    if (t.link == pressedLink.value) pushStyle(background)
                    pushStringAnnotation(urlAnnotation, t.link)
                    append(t.text)
                    pop()
                    if (t.link == pressedLink.value) pop()
                    pop()
                }
                is NewsDetail.Text.Code -> {
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
            annotatedString
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
        text = annotatedString,
        onTextLayout = { layoutResult.value = it },
        style = MaterialTheme.typography.body1,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = ::handleLinkTap, onPress = handleLinkPress)
        }
    )
}
