package org.dukecon.data.model

import org.threeten.bp.OffsetDateTime

data class EventEntity(val id: String,
                       val title: String,
                       val abstractText: String,
                       val startTime: OffsetDateTime,
                       val endTime: OffsetDateTime,
                       val speakerIds: List<String>,
                       val locationId: String,
                       val languageId: String,
                       val trackId:String,
                       val audienceId:String,
                       val eventTypeId:String,
                       val demo: Boolean,
                       val simultan: Boolean,
                       val veryPopular: Boolean,
                       val fullyBooked: Boolean,
                       val numberOfFavorites: Int)

