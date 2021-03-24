package com.github.tokou.common.database

import com.squareup.sqldelight.db.SqlDriver

expect fun inMemoryDatabaseDriver(): SqlDriver
