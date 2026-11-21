package pl.submt.data.local

import androidx.room.migration.Migration

/**
 * Manual migrations that Room cannot generate automatically
 * (e.g. renaming or splitting columns). Simple changes such as a new table
 * or a new column with a default value should use `AutoMigration` instead.
 */
object Migrations {
    val ALL: Array<Migration> = emptyArray()
}
