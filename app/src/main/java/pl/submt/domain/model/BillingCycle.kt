package pl.submt.domain.model

import java.time.LocalDate

/** How often a subscription is charged. */
enum class BillingCycle(
    /** Number of charges in one year; used to normalise costs. */
    val paymentsPerYear: Int,
) {
    WEEKLY(paymentsPerYear = 52),
    MONTHLY(paymentsPerYear = 12),
    QUARTERLY(paymentsPerYear = 4),
    YEARLY(paymentsPerYear = 1),
    ;

    /**
     * Date of the payment [periods] cycles after [start].
     *
     * Always computed from the original start date (never by adding one period at a time),
     * so end-of-month dates do not drift: 31 Jan + 2 months = 31 Mar, not 28 Mar.
     * Days that do not exist in the target month are clamped (31 Jan + 1 month = 28/29 Feb).
     */
    fun addTo(start: LocalDate, periods: Long): LocalDate = when (this) {
        WEEKLY -> start.plusWeeks(periods)
        MONTHLY -> start.plusMonths(periods)
        QUARTERLY -> start.plusMonths(periods * 3)
        YEARLY -> start.plusYears(periods)
    }
}
