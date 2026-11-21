package pl.submt.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import pl.submt.testing.TODAY
import pl.submt.testing.subscription
import java.time.LocalDate

class SubscriptionCostTest {

    @Test
    fun monthly_costEqualsPrice() {
        val netflix = subscription(amountMinor = 4_300, cycle = BillingCycle.MONTHLY)

        assertEquals(Money(4_300, "PLN"), netflix.monthlyCost)
        assertEquals(Money(51_600, "PLN"), netflix.yearlyCost)
    }

    @Test
    fun yearly_costIsSpreadOverTwelveMonths() {
        val yearly = subscription(amountMinor = 12_000, cycle = BillingCycle.YEARLY)

        assertEquals(Money(1_000, "PLN"), yearly.monthlyCost)
        assertEquals(Money(12_000, "PLN"), yearly.yearlyCost)
    }

    @Test
    fun weekly_usesFiftyTwoWeeksPerYear() {
        val weekly = subscription(amountMinor = 1_000, cycle = BillingCycle.WEEKLY)

        assertEquals(Money(52_000, "PLN"), weekly.yearlyCost)
        // 52 000 / 12 = 4 333.33 -> 4 333
        assertEquals(Money(4_333, "PLN"), weekly.monthlyCost)
    }

    @Test
    fun quarterly_monthlyCostIsRoundedHalfUp() {
        // 1 000 * 4 / 12 = 333.33 -> 333 ; 1 001 * 4 / 12 = 333.67 -> 334
        assertEquals(333L, subscription(amountMinor = 1_000, cycle = BillingCycle.QUARTERLY).monthlyCost.amountMinor)
        assertEquals(334L, subscription(amountMinor = 1_001, cycle = BillingCycle.QUARTERLY).monthlyCost.amountMinor)
    }

    @Test
    fun costKeepsCurrency() {
        assertEquals("EUR", subscription(currency = "EUR", cycle = BillingCycle.YEARLY).monthlyCost.currencyCode)
    }

    @Test
    fun nextPaymentDate_usesSchedule() {
        val sub = subscription(firstPaymentDate = LocalDate.of(2026, 1, 15), cycle = BillingCycle.MONTHLY)

        assertEquals(LocalDate.of(2026, 12, 15), sub.nextPaymentDate(TODAY))
    }

    @Test
    fun money_addsSameCurrency() {
        assertEquals(Money(300, "PLN"), Money(100, "PLN") + Money(200, "PLN"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun money_refusesToAddDifferentCurrencies() {
        Money(100, "PLN") + Money(100, "EUR")
    }
}
