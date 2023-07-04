package model

data class TrackerRecord(
    val id: String,
    val project: TrackerProject,
    val activity: TrackerActivity,
    val task: TrackerTask,
    val start: String,
    val duration: Int,
    val description: String,
)
