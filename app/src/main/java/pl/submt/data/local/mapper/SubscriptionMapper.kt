package pl.submt.data.local.mapper

import pl.submt.data.local.entity.SubscriptionEntity
import pl.submt.domain.model.Money
import pl.submt.domain.model.Subscription
import java.time.Instant

/** Conversions between the Room entity and the domain model. */

fun SubscriptionEntity.toDomain(): Subscription = Subscription(
    id = id,
    name = name,
    price = Money(amountMinor = amountMinor, currencyCode = currency),
    billingCycle = billingCycle,
    firstPaymentDate = firstPaymentDate,
    category = category,
    reminderDaysBefore = reminderDaysBefore,
    notes = notes,
    isActive = isActive,
    createdAt = Instant.ofEpochMilli(createdAt),
)

fun Subscription.toEntity(): SubscriptionEntity = SubscriptionEntity(
    id = id,
    name = name,
    amountMinor = price.amountMinor,
    currency = price.currencyCode,
    billingCycle = billingCycle,
    firstPaymentDate = firstPaymentDate,
    category = category,
    reminderDaysBefore = reminderDaysBefore,
    notes = notes,
    isActive = isActive,
    createdAt = createdAt.toEpochMilli(),
)
