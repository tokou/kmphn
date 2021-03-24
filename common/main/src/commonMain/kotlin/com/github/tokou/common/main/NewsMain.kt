package com.github.tokou.common.main

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.reduce
import com.github.tokou.common.main.NewsMain.*
import com.github.tokou.common.utils.ComponentContext

interface NewsMain {

    val models: Value<Model>

    data class Model(
        val news: List<News>
    )

    sealed class Output {
        data class Selected(val id: Long) : Output()
    }

    fun onNewsSelected(id: Long)
    fun onNewsSecondarySelected(id: Long)
    fun onLoadMoreSelected()
    fun onRefresh()

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

class NewsMainComponent(
    componentContext: ComponentContext,
    private val onOutput: (Output) -> Unit
): NewsMain, ComponentContext by componentContext {

    private val _models = MutableValue(Model(news))
    override val models: Value<Model> = _models

    override fun onNewsSelected(id: Long) {
        _models.reduce { it.copy(news = listOf(news.first().copy(id = 36565113, title = "New item")) + it.news) }
    }

    override fun onNewsSecondarySelected(id: Long) {
        onOutput(Output.Selected(id))
    }

    override fun onLoadMoreSelected() {
    }

    override fun onRefresh() {
        _models.value = Model(news)
    }
}

val news = listOf(
    News(
        id = 26539673,
        title = "Firefox 87 trims HTTP Referrers by default to protect user privacy",
        link = "https://blog.mozilla.org/security/2021/03/22/firefox-87-trims-http-referrers-by-default-to-protect-user-privacy/",
        user = "twapi",
        time = "4 hrs",
        comments = "137",
        points = "542"
    ),
    News(
        id = 26540007,
        title = "The S in IoT is for Security",
        link = "https://puri.sm/posts/the-s-in-iot-is-for-security/",
        user = "rauhl",
        time = "4 hrs",
        comments = "136",
        points = "247"
    ),
    News(
        id = 26538005,
        title = "Live feed from Iceland erupting volcano [video]",
        link = "https://www.ruv.is/frett/2021/03/20/live-feed-from-iceland-volcano",
        user = "lknik",
        time = "8 hrs",
        comments = "118",
        points = "444"
    ),
    News(
        id = 26540692,
        title = "Launch HN: Kitemaker (YC W21) – Fast alternative to Jira, built for remote teams",
        user = "kevsim",
        time = "3 hrs",
        comments = "48",
        points = "105"
    ),
    News(
        id = 26540737,
        title = "Red seaweed supplementation reduces enteric methane by over 80% in beef steers",
        link = "https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0247820",
        user = "giuliomagnifico",
        time = "3 hrs",
        comments = "196",
        points = "139"
    ),
    News(
        id = 26542129,
        title = "Pulling Bits from ROM Silicon Die Images: Unknown Architecture",
        link = "https://ryancor.medium.com/pulling-bits-from-rom-silicon-die-images-unknown-architecture-b73b6b0d4e5d",
        user = "mariuz",
        time = "1 hr",
        comments = "1",
        points = "13"
    ),
)

