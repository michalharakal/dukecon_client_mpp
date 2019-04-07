package org.dukecon.android.cache.persistance

import com.google.gson.Gson
import org.dukecon.data.model.*
import java.io.*

class ConferenceCacheGsonSerializer(private val baseFolder: String,
                                    private val gson: Gson) : ConferenceCacheSerializer {

    private fun getKeycloakFullName() = "$baseFolder/keycloak.json"
    private fun getSpeakersFullName() = "$baseFolder/speakers.json"
    private fun getRoomsFullName() = "$baseFolder/rooms.json"
    private fun getEventsFullName() = "$baseFolder/sessions.json"
    private fun getFavoritesFullName() = "$baseFolder/favorites.json"
    private fun getMetaDataFullName() = "$baseFolder/metaData.json"


    override fun readSpeakers(): List<SpeakerEntity> {
        val sd = File(getSpeakersFullName())
        return if (sd.exists()) {
            val inputStream = FileInputStream(sd)
            val reader = InputStreamReader(inputStream)
            gson.fromJson(reader, Array<SpeakerEntity>::class.java).toList()
        } else {
            listOf()
        }
    }

    override fun readEvents(): List<EventEntity> {
        return try {
            val sd = File(getEventsFullName())
            if (sd.exists()) {
                val inputStream = FileInputStream(sd)
                val reader = InputStreamReader(inputStream)

                gson.fromJson(reader, Array<EventEntity>::class.java).toList()
            } else {
                listOf()
            }
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun readRooms(): List<RoomEntity> {
        val sd = File(getRoomsFullName())
        return if (sd.exists()) {
            val inputStream = FileInputStream(sd)
            val reader = InputStreamReader(inputStream)

            gson.fromJson(reader, Array<RoomEntity>::class.java).toList()
        } else {
            listOf()
        }
    }

    override fun readFavorites(): List<FavoriteEntity> {
        val sd = File(getFavoritesFullName())
        return if (sd.exists()) {
            val inputStream = FileInputStream(sd)
            val reader = InputStreamReader(inputStream)

            gson.fromJson(reader, Array<FavoriteEntity>::class.java).toList()
        } else {
            listOf()
        }
    }

    override fun readMetaData(): MetaDataEntity {
        val sd = File(getMetaDataFullName())
        return if (sd.exists()) {
            val inputStream = FileInputStream(sd)
            val reader = InputStreamReader(inputStream)

            gson.fromJson(reader, MetaDataEntity::class.java)
        } else {
            emptyMetaDataEntity()
        }
    }

    private fun emptyMetaDataEntity(): MetaDataEntity {
        return MetaDataEntity("", emptyList(), emptyList(), emptyList(),
                LanguageEntity("", "", 0, emptyMap(), ""),
                emptyList(), emptyList(), "")
    }

    override fun readKeyCloack(): KeycloakEntity {
        val sd = File(getKeycloakFullName())
        return if (sd.exists()) {
            val inputStream = FileInputStream(sd)
            val reader = InputStreamReader(inputStream)
            gson.fromJson(reader, KeycloakEntity::class.java)
        } else {
            KeycloakEntity(
                    "dukecon-latest",
                    "https://keycloak.dukecon.org/auth",
                    "none",
                    "dukecon",
                    "/rest/preferences",
                    false
            )
        }
    }

    override fun writeRooms(cachedRooms: List<RoomEntity>) {
        writeList(getRoomsFullName(), cachedRooms)
    }

    override fun writeEvents(events: List<EventEntity>) {
        writeList(getEventsFullName(), events)
    }

    override fun writeSpeakers(speakers: List<SpeakerEntity>) {
        writeList(getSpeakersFullName(), speakers)
    }

    override fun writeFavorites(favorites: List<FavoriteEntity>) {
        writeList(getFavoritesFullName(), favorites)
    }

    override fun writeMetaData(metaData: MetaDataEntity) {
        val sd = File(getMetaDataFullName())
        sd.createNewFile()

        val fOut = FileOutputStream(sd)
        val myOutWriter = OutputStreamWriter(fOut)
        val json = gson.toJson(metaData)
        myOutWriter.write(json)
        myOutWriter.flush()
    }

    override fun writeKeyCloack(keycloakEntity: KeycloakEntity) {
        val sd = File(getKeycloakFullName())
        sd.createNewFile()

        val fOut = FileOutputStream(sd)
        val myOutWriter = OutputStreamWriter(fOut)
        val json = gson.toJson(keycloakEntity)
        myOutWriter.write(json)
        myOutWriter.flush()
    }

    private fun writeList(fileName: String, events: List<Any>) {
        val sd = File(fileName)
        sd.createNewFile()

        val fOut = FileOutputStream(sd)
        val myOutWriter = OutputStreamWriter(fOut)
        val json = gson.toJson(events)
        myOutWriter.write(json)
        myOutWriter.flush()
    }
}

interface ConferenceCacheSerializer {
    fun readSpeakers(): List<SpeakerEntity>
    fun readEvents(): List<EventEntity>
    fun readRooms(): List<RoomEntity>
    fun writeRooms(cachedRooms: List<RoomEntity>)
    fun writeEvents(events: List<EventEntity>)
    fun writeSpeakers(speakers: List<SpeakerEntity>)
    fun readFavorites(): List<FavoriteEntity>
    fun writeFavorites(favorites: List<FavoriteEntity>)
    fun readKeyCloack(): KeycloakEntity
    fun writeKeyCloack(keycloakEntity: KeycloakEntity)
    fun readMetaData(): MetaDataEntity
    fun writeMetaData(metaData: MetaDataEntity)
}
