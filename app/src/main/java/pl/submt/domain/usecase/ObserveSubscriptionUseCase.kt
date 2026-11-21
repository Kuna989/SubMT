package pl.submt.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository
import javax.inject.Inject

/** Live single subscription; emits null once it no longer exists. */
class ObserveSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    operator fun invoke(id: Long): Flow<Subscription?> = repository.observeSubscription(id)
}
