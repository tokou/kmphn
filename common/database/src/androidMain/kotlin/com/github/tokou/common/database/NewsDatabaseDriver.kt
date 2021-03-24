package com.github.tokou.common.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

fun NewsDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = NewsDatabase.Schema,
        context = context,
        name = "NewsDatabase.db"
    )
