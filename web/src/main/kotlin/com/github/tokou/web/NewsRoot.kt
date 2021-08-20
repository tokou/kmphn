package com.github.tokou.web

import com.arkivanov.decompose.RouterState
import com.github.tokou.common.root.NewsRoot
import react.RBuilder
import react.State

class NewsRootR(props: Props<NewsRoot>) : RenderableComponent<NewsRoot, NewsRootR.NewsRootState>(
    props = props,
    initialState = NewsRootState(routerState = props.component.routerState.value)
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

    class NewsRootState(
        var routerState: RouterState<*, NewsRoot.Child>,
    ) : State
}
