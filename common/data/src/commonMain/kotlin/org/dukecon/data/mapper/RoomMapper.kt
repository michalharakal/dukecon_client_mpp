package org.dukecon.data.mapper

import org.dukecon.data.model.RoomEntity
import org.dukecon.data.model.SpeakerEntity
import org.dukecon.domain.model.Room
import org.dukecon.domain.model.Speaker



/**
 * Map a [RoomEntity] to and from a [Speaker] instance when data is moving between
 * this later and the Domain layer
 */
open class RoomMapper constructor() : Mapper<RoomEntity, Room> {

    /**
     * Map a [RoomEntity] instance to a [Speaker] instance
     */
    override fun mapFromEntity(type: RoomEntity): Room {
        return Room(type.id, type.name)
    }

    /**
     * Map a [Speaker] instance to a [RoomEntity] instance
     */
    override fun mapToEntity(type: Room): RoomEntity {
        return RoomEntity(type.id, type.name)
    }
}