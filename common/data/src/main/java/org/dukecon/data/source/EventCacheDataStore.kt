package org.dukecon.data.source

import org.dukecon.data.model.*
import org.dukecon.data.repository.ConferenceDataCache
import org.dukecon.data.repository.EventDataStore



/**
 * Implementation of the [EventDataStore] interface to provide a means of communicating
 * with the local data source
 */
open class EventCacheDataStore constructor(private val conferenceDataCache: ConferenceDataCache) :
        EventDataStore {
    override fun getMetaData(): MetaDataEntity {
        return conferenceDataCache.getMetaData()
    }

    override fun getKeycloak(): KeycloakEntity {
        return conferenceDataCache.getKeycloak()
    }

    override fun submitFeedback(feedback: FeedbackEntity): Any {
        throw UnsupportedOperationException()
    }

    override fun saveFavorites(favorites: List<FavoriteEntity>): List<FavoriteEntity> {
        return conferenceDataCache.saveFavorites(favorites)
    }

    override fun getFavorites(): List<FavoriteEntity> {
        return conferenceDataCache.getFavorites()
    }

    override fun getSpeaker(id: String): SpeakerEntity {
        return conferenceDataCache.getSpeaker(id)
    }

    override fun getEvent(id: String): EventEntity {
        return conferenceDataCache.getEvent(id)
    }

    override fun getRooms(): List<RoomEntity> {
        return conferenceDataCache.getRooms()
    }

    override fun saveRooms(rooms: List<RoomEntity>) {
         conferenceDataCache.saveRooms(rooms)
    }

    override fun getSpeakers(): List<SpeakerEntity> {
        return conferenceDataCache.getSpeakers()
    }

    override fun saveSpeakers(speakers: List<SpeakerEntity>) {
        conferenceDataCache.saveSpeakers(speakers)
    }

    override fun clearEvents() {
        return conferenceDataCache.clearEvents()
    }

    override fun saveEvents(events: List<EventEntity>) {
        conferenceDataCache.saveEvents(events)
    }

    override fun getEvents(): List<EventEntity> {
        return conferenceDataCache.getEvents()
    }

    fun saveMetaData(metaData: MetaDataEntity) {
        conferenceDataCache.saveMetaData(metaData)
    }
}