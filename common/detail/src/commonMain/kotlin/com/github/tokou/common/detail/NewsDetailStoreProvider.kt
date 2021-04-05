package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.github.tokou.common.detail.NewsDetailStore.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.logger
import com.github.tokou.common.utils.subscribe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class NewsDetailStoreProvider(
    private val storeFactory: StoreFactory,
    private val repository: Repository,
    private val id: ItemId
) {

    fun provide(): NewsDetailStore = object : NewsDetailStore, Store<Intent, State, Label> by storeFactory.create(
        name = "NewsDetailStore",
        initialState = State.Loading,
        bootstrapper = SimpleBootstrapper(Unit),
        executorFactory = this::createExecutor,
        reducer = ReducerImpl
    ) {}

    private fun createExecutor(): ExecutorImpl { return ExecutorImpl(repository, id) }

    interface Repository {
        val updates: Flow<Result<News>>
        suspend fun load(id: Long)
    }
}

internal sealed class Outcome {
    data class Loaded(val item: News) : Outcome()
    data class Toggled(val itemId: ItemId, val collapsedComments: Set<ItemId>) : Outcome()
    object NotFound : Outcome() { override fun toString(): String = this::class.simpleName ?: super.toString() }
    object Loading : Outcome() { override fun toString(): String = this::class.simpleName ?: super.toString() }
}

internal object ReducerImpl : Reducer<State, Outcome> {
    override fun State.reduce(result: Outcome): State = when (this) {
        is State.Content -> when (result) {
            is Outcome.Loaded -> copy(news = result.item)
            is Outcome.Toggled -> copy(collapsedComments = result.collapsedComments, selectedComment = result.itemId)
            Outcome.NotFound -> State.Error
            Outcome.Loading -> State.Loading
        }
        else -> when (result) {
            is Outcome.Loaded -> State.Content(news = result.item)
            is Outcome.Toggled -> State.Error
            Outcome.NotFound -> State.Error
            Outcome.Loading -> State.Loading
        }
    }
}

@OptIn(FlowPreview::class)
internal class ExecutorImpl(
    private val repository: NewsDetailStoreProvider.Repository,
    private val id: ItemId
) : SuspendExecutor<Intent, Unit, State, Outcome, Label>() {

    override suspend fun executeAction(action: Unit, getState: () -> State) = coroutineScope {
        repository.updates
            .sample(100)
            .subscribe(this, ::dispatch) { r -> r
                .map { Outcome.Loaded(it) }
                .recover { Outcome.NotFound } // TODO: better error handling
                .getOrThrow()
            }
        repository.load(id)
    }

    override suspend fun executeIntent(intent: Intent, getState: () -> State) = when (intent) {
        is Intent.ToggleComment -> toggleComment(intent.id, getState())
        Intent.Refresh -> refresh(getState())
    }

    private suspend fun refresh(state: State) {
        dispatch(Outcome.Loading)
        repository.load(id)
    }

    private fun toggleComment(id: ItemId, state: State) = state.contentCase {
        val collapsed = collapsedComments
        val toggled = if (collapsed.contains(id)) collapsed - id else collapsed + id
        dispatch(Outcome.Toggled(id, toggled))
    }

    private fun State.contentCase(f: State.Content.() -> Unit) = when (this) {
        is State.Content -> f(this)
        else -> {}
    }
}

