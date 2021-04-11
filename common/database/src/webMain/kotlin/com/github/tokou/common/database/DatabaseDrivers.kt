package com.github.tokou.common.database

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.SqlPreparedStatement

private val noOpDriver = object : SqlDriver {
    override fun close() {}
    override fun currentTransaction(): Transacter.Transaction? = null
    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ) {}
    override fun executeQuery(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ): SqlCursor {
        return object : SqlCursor {
            override fun close() {}
            override fun getBytes(index: Int): ByteArray? = null
            override fun getDouble(index: Int): Double? = null
            override fun getLong(index: Int): Long? = null
            override fun getString(index: Int): String? = null
            override fun next(): Boolean = false
        }
    }
    override fun newTransaction(): Transacter.Transaction {
        return object : Transacter.Transaction() {
            override val enclosingTransaction: Transacter.Transaction? get() = null
            override fun endTransaction(successful: Boolean) {}
        }
    }
}

actual fun inMemoryDatabaseDriver(): SqlDriver = noOpDriver
