package com.github.tokou.common.detail

import com.arkivanov.decompose.value.Value
import com.github.tokou.common.utils.ItemId
import com.github.tokou.common.utils.UserId

interface NewsDetail {

    val models: Value<Model>

    fun onCommentClicked(id: ItemId)
    fun onUserClicked(id: UserId)
    fun onLinkClicked(uri: String, forceExternal: Boolean = false)
    fun onBack()
    fun onRetry()

    sealed class Output {
        object Back : Output()
        data class Item(val id: ItemId) : Output()
        data class Link(val uri: String) : Output()
    }

    sealed class Model {
        object Error : Model()
        object Loading : Model()
        data class Content(val header: Header, val comments: List<Comment>) : Model()
    }

    data class Header(
        val id: Long,
        val title: String? = null,
        val link: String? = null,
        val user: String,
        val time: String,
        val commentsCount: String? = null,
        val points: String? = null,
        val text: List<Text>? = null,
    ) {
        val hnLink: String get() = "https://news.ycombinator.com/item?id=$id"
    }

    sealed class Comment {

        object Loading : Comment()

        sealed class Content : Comment() {
            abstract val id: Long
            abstract val user: String
            abstract val time: String
            abstract val isOp: Boolean
            abstract val isSelected: Boolean

            data class Expanded(
                override val id: Long,
                override val user: String,
                override val time: String,
                override val isOp: Boolean,
                override val isSelected: Boolean,
                val children: List<Comment>,
                val text: List<Text>
            ) : Content()

            data class Collapsed(
                override val id: Long,
                override val user: String,
                override val time: String,
                override val isOp: Boolean,
                override val isSelected: Boolean,
                val childrenCount: String
            ) : Content()
        }
    }

    sealed class Text {
        abstract val text: String
        data class Plain(override val text: String) : Text()
        data class Emphasis(override val text: String) : Text()
        data class Link(override val text: String, val link: String) : Text()
        data class Code(override val text: String) : Text()
    }
}
