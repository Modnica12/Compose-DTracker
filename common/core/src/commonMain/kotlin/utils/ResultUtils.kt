package utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun <T> withResult(action: suspend () -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            Result.success(action())
        } catch (exception: Exception) {
            Result.failure(exception = exception)
        }
    }
}
