package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.github.tokou.common.detail.NewsDetailStore.*

internal interface NewsDetailStore : Store<Intent, State, Label> {

    sealed class Intent

    sealed class State {
        object Error : State()
        object Loading : State()
        object Empty : State()
        data class Content(val news: NewsDetail.News) : State()
    }

    sealed class Label
}
