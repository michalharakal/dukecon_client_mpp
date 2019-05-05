package org.dukecon.android.ui.features.timemachine

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import javax.inject.Inject

class DebugCurrentTimeProvider @Inject constructor() : CustomizableCurrentTimeProvider {

    override fun currentTimeMillis(): Long {
        return OffsetDateTime.now().toInstant().toEpochMilli() + offset
    }

    private var offset: Long = 0

    override fun setCustomMillis(value: Long) {
        offset = value - OffsetDateTime.now().toInstant().toEpochMilli()
    }

    private fun getCurrentTime(): String {
        val instant = Instant.ofEpochMilli(currentTimeMillis())
        val now = instant.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        return now.toString()
    }
}
