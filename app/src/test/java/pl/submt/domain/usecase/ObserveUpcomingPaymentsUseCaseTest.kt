package pl.submt.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import pl.submt.domain.model.BillingCycle
import pl.submt.testing.FakeSubscriptionRepository
import pl.submt.testing.subscription
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveUpcomingPaymentsUseCaseTest {

    private val november = LocalDate.of(2026, 11, 1) to LocalDate.of(2026, 11, 30)

    @Test
    fun returnsPaymentsOfActiveSubscriptionsSortedByDateThenName() = runTest {
        val repository = FakeSubscriptionRepository(
            listOf(
                subscription(id = 1, name = "Spotify", firstPaymentDate = LocalDate.of(2026, 1, 10)),
                subscription(id = 2, name = "Netflix", firstPaymentDate = LocalDate.of(2026, 3, 10)),
                subscription(id = 3, name = "Gym", cycle = BillingCycle.WEEKLY, firstPaymentDate = LocalDate.of(2026, 11, 2)),
                subscription(id = 4, name = "Cancelled", firstPaymentDate = LocalDate.of(2026, 1, 5), isActive = false),
                subscription(id = 5, name = "Yearly", cycle = BillingCycle.YEARLY, firstPaymentDate = LocalDate.of(2026, 6, 1)),
            ),
        )
        val useCase = ObserveUpcomingPaymentsUseCase(repository, UnconfinedTestDispatcher(testScheduler))

        val payments = useCase(november.first, november.second).first()

        assertEquals(
            listOf(
                "2026-11-02 Gym",
                "2026-11-09 Gym",
                "2026-11-10 Netflix",
                "2026-11-10 Spotify",
                "2026-11-16 Gym",
                "2026-11-23 Gym",
                "2026-11-30 Gym",
            ),
            payments.map { "${it.date} ${it.subscription.name}" },
        )
    }

    @Test
    fun updatesWhenSubscriptionsChange() = runTest {
        val repository = FakeSubscriptionRepository()
        val useCase = ObserveUpcomingPaymentsUseCase(repository, UnconfinedTestDispatcher(testScheduler))
        assertEquals(0, useCase(november.first, november.second).first().size)

        repository.addSubscription(subscription(firstPaymentDate = LocalDate.of(2026, 11, 20)))

        assertEquals(1, useCase(november.first, november.second).first().size)
    }
}
