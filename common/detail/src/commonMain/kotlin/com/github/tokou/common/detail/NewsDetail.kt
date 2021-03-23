package com.github.tokou.common.detail

import com.arkivanov.decompose.value.Value

interface NewsDetail {

    val models: Value<Model>

    data class Model(
        val news: News
    )

    sealed class Output {
        object Back : Output()
    }

    data class News(
        val id: Long,
        val title: String,
        val link: String? = null,
        val user: String,
        val time: String,
        val comments: String,
        val points: String,
    )
}
