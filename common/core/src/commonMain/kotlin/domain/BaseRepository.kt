package domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface BaseRepository {

    suspend fun <T> doWithResult(action: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                Result.success(action())
            } catch (exception: Exception) {
                Result.failure(exception = exception)
            }
        }
    }
}
