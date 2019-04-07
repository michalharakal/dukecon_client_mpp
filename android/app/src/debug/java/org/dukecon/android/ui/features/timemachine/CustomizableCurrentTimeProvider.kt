package org.dukecon.android.ui.features.timemachine

import org.dukecon.domain.features.time.CurrentTimeProvider

interface CustomizableCurrentTimeProvider : CurrentTimeProvider {
    fun setCustomMillis(value: Long)
}
