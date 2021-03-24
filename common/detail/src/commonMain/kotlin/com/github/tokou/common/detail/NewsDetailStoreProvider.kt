package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.github.tokou.common.detail.NewsDetailStore.*
import com.github.tokou.common.utils.ItemId
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
        data class Toggled(val collapsedComments: Set<ItemId>) : Result()
        object NotFound : Result()
        object Loading : Result()
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (this) {
                is State.Content -> when (result) {
                    is Result.Loaded -> copy(news = result.item)
                    is Result.Toggled -> copy(collapsedComments = result.collapsedComments)
                    Result.NotFound -> State.Error
                    Result.Loading -> State.Loading
                }
                else -> when (result) {
                    is Result.Loaded -> State.Content(news = result.item)
                    is Result.Toggled -> State.Error
                    Result.NotFound -> State.Error
                    Result.Loading -> State.Loading
                }
            }
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Label>() {
        override suspend fun executeAction(action: Unit, getState: () -> State) = coroutineScope {
            repository
                .updates
                .onStart { Result.Loading }
                .map(Result::Loaded)
                .flowOn(Dispatchers.Default)
                .onEach(::dispatch)
                .launchIn(this)
            repository.load(id)
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            is Intent.ToggleComment -> toggleComment(intent.id, getState())
        }

        private fun toggleComment(id: ItemId, state: State) = when (state) {
            is State.Content -> {
                val collapsed = state.collapsedComments
                val toggled = if (collapsed.contains(id)) collapsed - id else collapsed + id
                dispatch(Result.Toggled(toggled))
            }
            else -> {}
        }
    }

    interface Repository {
        val updates: Flow<News>
        suspend fun load(id: Long)
    }
}
