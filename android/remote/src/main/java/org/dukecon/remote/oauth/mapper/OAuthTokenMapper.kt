package org.dukecon.remote.oauth.mapper

import org.dukecon.data.model.OAuthToken
import org.dukecon.domain.features.time.CurrentTimeProvider
import javax.inject.Inject

class OAuthTokenMapper @Inject constructor(val currentTimeProvider: CurrentTimeProvider) {
    fun map(token: OAuthToken): org.dukecon.domain.model.OAuthToken {
        return org.dukecon.domain.model.OAuthToken(
            token.accessToken,
            token.refreshToken,
            currentTimeProvider.currentTimeMillis() + token.exppiresIn
        )
    }
}