package com.github.tokou.common.detail

import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.lifecycle.doOnCreate
import com.arkivanov.decompose.value.reduce
import com.github.tokou.common.api.NewsApi
import com.github.tokou.common.detail.NewsDetail.*
import com.github.tokou.common.utils.ComponentContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface NewsDetail {

    val models: Value<Model>

    data class Model(
        val news: News,
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

val news = News(
    id = 1,
    title = "Firefox 87 trims HTTP Referrers by default to protect user privacy",
    link = "https://blog.mozilla.org/security/2021/03/22/firefox-87-trims-http-referrers-by-default-to-protect-user-privacy/",
    user = "twapi",
    time = "4 hrs",
    comments = "137",
    points = "542"
)

class NewsDetailComponent(
    componentContext: ComponentContext,
    private val onOutput: (Output) -> Unit
): NewsDetail, ComponentContext by componentContext {

    private val _models = MutableValue(Model(news))
    override val models: Value<Model> = _models

    init {
        lifecycle.doOnCreate {
            launch {
                val id = NewsApi.fetchMaxItemId()
                val item = NewsApi.fetchItem(id)
                println(item)
                _models.reduce { it.copy(maxitem = id) }
            }
        }
    }

    override fun onBack() {
        onOutput(Output.Back)
    }
}
