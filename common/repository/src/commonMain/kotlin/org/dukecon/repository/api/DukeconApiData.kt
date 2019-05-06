package org.dukecon.repository.api

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

/*

val api = DukeConAPIClient("https://programm.javaland.eu/2019/rest", client)
    runBlocking<Unit> {
        val conferences = api.getConference("javaland2019")
            println(conferences)
    }
 */

@Serializable
data class Conference(
        val id: String,
        val name: String,
        val url: String,
        val homeUrl: String,
        @Optional
        val icon: String = "",
        val metaData: MetaData,
        @Optional
        val events: List<Event> = emptyList(),
        @Optional
        val speakers: List<Speaker> = emptyList()
)


@Serializable
data class MetaData(
        val id: String,
        val audiences: List<Audience>,
        val eventTypes: List<EventType>,
        val languages: List<Language>,
        val defaultLanguage: Language,
        val tracks: List<Track>,
        val locations: List<Location>,
        @Optional
        val defaultIcon: String = ""
)

@Serializable
data class Audience(
        val id: String,
        val order: Int,
        val names: DefinitionsAudienceNames,
        @Optional
        val icon: String = ""
)

@Serializable
data class EventType(
        val id: String,
        val order: Int,
        val names: DefinitionsEventTypeNames,
        @Optional
        val icon: String = ""
)

@Serializable
data class Language(
        val id: String,
        val code: String,
        val order: Int,
        val names: DefinitionsLanguageNames,
        @Optional
        val icon: String = ""
)

@Serializable
data class Track(
        val id: String,
        val order: Int,
        val names: DefinitionsTrackNames,
        @Optional
        val icon: String = ""
)

@Serializable
data class Location(
        val id: String,
        val order: Int,
        val names: DefinitionsLocationNames,
        @Optional
        val icon: String = "",
        val capacity: Int
)

@Serializable
data class Event(
        val id: String,
        val start: String,
        val end: String,
        val title: String,

        @Optional
        val abstractText: String = "",

        val demo: Boolean,
        val simultan: Boolean,
        val veryPopular: Boolean,
        val fullyBooked: Boolean,
        val numberOfFavorites: Int,

        @Optional
        val trackId: String = "",

        @Optional
        val audienceId: String = "",

        @Optional
        val typeId: String = "",

        @Optional
        val locationId: String = "",

        @Optional
        val speakerIds: List<String> = emptyList(),

        @Optional
        val languageId: String = ""
)

@Serializable
data class Speaker(
        val id: String,
        val name: String,
        @Optional
        val firstname: String = "",
        @Optional
        val lastname: String = "",
        @Optional
        val company: String = "",
        @Optional
        val email: String = "",
        @Optional
        val website: String = "",
        @Optional
        val twitter: String = "",
        @Optional
        val gplus: String = "",
        @Optional
        val facebook: String = "",
        @Optional
        val xing: String = "",
        @Optional
        val linkedin: String = "",
        @Optional
        val bio: String = "",
        @Optional
        val photoId: String = "",
        val eventIds: List<String?>
)

@Serializable
data class Feedback(
        val comment: String,
        val rating: Int
)

@Serializable
data class Keycloak(
        val realm: String,
        val auth_server_url: String,
        val ssl_required: String,
        val resource: String,
        val redirectUri: String,
        val useAccountManagement: String
)

// Synthetic class name
@Serializable
data class DefinitionsAudienceNames(val de:String, val en:String)

// Synthetic class name
@Serializable
data class DefinitionsEventTypeNames(val de:String, val en:String)

// Synthetic class name
@Serializable
data class DefinitionsLanguageNames(val de:String, val en:String)

// Synthetic class name
@Serializable
data class DefinitionsTrackNames(val de:String, val en:String)

// Synthetic class name
@Serializable
data class DefinitionsLocationNames(val de:String, val en:String)
