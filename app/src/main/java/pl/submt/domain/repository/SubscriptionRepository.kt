package pl.submt.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.submt.domain.model.Subscription

/**
 * Access to stored subscriptions. The domain and UI layers depend only on this interface;
 * the Room implementation lives in `data/repository`. Tests use an in-memory fake.
 *
 * Write methods may throw (e.g. `SQLiteException`); use cases catch and map those errors.
 */
interface SubscriptionRepository {

    /** All subscriptions sorted by name; emits again after every change. */
    fun observeSubscriptions(): Flow<List<Subscription>>

    /** Only active (not cancelled) subscriptions, sorted by name. */
    fun observeActiveSubscriptions(): Flow<List<Subscription>>

    /** A single subscription, or null when it does not exist (or was deleted). */
    fun observeSubscription(id: Long): Flow<Subscription?>

    suspend fun getSubscription(id: Long): Subscription?

    /** One-shot snapshot for background work (reminders, CSV export). */
    suspend fun getSubscriptions(): List<Subscription>

    /**
     * Inserts [subscription]. A new subscription ([Subscription.isNew]) gets a generated id;
     * one with an id keeps it (used to restore a deleted subscription on "Undo").
     * @return id of the stored subscription.
     */
    suspend fun addSubscription(subscription: Subscription): Long

    /** @return false when no subscription with this id exists. */
    suspend fun updateSubscription(subscription: Subscription): Boolean

    /** @return false when no subscription with this id exists. */
    suspend fun deleteSubscription(id: Long): Boolean
}
