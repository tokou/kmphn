package com.github.tokou.web

import kotlinext.js.Object
import kotlinext.js.jsObject
import react.RBuilder
import kotlin.reflect.KClass

fun <M : Any, T : RenderableComponent<M, *>> RBuilder.renderableChild(clazz: KClass<out T>, model: M) {
    child(clazz) {
        key = model.uniqueId().toString()
        attrs.component = model
    }
}

var uniqueId: Long = 0L

internal fun Any.uniqueId(): Long {
    var id: dynamic = asDynamic().__unique_id
    if (id == undefined) {
        id = ++uniqueId
        Object.defineProperty<Any, Long>(this, "__unique_id", jsObject { value = id })
    }

    return id
}
