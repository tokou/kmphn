package com.github.tokou.common.main

import kotlinx.coroutines.flow.Flow

interface NewsMain {

    val models: Flow<Model>

    fun onNewsSelected(id: Long)
    fun onNewsSecondarySelected(id: Long)
    fun onLoadMoreSelected()
    suspend fun onRefresh()

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }

    sealed class Model {
        object Error : Model()
        object Loading : Model()
        data class Content(val news: List<News>) : Model()
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
