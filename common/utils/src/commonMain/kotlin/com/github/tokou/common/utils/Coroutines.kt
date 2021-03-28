package com.github.tokou.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

expect fun <T> runBlocking(context: CoroutineContext, block: suspend CoroutineScope.() -> T): T
