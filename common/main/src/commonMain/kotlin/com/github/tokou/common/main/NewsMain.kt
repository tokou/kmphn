package com.github.tokou.common.main

import com.github.tokou.common.utils.ItemId
import kotlinx.coroutines.flow.Flow

interface NewsMain {

    val models: Flow<Model>

    fun onNewsSelected(id: ItemId, link: String?)
    fun onNewsSecondarySelected(id: ItemId)
    fun onLoadMoreSelected()
    fun onRefresh(fromPull: Boolean = false)

    sealed class Output {
        data class Selected(val id: ItemId) : Output()
        data class Link(val uri: String) : Output()
    }

    sealed class Model {
        object Error : Model()
        object Loading : Model()
        data class Content(
            val items: List<Item>,
            val isLoadingMore: Boolean,
            val isRefreshing: Boolean,
            val canLoadMore: Boolean
        ) : Model()
    }

    data class Item(
        val id: ItemId,
        val title: String,
        val link: String? = null,
        val user: String,
        val time: String,
        val comments: String,
        val points: String,
    )
}
