package pl.submt.domain.usecase

import kotlinx.coroutines.CancellationException
import pl.submt.domain.model.DataError
import pl.submt.domain.model.DataResult

/**
 * Runs a repository call and turns unexpected exceptions (e.g. SQLiteException) into
 * [DataError.DATABASE], so the UI can show a message instead of crashing.
 * Coroutine cancellation is rethrown – swallowing it would break structured concurrency.
 */
internal inline fun <T> safeDataCall(block: () -> DataResult<T>): DataResult<T> =
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        DataResult.Error(DataError.DATABASE)
    }
