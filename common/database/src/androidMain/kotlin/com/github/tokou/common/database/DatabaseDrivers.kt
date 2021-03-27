package com.github.tokou.common.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual fun inMemoryDatabaseDriver(): SqlDriver {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    NewsDatabase.Schema.create(driver)
    return driver.logging()
}

fun peristentDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = NewsDatabase.Schema,
        context = context,
        name = "NewsDatabase.db"
    ).logging()
