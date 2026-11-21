package pl.submt.data.local.converter

import androidx.room.TypeConverter
import pl.submt.domain.model.BillingCycle
import pl.submt.domain.model.SubscriptionCategory
import java.time.LocalDate

/**
 * Room type converters.
 *
 * Dates are stored as epoch days (INTEGER) so they sort and compare correctly in SQL.
 * Enums are stored by name (TEXT), so reordering enum constants never corrupts data.
 */
class Converters {

    @TypeConverter
    fun localDateToEpochDay(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun epochDayToLocalDate(epochDay: Long?): LocalDate? = epochDay?.let(LocalDate::ofEpochDay)

    @TypeConverter
    fun billingCycleToString(cycle: BillingCycle?): String? = cycle?.name

    @TypeConverter
    fun stringToBillingCycle(value: String?): BillingCycle? = value?.let(BillingCycle::valueOf)

    @TypeConverter
    fun categoryToString(category: SubscriptionCategory?): String? = category?.name

    @TypeConverter
    fun stringToCategory(value: String?): SubscriptionCategory? =
        value?.let { name ->
            // Unknown values (e.g. a category removed in a later version) fall back to OTHER.
            SubscriptionCategory.entries.firstOrNull { it.name == name } ?: SubscriptionCategory.OTHER
        }
}
