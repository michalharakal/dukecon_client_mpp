package org.dukecon.configuration

interface ConferenceConfiguration {
    val baseUrl: String
    val conferenceId: String
    val speakerAvatarUrl: String
    val supportsFeedback: Boolean
}