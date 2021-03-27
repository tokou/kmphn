package com.github.tokou.common.database

import com.github.tokou.common.utils.logDatabase
import com.github.tokou.common.utils.logger
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.logs.LogSqliteDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.datetime.Instant;

fun SqlDriver.logging() = LogSqliteDriver(this) {
    if (logDatabase) logger.d("SQLDELIGHT") { it }
}

private val idsListAdapter = object : ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> = Json.decodeFromString(databaseValue)
    override fun encode(value: List<Long>): String = Json.encodeToString(value)
}

private val instantAdapter = object : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long) = Instant.fromEpochSeconds(databaseValue)
    override fun encode(value: Instant) = value.epochSeconds
}

fun createDatabase(driver: SqlDriver): NewsDatabase {
    val itemAdapter = Item.Adapter(instantAdapter, instantAdapter, idsListAdapter)
    val commentAdapter = Comment.Adapter(instantAdapter, instantAdapter, idsListAdapter)
    return NewsDatabase(driver, commentAdapter, itemAdapter)
}
