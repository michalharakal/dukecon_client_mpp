package org.dukecon.data.mapper

import org.dukecon.data.model.SpeakerEntity
import org.dukecon.domain.aspects.twitter.TwitterLinks
import org.dukecon.domain.model.Speaker



/**
 * Map a [SpeakerEntity] to and from a [Speaker] instance when data is moving between
 * this later and the Domain layer
 */
open class SpeakerMapper constructor(val twitterLinks: TwitterLinks) : Mapper<SpeakerEntity, Speaker> {

    /**
     * Map a [SpeakerEntity] instance to a [Speaker] instance
     */
    override fun mapFromEntity(type: SpeakerEntity): Speaker {
        return Speaker(type.id, type.name, type.title, twitterLinks.getNormalizedTwitterUrl(type.twitter),
                type.bio, type.website, type.avatar)
    }

    /**
     * Map a [Speaker] instance to a [SpeakerEntity] instance
     */
    override fun mapToEntity(type: Speaker): SpeakerEntity {
        return SpeakerEntity(type.id, type.name, type.title, type.twitter, type.bio, type.website, type.avatar)
    }
}