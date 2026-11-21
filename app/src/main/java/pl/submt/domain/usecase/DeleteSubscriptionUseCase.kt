package pl.submt.domain.usecase

import pl.submt.domain.model.DataError
import pl.submt.domain.model.DataResult
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository
import javax.inject.Inject

/**
 * Deletes a subscription and returns the deleted data, so the UI can offer "Undo"
 * (see [RestoreSubscriptionUseCase]).
 */
class DeleteSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
) {
    suspend operator fun invoke(id: Long): DataResult<Subscription> = safeDataCall {
        val subscription = repository.getSubscription(id)
        if (subscription != null && repository.deleteSubscription(id)) {
            DataResult.Success(subscription)
        } else {
            DataResult.Error(DataError.NOT_FOUND)
        }
    }
}
