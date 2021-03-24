package com.github.tokou.common.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.github.tokou.common.detail.NewsDetailStore.*
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.Timestamp
import com.github.tokou.common.utils.UserId

interface NewsDetailStore : Store<Intent, State, Label> {

    sealed class Intent

    sealed class State {
        object Error : State()
        object Loading : State()
        object Empty : State()
        data class Content(val news: News) : State()
    }

    data class News(
        val id: ItemId,
        val title: String?,
        val link: String?,
        val user: UserId?,
        val time: Timestamp,
        val comments: List<Comment>,
        val points: Long,
        val descendants: Long,
    )

    sealed class Comment {
        abstract val id: Long

        data class Loading(override val id: Long) : Comment()
        data class Content(
            override val id: Long,
            val user: UserId,
            val time: Timestamp,
            val text: String,
            val comments: List<Comment>
        ) : Comment()
    }

    sealed class Label
}
