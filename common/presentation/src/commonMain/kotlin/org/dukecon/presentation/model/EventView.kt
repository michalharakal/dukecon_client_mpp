package org.dukecon.presentation.model

import org.dukecon.date.GMTDate
import org.dukecon.domain.model.Favorite

data class EventView(val id: String,
                     val title: String,
                     val description: String,
                     val startTime: GMTDate,
                     val endTime: GMTDate,
                     val speakers: List<SpeakerView>,
                     val favorite: Favorite,
                     val room: String)