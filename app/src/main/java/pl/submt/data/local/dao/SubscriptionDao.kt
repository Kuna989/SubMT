package pl.submt.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.submt.data.local.entity.SubscriptionEntity

/**
 * Data access for subscriptions.
 *
 * Reads return [Flow], so the UI updates automatically after every change.
 * Writes are `suspend` functions; Room runs them off the main thread.
 */
@Dao
interface SubscriptionDao {

    @Query("SELECT * FROM subscriptions ORDER BY name COLLATE NOCASE ASC")
    fun observeAll(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE is_active = 1 ORDER BY name COLLATE NOCASE ASC")
    fun observeActive(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE id = :id")
    fun observeById(id: Long): Flow<SubscriptionEntity?>

    @Query("SELECT * FROM subscriptions WHERE id = :id")
    suspend fun getById(id: Long): SubscriptionEntity?

    /** One-shot read used by background work (reminders, CSV export). */
    @Query("SELECT * FROM subscriptions ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAll(): List<SubscriptionEntity>

    /** @return the generated row id. */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(subscription: SubscriptionEntity): Long

    /** @return number of updated rows (0 if the subscription does not exist). */
    @Update
    suspend fun update(subscription: SubscriptionEntity): Int

    /** @return number of deleted rows. */
    @Delete
    suspend fun delete(subscription: SubscriptionEntity): Int

    @Query("DELETE FROM subscriptions WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("SELECT COUNT(*) FROM subscriptions")
    fun observeCount(): Flow<Int>
}
