package org.dukecon.repository.api

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
        val icon: String = "",
        val metaData: MetaData,
        val events: List<Event> = emptyList(),
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
        val defaultIcon: String = ""
)

@Serializable
data class Audience(
        val id: String,
        val order: Int,
        val names: DefinitionsAudienceNames,
        val icon: String = ""
)

@Serializable
data class EventType(
        val id: String,
        val order: Int,
        val names: DefinitionsEventTypeNames,
        val icon: String = ""
)

@Serializable
data class Language(
        val id: String,
        val code: String,
        val order: Int,
        val names: DefinitionsLanguageNames,
        val icon: String = ""
)

@Serializable
data class Track(
        val id: String,
        val order: Int,
        val names: DefinitionsTrackNames,
        val icon: String = ""
)

@Serializable
data class Location(
        val id: String,
        val order: Int,
        val names: DefinitionsLocationNames,
        val icon: String = "",
        val capacity: Int
)

@Serializable
data class Event(
        val id: String,
        val start: String,
        val end: String,
        val title: String,

        val abstractText: String = "",

        val demo: Boolean,
        val simultan: Boolean,
        val veryPopular: Boolean,
        val fullyBooked: Boolean,
        val numberOfFavorites: Int,

        
        val trackId: String = "",

        
        val audienceId: String = "",

        
        val typeId: String = "",

        
        val locationId: String = "",

        
        val speakerIds: List<String> = emptyList(),

        
        val languageId: String = ""
)

@Serializable
data class Speaker(
        val id: String,
        val name: String,
        
        val firstname: String = "",
        
        val lastname: String = "",
        
        val company: String = "",
        
        val email: String = "",
        
        val website: String = "",
        
        val twitter: String = "",
        
        val gplus: String = "",
        
        val facebook: String = "",
        
        val xing: String = "",
        
        val linkedin: String = "",
        
        val bio: String = "",
        
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
data class DefinitionsAudienceNames(val de:String = "", val en:String = "")

// Synthetic class name
@Serializable
data class DefinitionsEventTypeNames(val de:String = "", val en:String = "")

// Synthetic class name
@Serializable
data class DefinitionsLanguageNames(val de:String = "", val en:String = "")

// Synthetic class name
@Serializable
data class DefinitionsTrackNames(val de:String = "", val en:String = "")

// Synthetic class name
@Serializable
data class DefinitionsLocationNames(val de:String = "", val en:String = "")
