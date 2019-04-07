package org.dukecon.android.ui.features.timemachine

import mu.KotlinLogging
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import javax.inject.Inject

private val logger = KotlinLogging.logger {}

class DebugCurrentTimeProvider @Inject constructor() : CustomizableCurrentTimeProvider {

    override fun currentTimeMillis(): Long {
        return OffsetDateTime.now().toInstant().toEpochMilli() + offset
    }

    private var offset: Long = 0

    override fun setCustomMillis(value: Long) {
        offset = value - OffsetDateTime.now().toInstant().toEpochMilli()
        logger.info { "Setting custom time to %s".format(getCurrentTime()) }
    }

    private fun getCurrentTime(): String {
        val instant = Instant.ofEpochMilli(currentTimeMillis())
        val now = instant.atZone(ZoneId.systemDefault()).toOffsetDateTime()
        return now.toString()
    }
}
