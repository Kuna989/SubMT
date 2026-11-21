package pl.submt.domain.validation

import pl.submt.domain.model.Subscription
import java.time.Clock
import java.time.LocalDate
import java.util.Currency
import javax.inject.Inject

/** Form fields that can carry a validation error. */
enum class SubscriptionField {
    NAME,
    AMOUNT,
    CURRENCY,
    FIRST_PAYMENT_DATE,
    REMINDER_DAYS,
    NOTES,
}

/** Why a field is invalid. The UI maps each value to a localized message. */
enum class ValidationError {
    REQUIRED,
    TOO_LONG,
    NOT_POSITIVE,
    TOO_LARGE,
    INVALID_CURRENCY,
    TOO_FAR_IN_PAST,
    TOO_FAR_IN_FUTURE,
    OUT_OF_RANGE,
}

/**
 * Business rules for a subscription. Runs in the domain layer, so every way of saving
 * data (form, future import, ...) is checked the same way, regardless of the UI.
 */
class SubscriptionValidator @Inject constructor(
    private val clock: Clock,
) {

    /** @return one error per invalid field; empty map when the subscription is valid. */
    fun validate(subscription: Subscription): Map<SubscriptionField, ValidationError> = buildMap {
        val name = subscription.name.trim()
        when {
            name.isEmpty() -> put(SubscriptionField.NAME, ValidationError.REQUIRED)
            name.length > NAME_MAX_LENGTH -> put(SubscriptionField.NAME, ValidationError.TOO_LONG)
        }

        val amount = subscription.price.amountMinor
        when {
            amount <= 0 -> put(SubscriptionField.AMOUNT, ValidationError.NOT_POSITIVE)
            amount > MAX_AMOUNT_MINOR -> put(SubscriptionField.AMOUNT, ValidationError.TOO_LARGE)
        }

        if (!isValidCurrency(subscription.price.currencyCode)) {
            put(SubscriptionField.CURRENCY, ValidationError.INVALID_CURRENCY)
        }

        val date = subscription.firstPaymentDate
        val today = LocalDate.now(clock)
        when {
            date.isBefore(EARLIEST_PAYMENT_DATE) ->
                put(SubscriptionField.FIRST_PAYMENT_DATE, ValidationError.TOO_FAR_IN_PAST)
            date.isAfter(today.plusYears(MAX_YEARS_AHEAD)) ->
                put(SubscriptionField.FIRST_PAYMENT_DATE, ValidationError.TOO_FAR_IN_FUTURE)
        }

        val reminder = subscription.reminderDaysBefore
        if (reminder != null && reminder !in REMINDER_DAYS_RANGE) {
            put(SubscriptionField.REMINDER_DAYS, ValidationError.OUT_OF_RANGE)
        }

        if ((subscription.notes?.length ?: 0) > NOTES_MAX_LENGTH) {
            put(SubscriptionField.NOTES, ValidationError.TOO_LONG)
        }
    }

    private fun isValidCurrency(code: String): Boolean =
        CURRENCY_CODE_REGEX.matches(code) &&
            runCatching { Currency.getInstance(code) }.isSuccess

    companion object {
        const val NAME_MAX_LENGTH = 50
        const val NOTES_MAX_LENGTH = 500

        /** 1 000 000.00 in minor units – guards against typos such as an extra zero. */
        const val MAX_AMOUNT_MINOR = 100_000_000L

        val REMINDER_DAYS_RANGE = 0..30
        val EARLIEST_PAYMENT_DATE: LocalDate = LocalDate.of(2000, 1, 1)
        const val MAX_YEARS_AHEAD = 5L

        private val CURRENCY_CODE_REGEX = Regex("[A-Z]{3}")
    }
}
