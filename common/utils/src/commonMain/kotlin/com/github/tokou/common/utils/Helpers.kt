package com.github.tokou.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T, R> T.runLogging(tag: String, message: String, block: suspend T.() -> R): R? {
    return try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        logger.e(tag, e) { message }
        null
    }
}

fun <T, R> Flow<Result<T>>.subscribe(
    scope: CoroutineScope,
    dispatch: suspend (R) -> Unit,
    flowOn: CoroutineContext = Dispatchers.Default,
    handle: suspend (Result<T>) -> R
) = map(handle)
    .flowOn(flowOn)
    .onEach(dispatch)
    .launchIn(scope)

fun <T> runBlocking(block: suspend CoroutineScope.() -> T): T =
    runBlocking(EmptyCoroutineContext, block)
