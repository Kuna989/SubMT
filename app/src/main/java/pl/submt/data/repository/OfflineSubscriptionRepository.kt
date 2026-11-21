package pl.submt.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import pl.submt.data.local.dao.SubscriptionDao
import pl.submt.data.local.mapper.toDomain
import pl.submt.data.local.mapper.toEntity
import pl.submt.di.IoDispatcher
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository
import javax.inject.Inject

/** [SubscriptionRepository] backed by the local Room database. Works fully offline. */
class OfflineSubscriptionRepository @Inject constructor(
    private val dao: SubscriptionDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : SubscriptionRepository {

    override fun observeSubscriptions(): Flow<List<Subscription>> =
        dao.observeAll()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)

    override fun observeActiveSubscriptions(): Flow<List<Subscription>> =
        dao.observeActive()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)

    override fun observeSubscription(id: Long): Flow<Subscription?> =
        dao.observeById(id)
            .map { it?.toDomain() }
            .flowOn(ioDispatcher)

    override suspend fun getSubscription(id: Long): Subscription? = withContext(ioDispatcher) {
        dao.getById(id)?.toDomain()
    }

    override suspend fun getSubscriptions(): List<Subscription> = withContext(ioDispatcher) {
        dao.getAll().map { it.toDomain() }
    }

    override suspend fun addSubscription(subscription: Subscription): Long = withContext(ioDispatcher) {
        dao.insert(subscription.toEntity())
    }

    override suspend fun updateSubscription(subscription: Subscription): Boolean = withContext(ioDispatcher) {
        dao.update(subscription.toEntity()) > 0
    }

    override suspend fun deleteSubscription(id: Long): Boolean = withContext(ioDispatcher) {
        dao.deleteById(id) > 0
    }
}
