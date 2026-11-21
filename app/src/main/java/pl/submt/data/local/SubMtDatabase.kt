package pl.submt.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.submt.data.local.converter.Converters
import pl.submt.data.local.dao.SubscriptionDao
import pl.submt.data.local.entity.SubscriptionEntity

/**
 * The app's SQLite database.
 *
 * When adding a table or column: add the entity here, bump [version] and add an
 * `AutoMigration(from = N, to = N + 1)` (or a manual migration in [Migrations]).
 * The schema JSON in `app/schemas/` is generated on build and must be committed.
 */
@Database(
    entities = [SubscriptionEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class SubMtDatabase : RoomDatabase() {

    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        const val NAME = "submt.db"
    }
}
