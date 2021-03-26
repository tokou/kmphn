package com.github.tokou.common.main

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NewsMainComponent(
    componentContext: ComponentContext,
    private val onOutput: (NewsMain.Output) -> Unit
): NewsMain, ComponentContext by componentContext {

    private val _models = MutableStateFlow<NewsMain.Model>(NewsMain.Model.Content(news))
    override val models: Flow<NewsMain.Model> = _models

    override fun onNewsSelected(id: Long) {
        val value = _models.value
        _models.value = when (value) {
            is NewsMain.Model.Content -> value.copy(news = listOf(news.first().copy(id = 36565113, title = "New item")) + value.news)
            else -> NewsMain.Model.Error
        }
    }

    override fun onNewsSecondarySelected(id: Long) {
        onOutput(NewsMain.Output.Selected(id))
    }

    override fun onLoadMoreSelected() {
    }

    override suspend fun onRefresh() {
        _models.value = NewsMain.Model.Loading
        delay(3000)
        _models.value = NewsMain.Model.Content(news)
    }
}

val news = listOf(
    NewsMain.News(
        id = 26539673,
        title = "Firefox 87 trims HTTP Referrers by default to protect user privacy",
        link = "https://blog.mozilla.org/security/2021/03/22/firefox-87-trims-http-referrers-by-default-to-protect-user-privacy/",
        user = "twapi",
        time = "4 hrs",
        comments = "137",
        points = "542"
    ),
    NewsMain.News(
        id = 26540007,
        title = "The S in IoT is for Security",
        link = "https://puri.sm/posts/the-s-in-iot-is-for-security/",
        user = "rauhl",
        time = "4 hrs",
        comments = "136",
        points = "247"
    ),
    NewsMain.News(
        id = 26538005,
        title = "Live feed from Iceland erupting volcano [video]",
        link = "https://www.ruv.is/frett/2021/03/20/live-feed-from-iceland-volcano",
        user = "lknik",
        time = "8 hrs",
        comments = "118",
        points = "444"
    ),
    NewsMain.News(
        id = 26540692,
        title = "Launch HN: Kitemaker (YC W21) – Fast alternative to Jira, built for remote teams",
        user = "kevsim",
        time = "3 hrs",
        comments = "48",
        points = "105"
    ),
    NewsMain.News(
        id = 26540737,
        title = "Red seaweed supplementation reduces enteric methane by over 80% in beef steers",
        link = "https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0247820",
        user = "giuliomagnifico",
        time = "3 hrs",
        comments = "196",
        points = "139"
    ),
    NewsMain.News(
        id = 26542129,
        title = "Pulling Bits from ROM Silicon Die Images: Unknown Architecture",
        link = "https://ryancor.medium.com/pulling-bits-from-rom-silicon-die-images-unknown-architecture-b73b6b0d4e5d",
        user = "mariuz",
        time = "1 hr",
        comments = "1",
        points = "13"
    ),
)
