package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.github.tokou.common.detail.NewsDetailStore.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.Timestamp
import com.github.tokou.common.utils.UserId

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
