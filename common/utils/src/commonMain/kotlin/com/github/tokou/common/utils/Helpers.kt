package com.github.tokou.common.utils

suspend fun <T, R> T.runLogging(tag: String, message: String, block: suspend T.() -> R): R? {
    return try {
        block()
    } catch (e: Throwable) {
        logger.e(tag, e) { message }
        null
    }
}
