package com.github.tokou.common.main

import com.arkivanov.mvikotlin.core.store.Store
import com.github.tokou.common.main.NewsMainStore.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId
import kotlinx.datetime.Instant

interface NewsMainStore : Store<Intent, State, Label> {

    sealed class Intent {
        object Refresh : Intent() { override fun toString(): String = this::class.simpleName ?: super.toString() }
        object LoadMore : Intent() { override fun toString(): String = this::class.simpleName ?: super.toString() }
    }

    sealed class State {
        object Error : State() { override fun toString(): String = this::class.simpleName ?: super.toString() }
        object Loading : State() { override fun toString(): String = this::class.simpleName ?: super.toString() }
        data class Content(
            val news: List<News>,
            val isLoadingMore: Boolean = false,
        ) : State()
    }

    data class News(
        val id: ItemId,
        val title: String?,
        val link: String?,
        val user: UserId?,
        val time: Instant,
        val points: Long,
        val descendants: Long,
    )

    sealed class Label
}
