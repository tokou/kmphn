package com.github.tokou.common.detail

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.database.NewsDatabase
import com.github.tokou.common.detail.NewsDetail.*
import com.github.tokou.common.utils.ComponentContext
import com.github.tokou.common.utils.asValue
import com.github.tokou.common.utils.getStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

interface NewsDetail {

    val models: Value<Model>

    data class Model(
        val news: News? = null,
        val maxitem: Long = 0
    )

    fun onBack()

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
