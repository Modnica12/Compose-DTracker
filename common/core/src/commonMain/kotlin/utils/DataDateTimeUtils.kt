package utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

// Mapping to format with Z at the end
fun LocalDateTime.formatToRemoteTime(): String {
    return toInstant(timeZone = TimeZone.UTC).toString()
}
