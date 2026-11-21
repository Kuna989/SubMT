package pl.submt.data.local.converter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import pl.submt.domain.model.BillingCycle
import pl.submt.domain.model.SubscriptionCategory
import java.time.LocalDate

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun localDate_roundTripsThroughEpochDay() {
        val date = LocalDate.of(2026, 2, 28)

        val stored = converters.localDateToEpochDay(date)

        assertEquals(date, converters.epochDayToLocalDate(stored))
    }

    @Test
    fun localDate_epochDayKeepsChronologicalOrder() {
        val earlier = converters.localDateToEpochDay(LocalDate.of(2025, 12, 31))!!
        val later = converters.localDateToEpochDay(LocalDate.of(2026, 1, 1))!!

        assertEquals(1L, later - earlier)
    }

    @Test
    fun billingCycle_roundTripsForEveryValue() {
        BillingCycle.entries.forEach { cycle ->
            assertEquals(cycle, converters.stringToBillingCycle(converters.billingCycleToString(cycle)))
        }
    }

    @Test
    fun category_roundTripsForEveryValue() {
        SubscriptionCategory.entries.forEach { category ->
            assertEquals(category, converters.stringToCategory(converters.categoryToString(category)))
        }
    }

    @Test
    fun category_unknownValueFallsBackToOther() {
        assertEquals(SubscriptionCategory.OTHER, converters.stringToCategory("REMOVED_CATEGORY"))
    }

    @Test
    fun nullValues_stayNull() {
        assertNull(converters.localDateToEpochDay(null))
        assertNull(converters.epochDayToLocalDate(null))
        assertNull(converters.billingCycleToString(null))
        assertNull(converters.stringToBillingCycle(null))
        assertNull(converters.categoryToString(null))
        assertNull(converters.stringToCategory(null))
    }
}
