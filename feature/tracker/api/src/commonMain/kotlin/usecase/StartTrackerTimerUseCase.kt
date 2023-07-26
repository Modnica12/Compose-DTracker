package usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StartTrackerTimerUseCase {

    suspend operator fun invoke(startDuration: Int = 0): Flow<Int> = flow {
        var duration = startDuration
        while (true) {
            emit(duration)
            duration += 1
            delay(1000)
        }
    }
}
