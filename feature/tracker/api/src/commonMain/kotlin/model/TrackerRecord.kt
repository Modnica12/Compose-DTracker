package model

import datetime.getCurrentDateTime
import kotlinx.datetime.LocalDateTime

data class TrackerRecord(
    val id: String,
    val project: TrackerProject?, // TODO: null не нужен
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
