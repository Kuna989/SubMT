package pl.submt.domain.model

import java.time.LocalDate

/** A single scheduled charge of a subscription. Used by the calendar and reminders. */
data class UpcomingPayment(
    val subscription: Subscription,
    val date: LocalDate,
) {
    val amount: Money get() = subscription.price
}
