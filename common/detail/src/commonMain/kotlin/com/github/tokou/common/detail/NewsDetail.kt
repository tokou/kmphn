package com.github.tokou.common.detail

import kotlinx.coroutines.flow.Flow

interface NewsDetail {

    val models: Flow<Model>

    fun onBack()

    sealed class Output {
        object Back : Output()
    }

    sealed class Model {
        object Empty : Model()
        object Loading : Model()
        data class Content(val header: Header, val comments: List<Comment>) : Model()
    }

    data class Header(
        val id: Long,
        val title: String,
        val link: String? = null,
        val user: String,
        val time: String,
        val commentsCount: String,
        val points: String,
    ) {
        val hnLink: String get() = "https://news.ycombinator.com/item?id=$id"
    }

    sealed class Comment {
        abstract val id: Long

        data class Loading(override val id: Long) : Comment()

        sealed class Content : Comment() {
            abstract val user: String
            abstract val time: String
            abstract val isOp: Boolean

            data class Expanded(
                override val id: Long,
                override val user: String,
                override val time: String,
                override val isOp: Boolean,
                val children: List<Comment>,
                val text: String
            ) : Content()

            data class Collapsed(
                override val id: Long,
                override val user: String,
                override val time: String,
                override val isOp: Boolean,
                val childrenCount: String
            ) : Content()
        }
    }
}
