package utils

import kotlinx.datetime.LocalTime

fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this - (hours * 3600)) / 60
    val seconds = this % 60
    return "${hours.toPrettyTime()}:${minutes.toPrettyTime()}:${seconds.toPrettyTime()}"
}

private fun Int.toPrettyTime(): String {
    return toString().run { if (length < 2) "0$this" else this }
}

fun LocalTime.formatDetails(): String {
    return "${hour.toPrettyTime()}${minute.toPrettyTime()}"
}

fun String.detailsTimeToLocal(): LocalTime {
    val hours = take(2).toInt()
    val minutes = takeLast(2).toInt()
    return LocalTime(hour = hours, minute = minutes, second = 0, nanosecond = 0)
}
