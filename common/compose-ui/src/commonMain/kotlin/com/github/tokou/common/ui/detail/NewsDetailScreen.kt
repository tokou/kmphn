package com.github.tokou.common.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.asState
import com.github.tokou.common.detail.NewsDetail
import com.github.tokou.common.detail.NewsDetail.Model

@Composable
fun NewsDetailScreen(modifier: Modifier = Modifier, component: NewsDetail) {
    val model by component.models.asState()

    NewsDetailScaffold(
        modifier = modifier,
        onBack = component::onBack,
    ) {
        when (val m = model) {
            Model.Loading -> Loading()
            Model.Error -> Error(component::onRetry)
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
private fun Loading() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun Error(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text("Error", style = MaterialTheme.typography.h6)
            Button(onClick = onRetry) {
                Text("Retry", style = MaterialTheme.typography.button)
            }
        }
    }
}
