package utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSTimeZone
import platform.Foundation.defaultTimeZone
import platform.Foundation.timeZoneWithAbbreviation

actual fun getDate(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.timeZone = NSTimeZone.defaultTimeZone
    dateFormatter.dateFormat = "yyyy-MM-dd"
    return dateFormatter.stringFromDate(NSDate())
}

actual fun getTime(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.timeZone = NSTimeZone.timeZoneWithAbbreviation("UTC") ?: NSTimeZone.defaultTimeZone
    dateFormatter.dateFormat = "HH:mm:ss"
    return dateFormatter.stringFromDate(NSDate())
}
