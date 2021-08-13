package com.github.tokou.web

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.await
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import react.dom.render
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.js.Promise

suspend fun main() = coroutineScope {
    render(document.getElementById("app")) {
        child(App::class) {}
    }
}

suspend fun <T> Promise<T>.await(): T = suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
    this@await.then(
        onFulfilled = { cont.resume(it) },
        onRejected = { cont.resumeWithException(it) }
    )
}
