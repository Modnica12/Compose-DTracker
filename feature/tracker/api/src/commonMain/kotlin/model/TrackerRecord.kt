package model

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
                start = "2023-07-18T08:00:00Z",
                duration = 0,
                description = ""
            )
    }
}
