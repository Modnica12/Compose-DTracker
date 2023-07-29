package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun String.parseToDateTime(): LocalDateTime {
    return toInstant().toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
}

fun getCurrentDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

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

fun LocalDateTime.addDuration(secondsDuration: Int): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault())
        .plus(secondsDuration.toDuration(DurationUnit.SECONDS))
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun String.detailsTimeToLocal(): LocalTime {
    val hours = take(2).toInt()
    val minutes = takeLast(2).toInt()
    return LocalTime(hour = hours, minute = minutes, second = 0, nanosecond = 0)
}

fun getOffsetInHours(): Int {
    val now = getCurrentDateTime()
    val nowWithoutOffset = now.toInstant(offset = UtcOffset.ZERO)
    val nowWithOffset = now.toInstant(TimeZone.currentSystemDefault())
    return nowWithoutOffset.periodUntil(nowWithOffset, timeZone = TimeZone.UTC).hours
}

fun LocalDateTime.plusHours(hours: Int): LocalDateTime {
    return toInstant(offset = UtcOffset.ZERO)
        .apply { println(this) }
        .plus(hours, DateTimeUnit.HOUR, timeZone = TimeZone.UTC)
        .apply { println(this) }
        .toLocalDateTime(timeZone = TimeZone.UTC)
        .apply { println(this) }
}

fun LocalDateTime.toUTC(): LocalDateTime {
    return plusHours(getOffsetInHours())
}
