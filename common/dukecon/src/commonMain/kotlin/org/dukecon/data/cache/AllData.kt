package org.dukecon.data.cache

import kotlinx.serialization.*
import org.jetbrains.kotlinconf.data.*

// This format is enforced by Sessionize and it should not be changed unless we extract Sessionize DTO
@Serializable
data class AllData(
    val sessions: List<Session> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val speakers: List<Speaker> = emptyList(),
    val questions: List<Question> = emptyList(),
    val categories: List<Category> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val votes: List<Vote> = emptyList()
)

class SessionizeData(val allData: AllData, val etag: String = allData.hashCode().toString())
