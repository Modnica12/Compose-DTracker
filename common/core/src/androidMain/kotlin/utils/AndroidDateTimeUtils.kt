package utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

actual fun getDate(): String {
    return LocalDateTime.now(ZoneOffset.UTC).toLocalDate().toString()
}

actual fun getTime(): String {
    return LocalDateTime.now(ZoneOffset.UTC).toLocalTime().format(DateTimeFormatter.ISO_TIME)
}
