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
        initialState = State.Loading,
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private sealed class Result {
        data class Loaded(val item: News) : Result()
        data class Toggled(val itemId: ItemId, val collapsedComments: Set<ItemId>) : Result()
        object NotFound : Result() { override fun toString(): String = this::class.simpleName ?: super.toString() }
        object Loading : Result() { override fun toString(): String = this::class.simpleName ?: super.toString() }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (this) {
                is State.Content -> when (result) {
                    is Result.Loaded -> copy(news = result.item)
                    is Result.Toggled -> copy(collapsedComments = result.collapsedComments, selectedComment = result.itemId)
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

    object NoContent : Error()

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Label>() {

        suspend fun load() = coroutineScope {
            repository
                .updates
                .conflate()
                .map { r -> r
                    .map { Result.Loaded(it) }
                    .recover { if (it == NoContent) Result.Loading else Result.NotFound }
                    .getOrThrow()
                }
                .flowOn(Dispatchers.Default)
                .onEach(::dispatch)
                .onStart { dispatch(Result.Loading) }
                .launchIn(this)
            repository.load(id)
        }

        override suspend fun executeAction(action: Unit, getState: () -> State) {
            load()
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            is Intent.ToggleComment -> toggleComment(intent.id, getState())
            Intent.Refresh -> refresh(getState())
        }

        private suspend fun refresh(state: State) {
            load()
        }

        private fun toggleComment(id: ItemId, state: State) = state.contentCase {
            val collapsed = collapsedComments
            val toggled = if (collapsed.contains(id)) collapsed - id else collapsed + id
            dispatch(Result.Toggled(id, toggled))
        }

        private fun State.contentCase(f: State.Content.() -> Unit) = when (this) {
            is State.Content -> f(this)
            else -> {}
        }
    }

    interface Repository {
        val updates: Flow<kotlin.Result<News>>
        suspend fun load(id: Long)
    }
}
