package details.edit

import datetime.addDuration
import datetime.toUTC
import details.base.TrackerRecordViewModel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import model.TrackerRecord
import model.TrackerTask
import utils.detailsTimeToLocal
import utils.formatDetails

internal class EditTrackerRecordViewModel(
    private val recordId: String
) : TrackerRecordViewModel() {

    init {
        withViewModelScope {
            repository.getRecordWithId(recordId)?.let { record ->
                mutableDurationFlow.value = record.duration
                setRecordData(record = record)
                val endTime = record.start.addDuration(record.duration).time.formatDetails()
                viewState = viewState.copy(endTime = endTime)
            }
        }
    }

    override fun endTimeChanged(endTimeInSeconds: Long) {
        val startTimeSeconds = LocalDateTime(
            date = viewState.date,
            time = viewState.startTime.detailsTimeToLocal()
        )
            .toInstant(offset = UtcOffset.ZERO)
            .epochSeconds
        if (endTimeInSeconds > startTimeSeconds) {
            val diff = endTimeInSeconds - startTimeSeconds
            mutableDurationFlow.value = diff.toInt()
        }
    }

    override fun startTimeChanged(startTimeInSeconds: Long) {
        viewState.endTime?.let { endTime ->
            val endTimeInSeconds = LocalDateTime(
                date = viewState.date,
                time = endTime.detailsTimeToLocal()
            )
                .toInstant(timeZone = TimeZone.currentSystemDefault())
                .epochSeconds
            if (endTimeInSeconds > startTimeInSeconds) {
                val diff = endTimeInSeconds - startTimeInSeconds
                mutableDurationFlow.value = diff.toInt()
            }
        }
    }

    override suspend fun applyChanges(projectId: Int) {
        with(viewState) {
            repository.updateRecord(
                trackerRecord = TrackerRecord(
                    id = recordId,
                    project = selectedProject,
                    activity = selectedActivity,
                    task = TrackerTask(selectedTask, false),
                    description = selectedDescription,
                    start = LocalDateTime(
                        date = date,
                        time = startTime.detailsTimeToLocal()
                    ).toUTC(),
                    duration = durationFlow.value
                )
            )
        }
    }
}
