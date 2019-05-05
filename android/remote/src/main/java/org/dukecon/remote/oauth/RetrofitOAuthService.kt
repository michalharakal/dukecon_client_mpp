package org.dukecon.remote.oauth

import org.dukecon.data.model.OAuthToken
import org.dukecon.data.service.OAuthService
import org.dukecon.data.source.OAuthConfiguration
import org.dukecon.oauth.api.code.OauthApi
import org.dukecon.oauth.api.refresh.RefreshOauthApi
import java.io.IOException
import javax.inject.Inject

class RetrofitOAuthService @Inject constructor (
        private val oauthApi: OauthApi,
        private val refreshApi: RefreshOauthApi,
        private val oAuthConfiguration: OAuthConfiguration
) : OAuthService {
    override fun refresh(refreshToken: String): OAuthToken {
        val call = refreshApi.refresh(
                oAuthConfiguration.clientId,
                "refresh_token",
                "dukecon_mobile",
                refreshToken
        )
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val token = response.body()
                    if (token != null) {
                        return OAuthToken(
                                token.accessToken,
                                token.refreshToken,
                                token.expiresIn
                        )
                    }
                }
            }
        } catch (e: IOException) {

        }
        return OAuthToken("", "", 0)
    }

    override fun code2token(code: String): OAuthToken {
        val call = oauthApi.postCode(
                oAuthConfiguration.clientId,
                "authorization_code",
                oAuthConfiguration.redirectUri,
                code
        )
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val token = response.body()
                    if (token != null) {
                        return OAuthToken(
                                token.accessToken,
                                token.refreshToken,
                                token.expiresIn
                        )
                    }
                }
            }
        } catch (e: IOException) {

        }
        return OAuthToken("", "", 0)
    }
}
