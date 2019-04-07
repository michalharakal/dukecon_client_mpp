package org.dukecon.remote.oauth.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.dukecon.data.service.OAuthService
import org.dukecon.domain.features.oauth.TokensStorage
import org.dukecon.remote.oauth.mapper.OAuthTokenMapper
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class TokenRefreshAfterFailInterceptor(
    private val tokenStorage: TokensStorage,
    val oauthService: OAuthService,
    val mapper: OAuthTokenMapper
) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code() == HTTP_UNAUTHORIZED) {
            synchronized(this) {

                val token = tokenStorage.getToken()

                if (token.refreshToken == null || token.refreshToken.isEmpty()) {
                    tokenStorage.clear()
                } else {

                    // refresh
                    val refreshedToken = oauthService.refresh(token.refreshToken)
                    tokenStorage.setToken(mapper.map(refreshedToken))

                    val requestWithRefreshedTokenBuilder = chain.request().newBuilder()
                    requestWithRefreshedTokenBuilder.addHeader("Authorization", refreshedToken.accessToken)

                    chain.proceed(requestWithRefreshedTokenBuilder.build())
                }
            }
        }

        return response
    }
}
