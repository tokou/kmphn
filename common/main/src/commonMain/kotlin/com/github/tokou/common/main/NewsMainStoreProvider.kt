package com.github.tokou.common.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.github.tokou.common.main.NewsMainStore.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.subscribe
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
        executorFactory = this::createExecutor,
        reducer = ReducerImpl
    ) {}

    private fun createExecutor(): ExecutorImpl { return ExecutorImpl(repository) }

    interface Repository {
        val updates: Flow<Result<List<News>>>
        suspend fun load(loaded: Set<ItemId>, loadCount: Int)
    }
}

internal sealed class Outcome {
    data class Loaded(val news: List<News>, val askedCount: Int) : Outcome()
    data class Loading(val keepContent: Boolean) : Outcome()
    object NotFound : Outcome() { override fun toString(): String = this::class.simpleName ?: super.toString() }
    object LoadingMore : Outcome() { override fun toString(): String = this::class.simpleName ?: super.toString() }
    object LoadedMore : Outcome() { override fun toString(): String = this::class.simpleName ?: super.toString() }
}

internal object ReducerImpl : Reducer<State, Outcome> {
    override fun State.reduce(result: Outcome): State =
        when (this) {
            is State.Content -> when (result) {
                is Outcome.Loaded -> updateContent(result.news)
                Outcome.NotFound -> State.Error
                is Outcome.Loading -> if (result.keepContent) copy(isRefreshing = true) else State.Loading
                Outcome.LoadingMore -> copy(isLoadingMore = true)
                Outcome.LoadedMore -> copy(isLoadingMore = false)
            }
            else -> when (result) {
                is Outcome.Loaded -> State.Content(news = result.news)
                is Outcome.Loading -> State.Loading
                else -> State.Error
            }
        }

    private fun State.Content.updateContent(result: List<News>): State.Content {
        val oldIds = news.map { it.id }
        val newIds = result.map { it.id }
        val diffIds = newIds - oldIds
        val updated = news.map { o ->
            if (o.id in newIds) result.find { it.id == o.id }!!
            else o
        }
        val loaded = result.filter { it.id in diffIds }
        return copy(
            news = updated + loaded,
            canLoadMore = newIds.isNotEmpty(),
            isRefreshing = false,
        )
    }
}

internal class ExecutorImpl(
    private val repository: NewsMainStoreProvider.Repository,
    private val pageSize: Int = 30
) : SuspendExecutor<Intent, Unit, State, Outcome, Label>() {

    override suspend fun executeAction(action: Unit, getState: () -> State) = coroutineScope {
        repository.updates.subscribe(this, ::dispatch) { r -> r
            .map { Outcome.Loaded(it, pageSize) }
            .recover { Outcome.NotFound } // TODO: better error handling
            .getOrThrow()
        }
        repository.load(emptySet(), pageSize)
    }

    override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
        is Intent.Refresh -> refresh(keepContent = intent.keepContent, state = getState())
        Intent.LoadMore -> loadMore(state = getState())
    }

    private suspend fun loadMore(state: State) = state.contentCase {
        dispatch(Outcome.LoadingMore)
        repository.load(news.map { it.id }.toSet(), pageSize)
        dispatch(Outcome.LoadedMore)
    }

    private suspend fun refresh(keepContent: Boolean, state: State) = state.contentCase {
        dispatch(Outcome.Loading(keepContent))
        repository.load(emptySet(), pageSize)
    }

    private suspend fun State.contentCase(f: suspend State.Content.() -> Unit) = when (this) {
        is State.Content -> f(this)
        else -> {}
    }
}
