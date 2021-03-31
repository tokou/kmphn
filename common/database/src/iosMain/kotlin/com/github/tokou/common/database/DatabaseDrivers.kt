package com.github.tokou.common.database

import com.github.tokou.common.utils.logger
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration
import co.touchlab.sqliter.DatabaseConnection

actual fun inMemoryDatabaseDriver(): SqlDriver {
    val schema = NewsDatabase.Schema
    val create = { connection: DatabaseConnection -> wrapConnection(connection, schema::create) }
    val upgrade = { connection: DatabaseConnection, oldVersion: Int, newVersion: Int ->
        wrapConnection(connection) {
            schema.migrate(it, oldVersion, newVersion)
        }
    }
    val configuration = DatabaseConfiguration(":memory:", schema.version, create, upgrade, inMemory = true)
    return NativeSqliteDriver(configuration)
}

fun persistentDatabaseDriver(): SqlDriver =
    NativeSqliteDriver(NewsDatabase.Schema, "NewsDatabase.db").logging()
