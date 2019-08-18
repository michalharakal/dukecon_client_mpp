package org.dukecon.domain.repository

import org.dukecon.date.GMTDate
import org.dukecon.domain.model.*

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This is to be implemented by the data layer, setting the requirements for the
 * operations that need to be implemented
 */
interface ConferenceRepository {
    var onRefreshListeners: List<() -> Unit>

    fun update(completion: (String) -> Unit)

    fun getEvents(): List<Event>

    suspend fun update()
    suspend fun saveEvents(events: List<Event>)

    suspend fun getEvents(day: Int): List<Event>
    suspend fun getEventDates(): List<GMTDate>

    suspend fun getSpeakers(): List<Speaker>
    suspend fun getSpeaker(id: String): Speaker?
    suspend fun saveSpeakers(speakers: List<Speaker>)

    suspend fun getRooms(): List<Room>
    suspend fun saveRooms(rooms: List<Room>)

    suspend fun getEvent(id: String): Event?

    suspend fun saveFavorite(favorite: Favorite): List<Favorite>
    suspend fun getFavorites(): List<Favorite>

    suspend fun submitFeedback(feedback: Feedback): Any

    suspend fun getKeyCloak(): Keycloak
    suspend fun getMetaData(): MetaData

}