package model

data class TrackerRecordItem(
    val id: String,
    val description: String,
    val project: String,
    val duration: String
)

fun TrackerRecord.toPresentation(): TrackerRecordItem =
    TrackerRecordItem(
        id = id,
        description = description,
        project = project.name,
        duration = formatDuration(duration)
    )


private fun formatDuration(duration: Int): String {
    val hours = duration / 3600
    val minutes = (duration - (hours * 3600)) / 60
    val seconds = duration % 60
    return "${hours.toPrettyTime()}:${minutes.toPrettyTime()}:${seconds.toPrettyTime()}"
}

private fun Int.toPrettyTime(): String {
    return toString().run { if (length < 2) "0$this" else this }
}
