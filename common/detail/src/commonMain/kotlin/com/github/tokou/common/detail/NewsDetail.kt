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

class NewsDetailComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    database: NewsDatabase,
    itemId: Long,
    private val onOutput: (Output) -> Unit
): NewsDetail, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            NewsDetailStoreProvider(
                storeFactory = storeFactory,
                repository = NewsDetailRepository(database, NewsApi),
                id = itemId
            ).provide()
        }

    override val models: Value<Model> = store
        .asValue(this + Dispatchers.Main)
        .map(stateToModel)
        .map { it.copy(maxitem = itemId) }

    override fun onBack() {
        onOutput(Output.Back)
    }
}

internal val stateToModel: (NewsDetailStore.State) -> Model = { when (it) {
    is NewsDetailStore.State.Content -> Model(it.news)
    else -> Model()
} }
