package pl.submt.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.submt.domain.model.BillingCycle
import pl.submt.domain.model.SubscriptionCategory
import java.time.LocalDate

/**
 * A single subscription stored in the `subscriptions` table.
 *
 * Money is kept as [amountMinor] in minor units (e.g. grosze / cents) to avoid
 * floating point rounding errors. Next payment dates are not stored: they are
 * computed from [firstPaymentDate] and [billingCycle] in the domain layer, so
 * they never go stale.
 */
@Entity(
    tableName = "subscriptions",
    indices = [
        Index(value = ["is_active"]),
        Index(value = ["category"]),
    ],
)
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "amount_minor")
    val amountMinor: Long,

    /** ISO 4217 currency code, e.g. "PLN", "EUR", "USD". */
    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "billing_cycle")
    val billingCycle: BillingCycle,

    @ColumnInfo(name = "first_payment_date")
    val firstPaymentDate: LocalDate,

    @ColumnInfo(name = "category")
    val category: SubscriptionCategory,

    /** How many days before a payment the reminder is shown; null = no reminder. */
    @ColumnInfo(name = "reminder_days_before")
    val reminderDaysBefore: Int?,

    @ColumnInfo(name = "notes")
    val notes: String?,

    @ColumnInfo(name = "is_active", defaultValue = "1")
    val isActive: Boolean = true,

    /** Creation time in epoch milliseconds (UTC). */
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
