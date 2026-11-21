package pl.submt.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository
import javax.inject.Inject

/** Live list of subscriptions, sorted by name. */
class ObserveSubscriptionsUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    operator fun invoke(activeOnly: Boolean = false): Flow<List<Subscription>> =
        if (activeOnly) repository.observeActiveSubscriptions() else repository.observeSubscriptions()
}
