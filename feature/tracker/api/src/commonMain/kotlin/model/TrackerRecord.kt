package model

import utils.getDateTime

data class TrackerRecord(
    val id: String,
    val project: TrackerProject?,
    val activity: TrackerActivity?,
    val task: TrackerTask?,
    val start: String, // Заюзать сразу какой-нибудь Date
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
                start = getDateTime(),
                duration = 0,
                description = ""
            )
    }
}
