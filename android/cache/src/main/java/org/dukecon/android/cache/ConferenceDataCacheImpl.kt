package org.dukecon.android.cache

import io.ktor.util.date.GMTDate
import mu.KotlinLogging
import org.dukecon.android.cache.persistance.ConferenceCacheSerializer
import org.dukecon.data.model.*
import org.dukecon.data.repository.ConferenceDataCache

private val logger = KotlinLogging.logger {}

/**
 * Cached implementation for retrieving and saving Event instances. This class implements the
 * [ConferenceDataCache] from the Data layer as it is that layers responsibility for defining the
 * operations in which data store implementation layers can carry out. Just simple in memory chache
 */
class ConferenceDataCacheImplconstructor(
        private val conferenceCacheSerializer: ConferenceCacheSerializer,
        private val preferencesHelper: PreferencesHelper
) : ConferenceDataCache {

    override fun getKeycloak(): KeycloakEntity {
        logger.info { "reading getRooms from memory cache" }
        return keycloakEntity
    }

    var cachedRooms: List<RoomEntity> = listOf()
    var cachedEvents: List<EventEntity> = listOf()
    var cacheSpeakers: List<SpeakerEntity> = listOf()
    var cacheFavorites: List<FavoriteEntity> = listOf()
    var cacheMetaData: MetaDataEntity = emptyMetaDataEntity()

    private fun emptyMetaDataEntity(): MetaDataEntity {
        return MetaDataEntity("", emptyList(), emptyList(), emptyList(),
                LanguageEntity("", "", 0, emptyMap(), ""),
                emptyList(), emptyList(), "")
    }

    var keycloakEntity = KeycloakEntity(
            "dukecon-latest",
            "https://keycloak.dukecon.org/auth",
            "none",
            "dukecon",
            "/rest/preferences",
            false
    )

    init {
        if (preferencesHelper.lastCacheTime > 0) {
            logger.info { "cold start, reading data from disc cache" }
            conferenceCacheSerializer.run {
                cachedRooms = readRooms()
                cachedEvents = readEvents()
                cacheSpeakers = readSpeakers()
                cacheFavorites = readFavorites()
                keycloakEntity = readKeyCloack()
                cacheMetaData = readMetaData()
            }
        }
    }

    override fun clearEvents() {
        preferencesHelper.lastCacheTime = 0
        cachedEvents = listOf()
        cacheSpeakers = listOf()
        cachedRooms = listOf()
        cacheFavorites = listOf()
    }

    override fun saveRooms(rooms: List<RoomEntity>) {
        cachedRooms = rooms
        conferenceCacheSerializer.writeRooms(cachedRooms)
        preferencesHelper.lastCacheTime = System.currentTimeMillis()
    }

    override fun getRooms(): List<RoomEntity> {
        logger.info { "reading getRooms from memory cache" }
        return cachedRooms
    }


    override fun saveEvents(events: List<EventEntity>) {
        cachedEvents = events
        conferenceCacheSerializer.writeEvents(events)
        preferencesHelper.lastCacheTime = System.currentTimeMillis()
    }

    override fun getEvents(): List<EventEntity> {
        logger.info { "reading getEvents from memory cache" }
        return cachedEvents
    }

    override fun getEvent(id: String): EventEntity {
        logger.info { "reading getEvent with id= ${id} from memory cache" }
        return cachedEvents.find { event -> event.id == id } ?: emptyEntity()
    }

    override fun saveSpeakers(speakers: List<SpeakerEntity>) {
        cacheSpeakers = speakers
        conferenceCacheSerializer.writeSpeakers(speakers)
        preferencesHelper.lastCacheTime = System.currentTimeMillis()
    }

    override fun getSpeakers(): List<SpeakerEntity> {
        return cacheSpeakers
    }

    override fun getSpeaker(id: String): SpeakerEntity {
        logger.info { "reading speaker with id= ${id} from memory cache" }
        return cacheSpeakers.find { speaker -> speaker.id == id } ?: emptySpeakerEntity()
    }

    override fun getFavorites(): List<FavoriteEntity> {
        logger.info { "reading favorites from memory cache" }
        return cacheFavorites
    }

    override fun saveFavorites(favorite: List<FavoriteEntity>): List<FavoriteEntity> {
        cacheFavorites = favorite
        conferenceCacheSerializer.writeFavorites(cacheFavorites)
        preferencesHelper.lastCacheTime = System.currentTimeMillis()

        return cacheFavorites
    }

    override fun getMetaData(): MetaDataEntity {
        logger.info { "reading metadata from memory cache" }

        return cacheMetaData
    }

    override fun saveMetaData(metaDataEntity: MetaDataEntity) {
        cacheMetaData = metaDataEntity
        conferenceCacheSerializer.writeMetaData(metaDataEntity)
        preferencesHelper.lastCacheTime = System.currentTimeMillis()
    }


    private fun emptySpeakerEntity(): SpeakerEntity {
        return SpeakerEntity("", "", "", "", "", "", "")
    }

    private fun emptyEntity(): EventEntity {
        val event = EventEntity("", "", "",
                GMTDate(),
                GMTDate(),
                listOf(), "", "", "", "", "", false, false, false, false, 0)
        return event
    }
}