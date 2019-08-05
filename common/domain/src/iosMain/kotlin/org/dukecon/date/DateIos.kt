package org.dukecon.date

import platform.Foundation.*

/**
 * Create new gmt date from the [timestamp].
 * @param timestamp is a number of epoch milliseconds (it is `now` by default).
 */
@Suppress("FunctionName")
actual fun GMTDate(timestamp: Long?): GMTDate {
    val interval: Double = (timestamp ?: 0) / 1000.0
    return NSDate.dateWithTimeIntervalSince1970(interval).toDate(timestamp)
}

/**
 * Create an instance of [GMTDate] from the specified date/time components
 */
@Suppress("FunctionName")
actual fun GMTDate(
        seconds: Int, minutes: Int, hours: Int, dayOfMonth: Int, month: Month, year: Int
): GMTDate {
    val components = NSDateComponents()

    //val day = NSNumber(dayOfMonth)
    //val monthOfYear = NSNumber(month.ordinal)
    val date = NSCalendar.currentCalendar().dateFromComponents(components)
    return date?.toDate(null) ?: NSDate.date().toDate(null)
}

private fun NSDate.toDate(timestamp: Long?): GMTDate {
    timestamp?.let { GMTDate(timestamp = it) }
    return GMTDate(0, 0, 0, 0, Month.JANUARY, 0)
}

