package org.dukecon.data.cache

import kotlinx.serialization.*

@Serializable
data class CategoryItem(
    val name: String,
    val id: Int,
    val sort: Int
)
