package pl.submt.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import pl.submt.di.DefaultDispatcher
import pl.submt.domain.model.UpcomingPayment
import pl.submt.domain.payment.PaymentSchedule
import pl.submt.domain.repository.SubscriptionRepository
import java.time.LocalDate
import javax.inject.Inject

/**
 * All payments of active subscriptions between [from] and [to] (inclusive), sorted by date
 * and then by name. Used by the calendar (B4) and reminders (B5).
 */
class ObserveUpcomingPaymentsUseCase @Inject constructor(
    private val repository: SubscriptionRepository,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(from: LocalDate, to: LocalDate): Flow<List<UpcomingPayment>> =
        repository.observeActiveSubscriptions()
            .map { subscriptions ->
                subscriptions
                    .flatMap { subscription ->
                        PaymentSchedule
                            .paymentsBetween(subscription.firstPaymentDate, subscription.billingCycle, from, to)
                            .map { date -> UpcomingPayment(subscription, date) }
                    }
                    .sortedWith(compareBy({ it.date }, { it.subscription.name.lowercase() }))
            }
            .flowOn(defaultDispatcher)
}
