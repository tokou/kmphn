package com.github.tokou.common.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.github.tokou.common.main.NewsMainStore.*
import com.github.tokou.common.utils.ItemId
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NewsMainStoreProvider(
    private val storeFactory: StoreFactory,
    private val repository: Repository,
) {

    fun provide(): NewsMainStore = object : NewsMainStore, Store<Intent, State, Label> by storeFactory.create(
        name = "NewsMainStore",
        initialState = State.Loading,
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = ::ExecutorImpl,
        reducer = ReducerImpl
    ) {}

    private sealed class Result {
        data class Loaded(val news: List<News>, val askedCount: Int) : Result()
        object NotFound : Result() { override fun toString(): String = this::class.simpleName ?: super.toString() }
        object Loading : Result() { override fun toString(): String = this::class.simpleName ?: super.toString() }
        object LoadingMore : Result() { override fun toString(): String = this::class.simpleName ?: super.toString() }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (this) {
                is State.Content -> when (result) {
                    is Result.Loaded -> copy(
                        news = news + result.news,
                        isLoadingMore = false,
                        canLoadMore = result.news.size == result.askedCount
                    )
                    Result.NotFound -> State.Error
                    Result.Loading -> State.Loading
                    Result.LoadingMore -> copy(isLoadingMore = true)
                }
                else -> when (result) {
                    is Result.Loaded -> State.Content(news = result.news)
                    Result.NotFound -> State.Error
                    Result.Loading -> State.Loading
                    Result.LoadingMore -> State.Loading
                }
            }
    }

    object NoContent : Error()

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Label>() {

        private val pageSize = 30

        suspend fun load() = coroutineScope {
            repository
                .updates
                .map { r -> r
                    .map { Result.Loaded(it, pageSize) }
                    .recover { if (it == NoContent) Result.Loading else Result.NotFound }
                    .getOrThrow()
                }
                .flowOn(Dispatchers.Default)
                .onEach(::dispatch)
                .onStart { dispatch(Result.Loading) }
                .launchIn(this)
            repository.load(emptySet(), pageSize)
        }

        override suspend fun executeAction(action: Unit, getState: () -> State) {
            load()
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
            Intent.Refresh -> refresh(getState())
            Intent.LoadMore -> loadMore(getState())
        }

        private suspend fun loadMore(state: State) = state.contentCase {
            dispatch(Result.LoadingMore)
            repository.load(news.map { it.id }.toSet(), pageSize)
        }

        private suspend fun refresh(state: State) {
            dispatch(Result.Loading)
            repository.load(emptySet(), pageSize)
        }

        private suspend fun State.contentCase(f: suspend State.Content.() -> Unit) = when (this) {
            is State.Content -> f(this)
            else -> {}
        }
    }

    interface Repository {
        val updates: Flow<kotlin.Result<List<News>>>
        suspend fun load(loaded: Set<ItemId>, loadCount: Int)
    }
}
