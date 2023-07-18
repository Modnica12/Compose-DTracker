package model.details

import formatDuration
import getTime
import model.TrackerRecord

data class TrackerRecordDetails(
    val id: String?,
    val project: TrackerProject?,
    val activity: ActivityPresentation?,
    val task: String?,
    val description: String?,
    val start: String?,
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
            start = null,
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
        start = start.getTime(),
        duration = duration.formatDuration()
    )
