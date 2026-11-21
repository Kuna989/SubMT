package pl.submt.domain.usecase

import pl.submt.domain.model.DataResult
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository
import javax.inject.Inject

/** Puts back a subscription removed by [DeleteSubscriptionUseCase], keeping its id ("Undo"). */
class RestoreSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(subscription: Subscription): DataResult<Long> = safeDataCall {
        DataResult.Success(repository.addSubscription(subscription))
    }
}
