package org.dukecon.data.ext

import io.ktor.util.date.GMTDate

fun String.toOffsetDateTime(): GMTDate {
    return GMTDate.START // OffsetDateTime.parse("$this+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}