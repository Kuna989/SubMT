package pl.submt.domain.model

import pl.submt.domain.payment.PaymentSchedule
import java.time.Instant
import java.time.LocalDate

/**
 * A paid subscription (Netflix, Spotify, ...). Pure domain model – no Room or Android types,
 * so it can be used and tested anywhere.
 */
data class Subscription(
    val id: Long = NEW_ID,
    val name: String,
    /** Price of a single payment. */
    val price: Money,
    val billingCycle: BillingCycle,
    val firstPaymentDate: LocalDate,
    val category: SubscriptionCategory,
    /** Days before a payment when the reminder fires; null = no reminder. */
    val reminderDaysBefore: Int? = null,
    val notes: String? = null,
    val isActive: Boolean = true,
    val createdAt: Instant,
) {
    val isNew: Boolean get() = id == NEW_ID

    /** First payment on or after [date] (e.g. today). */
    fun nextPaymentDate(date: LocalDate): LocalDate =
        PaymentSchedule.nextPaymentOnOrAfter(firstPaymentDate, billingCycle, date)

    /** Total paid in one year at the current price. */
    val yearlyCost: Money get() = price * billingCycle.paymentsPerYear

    /**
     * Average cost per month (yearly cost / 12, rounded half up). For a weekly plan this is
     * more accurate than "price × 4", because a year has 52 weeks, not 48.
     */
    val monthlyCost: Money
        get() = when (billingCycle) {
            BillingCycle.MONTHLY -> price
            else -> Money(roundedDivision(yearlyCost.amountMinor, 12), price.currencyCode)
        }

    companion object {
        /** Id of a subscription that has not been saved yet (Room ids start at 1). */
        const val NEW_ID = 0L

        private fun roundedDivision(value: Long, divisor: Long): Long =
            Math.floorDiv(value * 2 + divisor, divisor * 2)
    }
}
