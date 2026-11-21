package pl.submt.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository

/**
 * In-memory [SubscriptionRepository] for unit tests of use cases and ViewModels.
 * Set [failWith] to simulate a database error on the next write.
 */
class FakeSubscriptionRepository(initial: List<Subscription> = emptyList()) : SubscriptionRepository {

    private val subscriptions = MutableStateFlow(initial.associateBy { it.id })
    private var nextId = (initial.maxOfOrNull { it.id } ?: 0) + 1

    var failWith: Exception? = null

    val all: List<Subscription> get() = subscriptions.value.values.sortedBy { it.name.lowercase() }

    override fun observeSubscriptions(): Flow<List<Subscription>> =
        subscriptions.map { map -> map.values.sortedBy { it.name.lowercase() } }

    override fun observeActiveSubscriptions(): Flow<List<Subscription>> =
        observeSubscriptions().map { list -> list.filter { it.isActive } }

    override fun observeSubscription(id: Long): Flow<Subscription?> = subscriptions.map { it[id] }

    override suspend fun getSubscription(id: Long): Subscription? = subscriptions.value[id]

    override suspend fun getSubscriptions(): List<Subscription> = all

    override suspend fun addSubscription(subscription: Subscription): Long {
        throwIfFailing()
        val id = if (subscription.isNew) nextId++ else subscription.id
        subscriptions.update { it + (id to subscription.copy(id = id)) }
        return id
    }

    override suspend fun updateSubscription(subscription: Subscription): Boolean {
        throwIfFailing()
        if (subscription.id !in subscriptions.value) return false
        subscriptions.update { it + (subscription.id to subscription) }
        return true
    }

    override suspend fun deleteSubscription(id: Long): Boolean {
        throwIfFailing()
        if (id !in subscriptions.value) return false
        subscriptions.update { it - id }
        return true
    }

    private fun throwIfFailing() {
        failWith?.let { throw it }
    }
}
