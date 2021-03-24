package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor

internal class NewsDetailStoreProvider(
    private val storeFactory: StoreFactory,
    private val repository: Repository,
    private val id: Long
) {

    fun provide(): NewsDetailStore = object : NewsDetailStore, Store<NewsDetailStore.Intent, NewsDetailStore.State, NewsDetailStore.Label> by storeFactory.create(
        name = "NewsDetailStore",
        initialState = NewsDetailStore.State.Empty,
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private sealed class Result {
        data class Loaded(val item: NewsDetail.News) : Result()
        object NotFound : Result()
    }

    private object ReducerImpl : Reducer<NewsDetailStore.State, Result> {
        override fun NewsDetailStore.State.reduce(result: Result): NewsDetailStore.State =
            when (result) {
                is Result.Loaded -> NewsDetailStore.State.Content(result.item)
                Result.NotFound -> NewsDetailStore.State.Error
            }
    }

    private inner class ExecutorImpl : SuspendExecutor<NewsDetailStore.Intent, Unit, NewsDetailStore.State, Result, NewsDetailStore.Label>() {
        override suspend fun executeAction(action: Unit, getState: () -> NewsDetailStore.State) {
            repository.load(id)?.let { dispatch(Result.Loaded(it)) } ?: dispatch(Result.NotFound)
        }
    }

    interface Repository {
        suspend fun load(id: Long): NewsDetail.News?
    }
}
