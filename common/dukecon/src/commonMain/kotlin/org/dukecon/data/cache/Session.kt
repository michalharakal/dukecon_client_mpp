package org.dukecon.data.cache

import kotlinx.serialization.*
import org.jetbrains.kotlinconf.data.QuestionAnswer

// This format is enforced by Sessionize and it should not be changed unless we extract Sessionize DTO
@Serializable
data class Session(
        val id: String,
        val isServiceSession: Boolean,
        val isPlenumSession: Boolean,
        val questionAnswers: List<QuestionAnswer>,
        val speakers: List<String>,
        @SerialName("description")
    val descriptionText: String?,
        val startsAt: String?,
        val title: String,
        val endsAt: String?,
        val categoryItems: List<Int>,
        val roomId: Int?
)
