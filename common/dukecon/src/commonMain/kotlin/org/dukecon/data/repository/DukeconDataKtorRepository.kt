package org.dukecon.data.repository

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import kotlinx.serialization.serializer
import org.dukecon.data.api.DukeconApi
import org.dukecon.data.api.Event
import org.dukecon.data.cache.SessionModel
import org.dukecon.data.cache.SpeakerModel
import org.dukecon.date.GMTDate
import org.dukecon.domain.aspects.storage.ApplicationStorage
import org.dukecon.domain.model.*
import org.dukecon.domain.repository.ConferenceRepository
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty

class DukeconDataKtorRepository(
        endPoint: String,
        private val conferenceId: String,
        private val settings: ApplicationStorage
) : ConferenceRepository {
    override fun update(completion: (String) -> Unit) {
        MainScope().launch {
            update()
            completion("")
        }
    }

    override fun getEvents(): List<org.dukecon.domain.model.Event> {
        return if (sessions != null) {
            sessions!!.map {
                toEvent(it)
            }
                    .sortedBy { it.startTime }
        } else {
            emptyList()
        }
    }

    //private val api = DukeconApi("https://programm.javaland.eu/2019/rest", "javaland2019")
    // private val api = DukeconApi("https://www.apachecon.com/acna19/s/rest/", "acna2019.json")
    private val api = DukeconApi(endPoint, conferenceId)

    var sessions: List<SessionModel>? by bindToPreferencesByKey("sessions", SessionModel.serializer().list)
    var speakers: List<SpeakerModel>? by bindToPreferencesByKey("speakers", SpeakerModel.serializer().list)
    //var favorites: List<SessionModel>? by bindToPreferencesByKey("favoritesKey", SessionModel.serializer().list)
    //var votes: List<Vote>? by bindToPreferencesByKey("votesKey", Vote.serializer().list)
    var userId: String? by bindToPreferencesByKey("userIdKey", String.serializer())


    override var onRefreshListeners: List<() -> Unit> = emptyList()

    override suspend fun getEventDates(): List<GMTDate> {
        return sessions!!.map {
            toEvent(it)
        }.distinctBy { it.startTime.dayOfMonth }
                .map {
                    it.startTime
                }.sortedBy { it.dayOfMonth }
    }

    override suspend fun getEvents(day: Int): List<org.dukecon.domain.model.Event> {
        return if (sessions != null) {
            sessions!!.filter { it.startsAt.dayOfMonth == day }
                    .map {
                        toEvent(it)
                    }
                    .sortedBy { it.startTime }
        } else {
            emptyList()
        }
    }

    override suspend fun getEvent(id: String): org.dukecon.domain.model.Event? {
        return if (sessions != null) {
            toEvent(sessions!!.first { it.id == id })
        } else {
            null
        }
    }

    override suspend fun getSpeakers(): List<Speaker> {
        return speakers!!.map {
            toSpeaker(it)
        }.sortedBy { it.name }
    }

    override suspend fun getSpeaker(id: String): Speaker? {
        return speakers!!.find { it.id == id }?.let { toSpeaker(it) }
    }

    override suspend fun saveEvents(events: List<org.dukecon.domain.model.Event>) {

    }


    private fun toSpeaker(it: SpeakerModel): Speaker {
        return Speaker(id = it.id,
                name = it.fullName,
                title = it.tagLine,
                twitter = "",
                bio = it.bio,
                website = "",
                avatar = "")
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


    override suspend fun update() {
        try {
            val conference = api.getConference(conferenceId)
            sessions = conference.events.map {
                toSessionModel(it)
            }
            speakers = conference.speakers.map {
                toSpeakernModel(it)
            }

        } catch (source: Throwable) {
            println(source.toString())
        }
    }

    private fun toSpeakernModel(it: org.dukecon.data.api.Speaker): SpeakerModel {
        return SpeakerModel(
                id = it.id,
                firstName = it.firstname,
                lastName = it.lastname,
                profilePicture = it.photoId,
                sessions = emptyList(),
                tagLine = "",
                isTopSpeaker = true,
                bio = it.bio,
                fullName = it.name,
                links = emptyList())
        // listOf(it.twitter, it.linkedin, it.facebook, it.website, it.xing)
    }

    private fun toSessionModel(it: Event): SessionModel {
        return SessionModel(it.id, it.title, it.typeId, it.abstractText, it.start, it.end, it.locationId, emptyList())
    }

    private fun toEvent(it: SessionModel): org.dukecon.domain.model.Event {
        return Event(it.id,
                it.title,
                it.descriptionText,
                it.startsAt ?: GMTDate(),
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

    private inline fun <reified T : Any> read(key: String, elementSerializer: KSerializer<T>) = settings
            .getString(key)
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