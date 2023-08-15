package model.details

import datetime.getCurrentDateTime
import kotlinx.datetime.LocalDateTime
import model.TrackerRecord
import utils.formatDuration

data class TrackerRecordDetails(
    val id: String?,
    val project: TrackerProject?,
    val activity: ActivityPresentation?,
    val task: String?,
    val description: String?,
    val start: LocalDateTime,
    val duration: String?,
) {

    val isTracking: Boolean
        get() = !duration.isNullOrEmpty()

    companion object {
        val default = TrackerRecordDetails(
            id = null,
            project = null,
            activity = null,
            task = null,
            description = null,
            start = getCurrentDateTime(),
            duration = null,
        )
    }
}

fun TrackerRecord.toDetails(): TrackerRecordDetails =
    TrackerRecordDetails(
        id = id,
        project = project?.toPresentation(),
        activity = activity?.toPresentation(),
        task = task?.name,
        description = description,
        start = start,
        duration = duration.formatDuration()
    )
