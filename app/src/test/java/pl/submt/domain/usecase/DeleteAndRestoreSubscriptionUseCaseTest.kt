package pl.submt.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pl.submt.domain.model.DataError
import pl.submt.domain.model.DataResult
import pl.submt.testing.FakeSubscriptionRepository
import pl.submt.testing.subscription

class DeleteAndRestoreSubscriptionUseCaseTest {

    private val netflix = subscription(id = 1, name = "Netflix")
    private val spotify = subscription(id = 2, name = "Spotify")
    private val repository = FakeSubscriptionRepository(listOf(netflix, spotify))
    private val delete = DeleteSubscriptionUseCase(repository)
    private val restore = RestoreSubscriptionUseCase(repository)

    @Test
    fun delete_removesSubscription_andReturnsItForUndo() = runTest {
        val result = delete(1)

        assertEquals(DataResult.Success(netflix), result)
        assertEquals(listOf(spotify), repository.all)
    }

    @Test
    fun delete_missingSubscription_returnsNotFound() = runTest {
        assertEquals(DataResult.Error(DataError.NOT_FOUND), delete(42))
    }

    @Test
    fun delete_databaseFailure_returnsDatabaseError() = runTest {
        repository.failWith = RuntimeException("database is locked")

        assertEquals(DataResult.Error(DataError.DATABASE), delete(1))
    }

    @Test
    fun restore_afterDelete_bringsBackSameIdAndData() = runTest {
        val deleted = (delete(1) as DataResult.Success).data

        val result = restore(deleted)

        assertEquals(DataResult.Success(1L), result)
        assertEquals(listOf(netflix, spotify), repository.all)
    }

    @Test
    fun cancellation_isNotSwallowed() = runTest {
        repository.failWith = CancellationException("cancelled")

        val thrown = runCatching { delete(1) }.exceptionOrNull()

        assertTrue(thrown is CancellationException)
    }
}
