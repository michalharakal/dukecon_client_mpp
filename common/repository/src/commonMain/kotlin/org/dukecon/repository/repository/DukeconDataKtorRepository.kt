package org.dukecon.common.data

import io.ktor.util.date.GMTDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import org.dukecon.repository.api.DukeconApi
import org.dukecon.repository.api.Event
import org.dukecon.repository.cache.SessionModel
import org.dukecon.data.cache.Settings
import org.dukecon.domain.model.*
import org.dukecon.domain.repository.ConferenceRepository
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty


class DukeconDataKtorRepository(
        endPoint: String,
        uid: String,
        private val settings: Settings
) : ConferenceRepository {
    override suspend fun saveEvents(events: List<org.dukecon.domain.model.Event>) {

    }

    override suspend fun getEvents(day: Int): List<org.dukecon.domain.model.Event> {
        return if (sessions != null) {
            sessions!!.map {
                toEvent(it)
            }
        } else {
            emptyList()
        }
    }

    override suspend fun getEventDates(): List<GMTDate> {
        return listOf(GMTDate())

    }

    override suspend fun getSpeakers(): List<Speaker> {
        return emptyList()
    }

    override suspend fun getSpeaker(id: String): Speaker {
        return Speaker("1", "","", "", "", "", "")
    }

    override suspend fun saveSpeakers(speakers: List<Speaker>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getRooms(): List<Room> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveRooms(rooms: List<Room>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getEvent(id: String): org.dukecon.domain.model.Event {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveFavorite(favorite: Favorite): List<Favorite> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getFavorites(): List<Favorite> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun submitFeedback(feedback: Feedback): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getKeyCloak(): Keycloak {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getMetaData(): MetaData {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val api = DukeconApi("https://programm.javaland.eu/2019/rest", "javaland2019")

    var sessions: List<SessionModel>? by bindToPreferencesByKey("settingsKey", SessionModel.serializer().list)
    //var favorites: List<SessionModel>? by bindToPreferencesByKey("favoritesKey", SessionModel.serializer().list)
    //var votes: List<Vote>? by bindToPreferencesByKey("votesKey", Vote.serializer().list)
    var userId: String? by bindToPreferencesByKey("userIdKey", String.serializer())

    init {
        userId = "javaland2019"
    }

    override var onRefreshListeners: List<() -> Unit> = emptyList()


    override suspend fun update() {
        try {
            val conference = api.getConference("javaland2019")
            sessions = conference.events.map {
                toSessionModel(it)
            }

        } catch (source: Throwable) {
            println(source.toString())
        }
    }

    private fun toSessionModel(it: Event): SessionModel {
        return SessionModel(it.id, it.title, it.typeId, it.abstractText, it.start, it.end, it.locationId, emptyList())
    }

    private fun toEvent(it: SessionModel ):org.dukecon.domain.model.Event  {
        return Event(it.id, it.title, it.descriptionText, it.startsAt ?: GMTDate(),
                it.endsAt ?: GMTDate(),
                emptyList(),
                Favorite("1", 1, false),
                Location("", 0, emptyMap(), "", 100),
                Track("1", 0, emptyMap(), ""),
                Audience("1", 0, emptyMap(), ""),
                EventType("1", 0, emptyMap(), ""),
                true,
                Language("1", "", 0, emptyMap(), ""),
                false,
                false,
                false,
                0)
    }

    /*
    override fun getRating(sessionId: String): SessionRating? {
        return SessionRating.OK
    }

    override suspend fun addRating(sessionId: String, rating: SessionRating) {
    }

    override suspend fun removeRating(sessionId: String) {
    }

    override suspend fun setFavorite(sessionId: String, isFavorite: Boolean) {
    }

    override fun acceptPrivacyPolicy() {
    }
*/

    private inline fun <reified T : Any> read(key: String, elementSerializer: KSerializer<T>) = settings
            .getString(key, "")
            .takeUnless { it.isBlank() }
            ?.let {
                try {
                    Json.parse(elementSerializer, it)
                } catch (_: Throwable) {
                    null
                }
            }

    private inline fun <reified T : Any> write(key: String, obj: T?, elementSerializer: KSerializer<T>) {
        settings.putString(key, if (obj == null) "" else Json.stringify(elementSerializer, obj))
    }

    private inline fun <reified T : Any> bindToPreferencesByKey(
            key: String,
            elementSerializer: KSerializer<T>
    ): ReadWriteProperty<Any?, T?> = Delegates.observable(read(key, elementSerializer)) { _, _, new ->
        write(key, new, elementSerializer)
    }


}