package datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
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

fun LocalDateTime.addDuration(secondsDuration: Int): LocalDateTime {
    return this.toInstant(TimeZone.currentSystemDefault())
        .plus(secondsDuration.toDuration(DurationUnit.SECONDS))
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun getOffsetInHours(): Int {
    val now = getCurrentDateTime()
    val nowWithoutOffset = now.toInstant(offset = UtcOffset.ZERO)
    val nowWithOffset = now.toInstant(TimeZone.currentSystemDefault())
    return nowWithoutOffset.periodUntil(nowWithOffset, timeZone = TimeZone.UTC).hours
}

fun LocalDateTime.plusHours(hours: Int): LocalDateTime {
    return toInstant(offset = UtcOffset.ZERO)
        .plus(hours, DateTimeUnit.HOUR, timeZone = TimeZone.UTC)
        .toLocalDateTime(timeZone = TimeZone.UTC)
}

fun LocalDateTime.toUTC(): LocalDateTime {
    return plusHours(getOffsetInHours())
}
