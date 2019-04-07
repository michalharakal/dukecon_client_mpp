package org.dukecon.data.ext

import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter


fun String.toOffsetDateTime(): OffsetDateTime {
    return OffsetDateTime.parse("$this+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}