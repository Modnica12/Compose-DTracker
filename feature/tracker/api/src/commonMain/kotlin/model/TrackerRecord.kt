package model

import kotlinx.datetime.LocalDateTime
import utils.getCurrentDateTime

data class TrackerRecord(
    val id: String,
    val project: TrackerProject?,
    val activity: TrackerActivity?,
    val task: TrackerTask?,
    val start: LocalDateTime,
    val duration: Int,
    val description: String,
) {

    companion object {
        val default: TrackerRecord
            get() = TrackerRecord(
                id = "",
                project = null,
                activity = null,
                task = null,
                start = getCurrentDateTime(),
                duration = 0,
                description = ""
            )
    }
}
