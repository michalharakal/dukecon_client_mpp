package org.dukecon.data.repository

import org.dukecon.data.model.*


/**
 * Interface defining methods for the caching of Bufferroos. This is to be implemented by the
 * cache layer, using this interface as a way of communicating.
 */
interface ConferenceDataCache {

    /**
     * Clear all Events from the cache
     */
    fun clearEvents()

    /**
     * Save a given list of EventEntity to the cache
     */
    fun saveEvents(events: List<EventEntity>)

    /**
     * Retrieve a list of Events, from the cache
     */
    fun getEvents(): List<EventEntity>

    fun getSpeakers(): List<SpeakerEntity>
    fun getSpeaker(id: String): SpeakerEntity

    fun saveSpeakers(speakers: List<SpeakerEntity>)

    fun getRooms(): List<RoomEntity>
    fun saveRooms(rooms: List<RoomEntity>)

    fun getEvent(id: String): EventEntity

    fun getFavorites(): List<FavoriteEntity>
    fun saveFavorites(favorite: List<FavoriteEntity>): List<FavoriteEntity>

    fun getKeycloak(): KeycloakEntity

    fun getMetaData(): MetaDataEntity
    fun saveMetaData(metaDataEntity: MetaDataEntity)

}