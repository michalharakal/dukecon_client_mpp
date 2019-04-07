package org.dukecon.remote.oauth.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.dukecon.domain.features.oauth.TokensStorage
import java.io.IOException

class OauthAuthorizationInterceptor(private val tokenStorage: TokensStorage) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStorage.getToken()

        val accessToken = token.accessToken

        return if (accessToken.isNotBlank()) {
            val requestWithAuthorizationHeaderBuilder = chain.request().newBuilder()
            requestWithAuthorizationHeaderBuilder.header(
                "Authorization", "bearer " + accessToken
            )
            chain.proceed(requestWithAuthorizationHeaderBuilder.build())
        } else {
            chain.proceed(chain.request())
        }
    }
}
