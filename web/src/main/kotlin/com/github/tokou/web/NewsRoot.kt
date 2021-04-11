package com.github.tokou.web

import com.arkivanov.decompose.RouterState
import com.github.tokou.common.root.NewsRoot
import react.RBuilder
import react.RState

class NewsRootR(props: Props<NewsRoot>) : RenderableComponent<NewsRoot, NewsRootR.State>(
    props = props,
    initialState = State(routerState = props.component.routerState.value)
) {

    init {
        component.routerState.bindToState { routerState = it }
    }

    override fun RBuilder.render() {
        when (val instance = state.routerState.activeChild.instance) {
            is NewsRoot.Child.Main -> renderableChild(NewsMainR::class, instance.component)
            is NewsRoot.Child.Detail -> renderableChild(NewsDetailR::class, instance.component)
        }
    }

    class State(
        var routerState: RouterState<*, NewsRoot.Child>,
    ) : RState
}
