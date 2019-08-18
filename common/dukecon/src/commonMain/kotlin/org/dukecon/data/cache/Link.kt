package org.dukecon.data.cache

import kotlinx.serialization.*

@Serializable
data class Link(
    val linkType: String,
    val title: String,
    val url: String
)
