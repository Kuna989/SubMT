package pl.submt.testing

import pl.submt.domain.model.BillingCycle
import pl.submt.domain.model.Money
import pl.submt.domain.model.Subscription
import pl.submt.domain.model.SubscriptionCategory
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/** Fixed "today" used across tests: 21 Nov 2026. */
val TODAY: LocalDate = LocalDate.of(2026, 11, 21)

val FIXED_CLOCK: Clock = Clock.fixed(TODAY.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

val CREATED_AT: Instant = Instant.parse("2026-01-01T10:00:00Z")

fun subscription(
    id: Long = Subscription.NEW_ID,
    name: String = "Netflix",
    amountMinor: Long = 4_300,
    currency: String = "PLN",
    cycle: BillingCycle = BillingCycle.MONTHLY,
    firstPaymentDate: LocalDate = LocalDate.of(2026, 1, 15),
    category: SubscriptionCategory = SubscriptionCategory.STREAMING,
    reminderDaysBefore: Int? = 3,
    notes: String? = null,
    isActive: Boolean = true,
) = Subscription(
    id = id,
    name = name,
    price = Money(amountMinor, currency),
    billingCycle = cycle,
    firstPaymentDate = firstPaymentDate,
    category = category,
    reminderDaysBefore = reminderDaysBefore,
    notes = notes,
    isActive = isActive,
    createdAt = CREATED_AT,
)
