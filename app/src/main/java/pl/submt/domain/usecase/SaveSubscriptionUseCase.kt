package pl.submt.domain.usecase

import pl.submt.domain.model.DataError
import pl.submt.domain.model.DataResult
import pl.submt.domain.model.Subscription
import pl.submt.domain.repository.SubscriptionRepository
import pl.submt.domain.validation.SubscriptionField
import pl.submt.domain.validation.SubscriptionValidator
import pl.submt.domain.validation.ValidationError
import java.time.Clock
import javax.inject.Inject

sealed interface SaveSubscriptionResult {
    data class Success(val id: Long) : SaveSubscriptionResult

    /** Nothing was saved; show [errors] next to the matching form fields. */
    data class Invalid(val errors: Map<SubscriptionField, ValidationError>) : SaveSubscriptionResult

    data class Error(val error: DataError) : SaveSubscriptionResult
}

/**
 * Adds a new subscription ([Subscription.isNew]) or updates an existing one.
 * Input is normalised (trimmed name, blank notes -> null, upper-case currency) and
 * validated before anything is written.
 */
class SaveSubscriptionUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    private val validator: SubscriptionValidator,
    private val clock: Clock,
) {
    suspend operator fun invoke(subscription: Subscription): SaveSubscriptionResult {
        val normalized = subscription.normalized()

        val errors = validator.validate(normalized)
        if (errors.isNotEmpty()) return SaveSubscriptionResult.Invalid(errors)

        val result = safeDataCall {
            if (normalized.isNew) {
                val id = repository.addSubscription(normalized.copy(createdAt = clock.instant()))
                DataResult.Success(id)
            } else if (repository.updateSubscription(normalized)) {
                DataResult.Success(normalized.id)
            } else {
                DataResult.Error(DataError.NOT_FOUND)
            }
        }

        return when (result) {
            is DataResult.Success -> SaveSubscriptionResult.Success(result.data)
            is DataResult.Error -> SaveSubscriptionResult.Error(result.error)
        }
    }

    private fun Subscription.normalized(): Subscription = copy(
        name = name.trim(),
        notes = notes?.trim()?.takeIf { it.isNotEmpty() },
        price = price.copy(currencyCode = price.currencyCode.trim().uppercase()),
    )
}
