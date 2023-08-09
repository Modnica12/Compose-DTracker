package usecase

import AuthRepository
import TrackerRecordsRepository

class FetchRecordsUseCase(
    private val trackerRecordsRepository: TrackerRecordsRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return authRepository.getUserId()?.let { userId ->
            trackerRecordsRepository.fetchUserRecords(userId)
        } ?: Result.failure(Exception("No user id"))
    }
}
