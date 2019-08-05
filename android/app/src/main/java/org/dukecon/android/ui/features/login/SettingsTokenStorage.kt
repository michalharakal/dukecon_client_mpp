package org.dukecon.android.ui.features.login

import org.dukecon.domain.aspects.storage.ApplicationStorage
import org.dukecon.domain.features.oauth.TokensStorage
import org.dukecon.domain.model.OAuthToken
import javax.inject.Inject

class SettingsTokenStorage @Inject constructor(private val settings: ApplicationStorage) : TokensStorage {
    override fun clear() {
        //settings.clear("token")
    }

    override fun getToken(): OAuthToken? {
        return settings.getString("token")?.toOAuthToken()
    }

    override fun loginRequired(): Boolean {
        return true
    }

    override fun setToken(refreshedToken: OAuthToken) {
    }
}

private fun String.toOAuthToken(): OAuthToken {
    return OAuthToken("", "", 0)
}
