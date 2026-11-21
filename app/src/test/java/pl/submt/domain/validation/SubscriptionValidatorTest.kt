package pl.submt.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.submt.testing.FIXED_CLOCK
import pl.submt.testing.TODAY
import pl.submt.testing.subscription
import java.time.LocalDate

class SubscriptionValidatorTest {

    private val validator = SubscriptionValidator(FIXED_CLOCK)

    private fun errorsFor(sub: pl.submt.domain.model.Subscription) = validator.validate(sub)

    @Test
    fun validSubscription_hasNoErrors() {
        assertTrue(errorsFor(subscription()).isEmpty())
    }

    @Test
    fun blankName_isRequired() {
        assertEquals(ValidationError.REQUIRED, errorsFor(subscription(name = "   "))[SubscriptionField.NAME])
    }

    @Test
    fun tooLongName_isRejected() {
        val name = "a".repeat(SubscriptionValidator.NAME_MAX_LENGTH + 1)

        assertEquals(ValidationError.TOO_LONG, errorsFor(subscription(name = name))[SubscriptionField.NAME])
    }

    @Test
    fun nameAtMaxLength_isAccepted() {
        val name = "a".repeat(SubscriptionValidator.NAME_MAX_LENGTH)

        assertTrue(SubscriptionField.NAME !in errorsFor(subscription(name = name)))
    }

    @Test
    fun zeroOrNegativeAmount_isRejected() {
        assertEquals(ValidationError.NOT_POSITIVE, errorsFor(subscription(amountMinor = 0))[SubscriptionField.AMOUNT])
        assertEquals(ValidationError.NOT_POSITIVE, errorsFor(subscription(amountMinor = -100))[SubscriptionField.AMOUNT])
    }

    @Test
    fun hugeAmount_isRejected() {
        val amount = SubscriptionValidator.MAX_AMOUNT_MINOR + 1

        assertEquals(ValidationError.TOO_LARGE, errorsFor(subscription(amountMinor = amount))[SubscriptionField.AMOUNT])
    }

    @Test
    fun invalidCurrency_isRejected() {
        listOf("", "PL", "pln", "XYZ1", "ABC").forEach { code ->
            assertEquals(
                "currency '$code'",
                ValidationError.INVALID_CURRENCY,
                errorsFor(subscription(currency = code))[SubscriptionField.CURRENCY],
            )
        }
    }

    @Test
    fun knownCurrencies_areAccepted() {
        listOf("PLN", "EUR", "USD", "GBP").forEach { code ->
            assertTrue(code, SubscriptionField.CURRENCY !in errorsFor(subscription(currency = code)))
        }
    }

    @Test
    fun firstPaymentDate_tooFarInPastOrFuture_isRejected() {
        val past = subscription(firstPaymentDate = LocalDate.of(1999, 12, 31))
        val future = subscription(firstPaymentDate = TODAY.plusYears(SubscriptionValidator.MAX_YEARS_AHEAD).plusDays(1))

        assertEquals(ValidationError.TOO_FAR_IN_PAST, errorsFor(past)[SubscriptionField.FIRST_PAYMENT_DATE])
        assertEquals(ValidationError.TOO_FAR_IN_FUTURE, errorsFor(future)[SubscriptionField.FIRST_PAYMENT_DATE])
    }

    @Test
    fun reminderDays_outOfRange_isRejected_nullIsAllowed() {
        assertEquals(ValidationError.OUT_OF_RANGE, errorsFor(subscription(reminderDaysBefore = 31))[SubscriptionField.REMINDER_DAYS])
        assertEquals(ValidationError.OUT_OF_RANGE, errorsFor(subscription(reminderDaysBefore = -1))[SubscriptionField.REMINDER_DAYS])
        assertTrue(SubscriptionField.REMINDER_DAYS !in errorsFor(subscription(reminderDaysBefore = null)))
        assertTrue(SubscriptionField.REMINDER_DAYS !in errorsFor(subscription(reminderDaysBefore = 0)))
    }

    @Test
    fun tooLongNotes_isRejected() {
        val notes = "x".repeat(SubscriptionValidator.NOTES_MAX_LENGTH + 1)

        assertEquals(ValidationError.TOO_LONG, errorsFor(subscription(notes = notes))[SubscriptionField.NOTES])
    }

    @Test
    fun multipleProblems_areAllReported() {
        val errors = errorsFor(subscription(name = "", amountMinor = 0, currency = "??"))

        assertEquals(setOf(SubscriptionField.NAME, SubscriptionField.AMOUNT, SubscriptionField.CURRENCY), errors.keys)
    }
}
