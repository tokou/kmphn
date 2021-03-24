package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.github.tokou.common.detail.NewsDetailStore.*

class NewsDetailStoreProvider(
    private val storeFactory: StoreFactory,
    private val repository: Repository,
    private val id: Long
) {

    fun provide(): NewsDetailStore = object : NewsDetailStore, Store<Intent, State, Label> by storeFactory.create(
        name = "NewsDetailStore",
        initialState = State.Empty,
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private sealed class Result {
        data class Loaded(val item: News) : Result()
        object NotFound : Result()
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.Loaded -> State.Content(result.item)
                Result.NotFound -> State.Error
            }
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Label>() {
        override suspend fun executeAction(action: Unit, getState: () -> State) {
            repository.load(id)?.let { dispatch(Result.Loaded(it)) } ?: dispatch(Result.NotFound)
        }
    }

    interface Repository {
        suspend fun load(id: Long): News?
    }
}
