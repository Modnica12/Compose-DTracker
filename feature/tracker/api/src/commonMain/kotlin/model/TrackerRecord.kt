package model

data class TrackerRecord(
    val id: String,
    val project: TrackerProject,
    val activity: TrackerActivity?,
    val task: TrackerTask,
    val start: String, // Заюзать сразу какой-нибудь Date
    val duration: Int,
    val description: String,
)
