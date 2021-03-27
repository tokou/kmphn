package com.github.tokou.common.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

fun <T, K> slideIn(): @Composable (currentChild: T, currentKey: K, children: @Composable (T, K) -> Unit) -> Unit =
    { currentChild: T, currentKey: K, children: @Composable (T, K) -> Unit ->
        KeyedSlideIn(currentChild, currentKey, children)
    }

@Composable
private fun <T, K> KeyedSlideIn(currentChild: T, currentKey: K, children: @Composable (T, K) -> Unit) {
    SlideIn(ChildWrapper(currentChild, currentKey)) {
        children(it.child, it.key)
    }
}

private data class ChildWrapper<out T, out C>(val child: T, val key: C) {
    override fun equals(other: Any?): Boolean = key == (other as? ChildWrapper<*, *>)?.key
    override fun hashCode(): Int = key.hashCode()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> SlideIn(
    targetState: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val duration = DefaultDurationMillis
    val easing = FastOutSlowInEasing
    val alpha = 0.4f
    val tween = tween<IntOffset>(durationMillis = duration, easing = easing)
    val tweenFloat = tween<Float>(durationMillis = duration, easing = easing)
    val slideIn = slideInHorizontally(initialOffsetX = { o -> o }, animationSpec = tween)
    val slideOut = slideOutHorizontally(targetOffsetX = { o -> o }, animationSpec = tween)
    val fadeIn = fadeIn(initialAlpha = alpha, animationSpec = tweenFloat)
    val fadeOut = fadeOut(targetAlpha = alpha, animationSpec = tweenFloat)

    val backStack = remember { mutableStateListOf<T>() }
    val deStacked = remember { mutableStateListOf<T>() }

    if (backStack.contains(targetState)) {
        val elements = backStack.subList(backStack.lastIndexOf(targetState) + 1, backStack.size).filterNot { it in deStacked }
        if (targetState in deStacked) deStacked.remove(targetState)
        if (elements.isNotEmpty()) {
            backStack.removeAll(deStacked)
            deStacked.addAll(elements)
        }
    } else {
        if (deStacked.isNotEmpty()) backStack.removeAll(deStacked)
        backStack.add(targetState)
        if (deStacked.isNotEmpty()) deStacked.clear()
    }

    Box(modifier) {
        backStack.forEach {
            key(it) {
                val isOld = deStacked.contains(it)
                val isLast = backStack.last() == it
                val shouldSlideIn = isLast && backStack.size > 1
                val shouldSlideOut = isOld || isLast
                val enter = if (shouldSlideIn) slideIn else fadeIn
                val exit = if (shouldSlideOut) slideOut else fadeOut
                val isTarget = it == targetState
                AnimatedVisibility(
                    visible = isTarget,
                    enter = enter,
                    exit = exit,
                    initiallyVisible = false
                ) {
                    content(it)
                }
            }
        }
    }
}
