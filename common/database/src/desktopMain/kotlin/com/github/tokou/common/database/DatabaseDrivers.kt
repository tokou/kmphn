package com.github.tokou.common.database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

actual fun inMemoryDatabaseDriver(): SqlDriver {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    NewsDatabase.Schema.create(driver)
    return driver
}

fun persistentDatabaseDriver(): SqlDriver {
    val databasePath = File(System.getProperty("java.io.tmpdir"), "NewsDatabase.db")
    val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
    NewsDatabase.Schema.create(driver)
    return driver
}
