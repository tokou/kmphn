package com.github.tokou.common.database

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val kidsAdapter = object : ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> = Json.decodeFromString(databaseValue)
    override fun encode(value: List<Long>): String = Json.encodeToString(value)
}

fun createDatabase(driver: SqlDriver): NewsDatabase {
    val adapter = Item.Adapter(kidsAdapter)
    return NewsDatabase(driver, adapter)
}
