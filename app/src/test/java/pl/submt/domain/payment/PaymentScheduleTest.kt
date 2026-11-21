package pl.submt.domain.payment

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.submt.domain.model.BillingCycle
import java.time.LocalDate

class PaymentScheduleTest {

    private fun date(year: Int, month: Int, day: Int) = LocalDate.of(year, month, day)

    // nextPaymentOnOrAfter

    @Test
    fun next_whenFirstPaymentIsInFuture_isFirstPayment() {
        val next = PaymentSchedule.nextPaymentOnOrAfter(date(2026, 12, 10), BillingCycle.MONTHLY, date(2026, 11, 21))

        assertEquals(date(2026, 12, 10), next)
    }

    @Test
    fun next_whenPaymentIsToday_isToday() {
        val next = PaymentSchedule.nextPaymentOnOrAfter(date(2026, 1, 21), BillingCycle.MONTHLY, date(2026, 11, 21))

        assertEquals(date(2026, 11, 21), next)
    }

    @Test
    fun next_monthly_rollsToNextMonth() {
        val next = PaymentSchedule.nextPaymentOnOrAfter(date(2026, 1, 15), BillingCycle.MONTHLY, date(2026, 11, 21))

        assertEquals(date(2026, 12, 15), next)
    }

    @Test
    fun next_weekly() {
        // 2026-11-02 is a Monday; next Monday on/after Sat 21 Nov is 23 Nov.
        val next = PaymentSchedule.nextPaymentOnOrAfter(date(2026, 11, 2), BillingCycle.WEEKLY, date(2026, 11, 21))

        assertEquals(date(2026, 11, 23), next)
    }

    @Test
    fun next_quarterly() {
        val next = PaymentSchedule.nextPaymentOnOrAfter(date(2026, 1, 10), BillingCycle.QUARTERLY, date(2026, 11, 21))

        assertEquals(date(2027, 1, 10), next)
    }

    @Test
    fun next_yearly_afterManyYears() {
        val next = PaymentSchedule.nextPaymentOnOrAfter(date(2010, 3, 5), BillingCycle.YEARLY, date(2026, 11, 21))

        assertEquals(date(2027, 3, 5), next)
    }

    @Test
    fun monthly_endOfMonth_isClampedWithoutDrift() {
        val first = date(2026, 1, 31)

        assertEquals(date(2026, 2, 28), PaymentSchedule.nextPaymentOnOrAfter(first, BillingCycle.MONTHLY, date(2026, 2, 1)))
        // Back to the 31st in March – no drift to the 28th.
        assertEquals(date(2026, 3, 31), PaymentSchedule.nextPaymentOnOrAfter(first, BillingCycle.MONTHLY, date(2026, 3, 1)))
        assertEquals(date(2026, 4, 30), PaymentSchedule.nextPaymentOnOrAfter(first, BillingCycle.MONTHLY, date(2026, 4, 1)))
    }

    @Test
    fun yearly_leapDay_isClampedTo28thInCommonYears() {
        val first = date(2024, 2, 29)

        assertEquals(date(2025, 2, 28), PaymentSchedule.nextPaymentOnOrAfter(first, BillingCycle.YEARLY, date(2024, 3, 1)))
        assertEquals(date(2028, 2, 29), PaymentSchedule.nextPaymentOnOrAfter(first, BillingCycle.YEARLY, date(2027, 3, 1)))
    }

    // paymentsBetween

    @Test
    fun between_returnsAllPaymentsInRangeInclusive() {
        val payments = PaymentSchedule.paymentsBetween(
            firstPaymentDate = date(2026, 1, 15),
            cycle = BillingCycle.MONTHLY,
            from = date(2026, 3, 15),
            to = date(2026, 6, 15),
        )

        assertEquals(
            listOf(date(2026, 3, 15), date(2026, 4, 15), date(2026, 5, 15), date(2026, 6, 15)),
            payments,
        )
    }

    @Test
    fun between_weeklyInOneMonth() {
        val payments = PaymentSchedule.paymentsBetween(
            firstPaymentDate = date(2026, 11, 2),
            cycle = BillingCycle.WEEKLY,
            from = date(2026, 11, 1),
            to = date(2026, 11, 30),
        )

        assertEquals(listOf(2, 9, 16, 23, 30).map { date(2026, 11, it) }, payments)
    }

    @Test
    fun between_rangeBeforeFirstPayment_isEmpty() {
        val payments = PaymentSchedule.paymentsBetween(
            firstPaymentDate = date(2026, 12, 1),
            cycle = BillingCycle.MONTHLY,
            from = date(2026, 11, 1),
            to = date(2026, 11, 30),
        )

        assertTrue(payments.isEmpty())
    }

    @Test
    fun between_invertedRange_isEmpty() {
        val payments = PaymentSchedule.paymentsBetween(
            firstPaymentDate = date(2026, 1, 1),
            cycle = BillingCycle.WEEKLY,
            from = date(2026, 12, 31),
            to = date(2026, 1, 1),
        )

        assertTrue(payments.isEmpty())
    }

    @Test
    fun between_yearlyPaymentOutsideRange_isEmpty() {
        val payments = PaymentSchedule.paymentsBetween(
            firstPaymentDate = date(2025, 6, 1),
            cycle = BillingCycle.YEARLY,
            from = date(2026, 7, 1),
            to = date(2026, 12, 31),
        )

        assertTrue(payments.isEmpty())
    }
}
