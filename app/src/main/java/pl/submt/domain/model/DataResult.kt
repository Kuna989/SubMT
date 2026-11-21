package pl.submt.domain.model

/** Result of an operation that can fail for reasons outside the user's control. */
sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val error: DataError) : DataResult<Nothing>
}

/** Failure reasons the UI turns into user-facing messages. */
enum class DataError {
    /** The requested item does not exist (e.g. it was deleted in the meantime). */
    NOT_FOUND,

    /** Reading or writing the local database failed (disk full, corrupted file, ...). */
    DATABASE,
}
