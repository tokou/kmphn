package com.github.tokou.common.database

import com.github.tokou.common.utils.logDatabase
import com.github.tokou.common.utils.logger
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.logs.LogSqliteDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SqlDriver.logging() = LogSqliteDriver(this) {
    if (logDatabase) logger.d("SQLDELIGHT") { it }
}

private val kidsAdapter = object : ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> = Json.decodeFromString(databaseValue)
    override fun encode(value: List<Long>): String = Json.encodeToString(value)
}

fun createDatabase(driver: SqlDriver): NewsDatabase {
    val itemAdapter = Item.Adapter(kidsAdapter)
    val commentAdapter = Comment.Adapter(kidsAdapter)
    return NewsDatabase(driver, commentAdapter, itemAdapter)
}
