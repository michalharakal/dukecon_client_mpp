package org.dukecon.date

@Suppress("FunctionName")
actual fun GMTDate(timestamp: Long?): GMTDate {
    return GMTDate(0)
}

actual fun GMTDate(seconds: Int, minutes: Int, hours: Int, dayOfMonth: Int, month: Month, year: Int): GMTDate {
    return GMTDate(0)
}
