package pl.submt.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.submt.domain.model.DataError
import pl.submt.domain.validation.SubscriptionField
import pl.submt.domain.validation.SubscriptionValidator
import pl.submt.testing.FIXED_CLOCK
import pl.submt.testing.FakeSubscriptionRepository
import pl.submt.testing.subscription

class SaveSubscriptionUseCaseTest {

    private val repository = FakeSubscriptionRepository()
    private val saveSubscription = SaveSubscriptionUseCase(repository, SubscriptionValidator(FIXED_CLOCK), FIXED_CLOCK)

    @Test
    fun newSubscription_isAdded_withGeneratedIdAndCreationTime() = runTest {
        val result = saveSubscription(subscription(name = "Netflix"))

        assertTrue(result is SaveSubscriptionResult.Success)
        val saved = repository.all.single()
        assertEquals((result as SaveSubscriptionResult.Success).id, saved.id)
        assertEquals(FIXED_CLOCK.instant(), saved.createdAt)
    }

    @Test
    fun input_isNormalized() = runTest {
        saveSubscription(subscription(name = "  Spotify  ", currency = " eur ", notes = "   "))

        val saved = repository.all.single()
        assertEquals("Spotify", saved.name)
        assertEquals("EUR", saved.price.currencyCode)
        assertNull(saved.notes)
    }

    @Test
    fun invalidSubscription_isNotSaved_andErrorsAreReturned() = runTest {
        val result = saveSubscription(subscription(name = "", amountMinor = 0))

        assertEquals(
            setOf(SubscriptionField.NAME, SubscriptionField.AMOUNT),
            (result as SaveSubscriptionResult.Invalid).errors.keys,
        )
        assertTrue(repository.all.isEmpty())
    }

    @Test
    fun existingSubscription_isUpdated() = runTest {
        val repository = FakeSubscriptionRepository(listOf(subscription(id = 1, amountMinor = 4_300)))
        val save = SaveSubscriptionUseCase(repository, SubscriptionValidator(FIXED_CLOCK), FIXED_CLOCK)

        val result = save(subscription(id = 1, amountMinor = 6_000))

        assertEquals(SaveSubscriptionResult.Success(1), result)
        assertEquals(6_000L, repository.all.single().price.amountMinor)
    }

    @Test
    fun updatingDeletedSubscription_returnsNotFound() = runTest {
        val result = saveSubscription(subscription(id = 99))

        assertEquals(SaveSubscriptionResult.Error(DataError.NOT_FOUND), result)
    }

    @Test
    fun databaseFailure_returnsDatabaseError() = runTest {
        repository.failWith = IllegalStateException("disk I/O error")

        val result = saveSubscription(subscription())

        assertEquals(SaveSubscriptionResult.Error(DataError.DATABASE), result)
    }
}
