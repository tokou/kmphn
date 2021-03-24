package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.Item
import com.github.tokou.common.database.NewsDatabase
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
