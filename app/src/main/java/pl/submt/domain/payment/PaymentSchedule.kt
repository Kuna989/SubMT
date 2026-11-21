package pl.submt.domain.payment

import pl.submt.domain.model.BillingCycle
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Payment date calculations. Payment dates are never stored – they are derived from the
 * first payment date and the billing cycle, so they are always up to date.
 *
 * Payment number n (n = 0, 1, 2, ...) falls on `cycle.addTo(firstPaymentDate, n)`.
 */
object PaymentSchedule {

    /** The first payment falling on or after [date]. */
    fun nextPaymentOnOrAfter(
        firstPaymentDate: LocalDate,
        cycle: BillingCycle,
        date: LocalDate,
    ): LocalDate = cycle.addTo(firstPaymentDate, indexOnOrAfter(firstPaymentDate, cycle, date))

    /** All payments between [from] and [to] (both inclusive), in chronological order. */
    fun paymentsBetween(
        firstPaymentDate: LocalDate,
        cycle: BillingCycle,
        from: LocalDate,
        to: LocalDate,
    ): List<LocalDate> {
        if (to.isBefore(from)) return emptyList()

        var index = indexOnOrAfter(firstPaymentDate, cycle, from)
        return buildList {
            var payment = cycle.addTo(firstPaymentDate, index)
            while (!payment.isAfter(to)) {
                add(payment)
                index++
                payment = cycle.addTo(firstPaymentDate, index)
            }
        }
    }

    /**
     * Index n of the earliest payment on or after [date]. Starts from an estimate based on the
     * whole number of cycles that have elapsed (never too high) and steps forward, which is
     * O(1) in practice even for subscriptions started years ago.
     */
    private fun indexOnOrAfter(firstPaymentDate: LocalDate, cycle: BillingCycle, date: LocalDate): Long {
        if (!firstPaymentDate.isBefore(date)) return 0

        var index = when (cycle) {
            BillingCycle.WEEKLY -> ChronoUnit.WEEKS.between(firstPaymentDate, date)
            BillingCycle.MONTHLY -> ChronoUnit.MONTHS.between(firstPaymentDate, date)
            BillingCycle.QUARTERLY -> ChronoUnit.MONTHS.between(firstPaymentDate, date) / 3
            BillingCycle.YEARLY -> ChronoUnit.YEARS.between(firstPaymentDate, date)
        }
        while (cycle.addTo(firstPaymentDate, index).isBefore(date)) {
            index++
        }
        return index
    }
}
