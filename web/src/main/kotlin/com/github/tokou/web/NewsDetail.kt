package com.github.tokou.web

import com.github.tokou.common.detail.NewsDetail
import kotlinx.html.UL
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.State
import react.dom.*

class NewsDetailR(props: Props<NewsDetail>) : RenderableComponent<NewsDetail, NewsDetailR.NewsDetailState>(
    props = props,
    initialState = NewsDetailState(props.component.models.value)
) {

    init {
        component.models.bindToState { model = it }
    }

    override fun RBuilder.render() {
        when (val m = state.model) {
            NewsDetail.Model.Loading -> loader()
            NewsDetail.Model.Error -> div { +"Error!" }
            is NewsDetail.Model.Content -> {
                div { with(m.header) {
                    p { a("/") {
                        attrs {
                            onClickFunction = {
                                it.preventDefault()
                                component.onBack()
                            }
                        }
                        +"←"
                    } }
                    title?.let { p { +it } }
                    p {
                        points?.let { +"$it points / " }
                        +"$user — $time"
                        commentsCount?.let { +" / $it comments" }
                        link?.let { l -> p { a(l) {
                            attrs {
                                onClickFunction = {
                                    it.preventDefault()
                                    component.onLinkClicked(l)
                                }
                            }
                            +l
                        } } }
                        p { a(hnLink) {
                            attrs {
                                onClickFunction = {
                                    it.preventDefault()
                                    component.onLinkClicked(hnLink, true)
                                }
                            }
                            +hnLink
                        } }
                    }
                } }
                ul {
                    commentTree(m.comments)
                }
            }
        }
    }

    private fun RDOMBuilder<UL>.commentTree(comments: List<NewsDetail.Comment>) {
        for (comment in comments) {
            when (comment) {
                is NewsDetail.Comment.Loading -> li { +"..." }
                is NewsDetail.Comment.Content.Collapsed -> li { +"${comment.user} — ${comment.time} ${if (comment.childrenCount.isNotEmpty()) "/ ${comment.childrenCount}" else ""} "
                    a("/") {
                        attrs {
                            onClickFunction = {
                                it.preventDefault()
                                component.onCommentClicked(comment.id)
                            }
                        }
                        +"[+]"
                    }
                }
                is NewsDetail.Comment.Content.Expanded -> {
                    li {
                        attrs.jsStyle {
                            border = "1px solid black"
                        }
                        +"${comment.user} — ${comment.time} "
                        a("/") {
                            attrs {
                                onClickFunction = {
                                    it.preventDefault()
                                    component.onCommentClicked(comment.id)
                                }
                            }
                            +"[-]"
                        }
                        p {
                            for (text in comment.text) {
                                when (text) {
                                    is NewsDetail.Text.Plain -> text.text.split("\n").filterNot { it.isEmpty() }.map {
                                        +it
                                        br {}
                                    }
                                    is NewsDetail.Text.Emphasis -> em { text.text.split("\n").filterNot { it.isEmpty() }.map {
                                        p { +it }
                                    } }
                                    is NewsDetail.Text.Link -> a(text.link) {
                                        attrs {
                                            onClickFunction = {
                                                it.preventDefault()
                                                component.onLinkClicked(text.link)
                                            }
                                        }
                                        text.text.split("\n").filterNot { it.isEmpty() }.map {
                                            p { +it }
                                        }
                                    }
                                    is NewsDetail.Text.Code -> code { pre { +text.text } }
                                }
                            }
                        }
                    }
                    ul { commentTree(comment.children) }
                }
            }
        }
    }

    class NewsDetailState(
        var model: NewsDetail.Model
    ) : State
}
