
fun String.getDate(): String {
    return split("T").firstOrNull() ?: ""
}

fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this - (hours * 3600)) / 60
    val seconds = this % 60
    return "${hours.toPrettyTime()}:${minutes.toPrettyTime()}:${seconds.toPrettyTime()}"
}

fun Int.toPrettyTime(): String {
    return toString().run { if (length < 2) "0$this" else this }
}
