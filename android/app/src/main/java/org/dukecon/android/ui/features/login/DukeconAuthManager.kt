package org.dukecon.android.ui.features.login

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import org.dukecon.android.ui.features.login.browser.CustomTabActivityHelper
import org.dukecon.android.ui.features.login.browser.WebviewFallback
import org.dukecon.data.service.OAuthService
import org.dukecon.data.source.OAuthConfiguration
import org.dukecon.domain.aspects.auth.AuthManager
import org.dukecon.domain.features.oauth.TokensStorage
import org.dukecon.domain.model.OAuthToken
import org.dukecon.remote.oauth.mapper.OAuthTokenMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DukeconAuthManager @Inject constructor(
        private val oAuthConfiguration: OAuthConfiguration,
        private val oAuthService: OAuthService,
        private val tokensStorage: TokensStorage,
        private val oAuthTokenMapper: OAuthTokenMapper
) : AuthManager {
    override fun hasSession(token: OAuthToken): Boolean {
        return token.refreshToken.isNotEmpty()
    }

    override suspend fun exchangeToken(code: String) {
        val token = oAuthService.code2token(code)
        tokensStorage.setToken(oAuthTokenMapper.map(token))
    }

    override suspend fun login(activity: Any) {
        val uri =
                "${oAuthConfiguration.baseUrl}auth?client_id=${oAuthConfiguration.clientId}&redirect_uri=${oAuthConfiguration.redirectUri}&response_type=code&scope=openid%20offline_access"
        val customTabsIntent = CustomTabsIntent.Builder().build()
        CustomTabActivityHelper.openCustomTab(
                activity as Activity, customTabsIntent, Uri.parse(uri), WebviewFallback()
        )
    }
}
