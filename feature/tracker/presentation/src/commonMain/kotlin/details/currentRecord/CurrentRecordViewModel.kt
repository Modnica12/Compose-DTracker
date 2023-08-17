package details.currentRecord

import currentRecord.CurrentRecordManager
import datetime.toUTC
import details.base.TrackerRecordViewModel
import di.getKoinInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import usecase.StartTrackerTimerUseCase
import utils.detailsTimeToLocal

internal class CurrentRecordViewModel: TrackerRecordViewModel() {

    private val currentRecordManager: CurrentRecordManager = getKoinInstance()
    private val startTrackerTimerUseCase = StartTrackerTimerUseCase()

    private var timerJob: Job? = null

    init {
        if (currentRecordManager.currentRecord.value == null) {
            startTimer(startDuration = 0)
        } else {
            repository.currentRecord.value?.let { currentRecord ->
                setRecordData(record = currentRecord)
                startTimer(startDuration = currentRecord.duration)
            }
        }
    }

    override fun startTimeChanged(startTimeInSeconds: Long) {
        val now = Clock.System.now()

        if (now.epochSeconds > startTimeInSeconds) {
            val diff = now.epochSeconds - startTimeInSeconds
            startTimer(startDuration = diff.toInt())
        }
    }

    override suspend fun applyChanges(projectId: Int) {
        with(viewState) {
            // Timer can be stopped from records list, we need to rerun it
            currentRecordManager.startTimer()
            currentRecordManager.updateRecord(
                projectId = projectId,
                activityId = selectedActivity?.id,
                task = selectedTask,
                description = selectedDescription,
                // Mapping from user's timezone
                // TODO: если создаем новую запись, то на бэк уйдет время с 00 секунд, т.к. храним только часы и минуты
                start = LocalDateTime(date = date, time = startTime.detailsTimeToLocal()).toUTC()
            )
        }
    }

    private fun startTimer(startDuration: Int) {
        timerJob?.cancel()
        timerJob = null
        timerJob = viewModelScope.launch {
            startTrackerTimerUseCase(startDuration = startDuration)
                .collectLatest { duration ->
                    ensureActive()
                    mutableDurationFlow.value = duration
                }
        }
    }
}
