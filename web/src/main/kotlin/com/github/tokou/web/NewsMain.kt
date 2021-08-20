package com.github.tokou.web

import com.github.tokou.common.main.NewsMain
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.State
import react.dom.*

class NewsMainR(props: Props<NewsMain>) : RenderableComponent<NewsMain, NewsMainR.NewsMainState>(
    props = props,
    initialState = NewsMainState(props.component.models.value)
) {
    init {
        component.models.bindToState { model = it }
    }

    override fun RBuilder.render() {
        when (val m = state.model) {
            NewsMain.Model.Loading -> loader()
            NewsMain.Model.Error -> error()
            is NewsMain.Model.Content -> ul {
                for (item in m.items) {
                    li {
                        p {
                            +"["
                            a("/item?id=${item.id}") {
                                attrs {
                                    onClickFunction = {
                                        it.preventDefault()
                                        component.onNewsSecondarySelected(item.id)
                                    }
                                }
                                +"${item.id}"
                            }
                            +"] ${item.title}"
                        }
                        p { +"${item.user} — ${item.time}" }
                        p { +"${item.comments} comments ${item.points} points" }
                        a(item.link) {
                            attrs {
                                onClickFunction = {
                                    it.preventDefault()
                                    component.onNewsSelected(item.id, item.link)
                                }
                            }
                            +"${item.link}"
                        }
                    }
                }
                if (m.canLoadMore) {
                    li {
                        if (m.isLoadingMore) +"Loading..."
                        else a("/") {
                            attrs {
                                onClickFunction = {
                                    it.preventDefault()
                                    component.onLoadMoreSelected()
                                }
                            }
                            +"Load More"
                        }
                    }
                }
            }
        }
    }

    class NewsMainState(
        var model: NewsMain.Model
    ) : State
}

fun RBuilder.loader() = div { +"Loading..." }
fun RBuilder.error() = div { +"Error!" }
