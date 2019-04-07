package org.dukecon.android.cache

import android.content.Context
import android.content.SharedPreferences
import org.dukecon.domain.features.oauth.TokensStorage
import org.dukecon.domain.model.OAuthToken

class SharedPreferencesTokenStorage constructor(context: Context) : TokensStorage {

    override fun loginRequired(): Boolean {
        val token = getToken()
        return token.refreshToken.isNullOrEmpty()
    }

    companion object {
        private val PREF_BUFFER_PACKAGE_NAME = "org.dukecon.ouath"
        private val PREF_KEY_ACCESS_TOKEN = "access_token"
        private val PREF_KEY_REFRESH_TOKEN = "refresh_token"
        private val PREF_KEY_EXPIRES_AT = "expires_at"
    }

    private val oauthTokenPref: SharedPreferences

    init {
        oauthTokenPref = context.getSharedPreferences(PREF_BUFFER_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    override fun getToken(): OAuthToken {
        val access = oauthTokenPref.getString(PREF_KEY_ACCESS_TOKEN, "")
        val refresh = oauthTokenPref.getString(PREF_KEY_REFRESH_TOKEN, "")
        val expiresAt = oauthTokenPref.getLong(PREF_KEY_EXPIRES_AT, 0)
        return OAuthToken(access!!, refresh!!, expiresAt)
    }

    override fun clear() {
        oauthTokenPref.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()
        oauthTokenPref.edit().remove(PREF_KEY_REFRESH_TOKEN).apply()
        oauthTokenPref.edit().remove(PREF_KEY_EXPIRES_AT).apply()
    }

    override fun setToken(refreshedToken: OAuthToken) {
        oauthTokenPref.edit().putString(PREF_KEY_ACCESS_TOKEN, refreshedToken.accessToken).apply()
        oauthTokenPref.edit().putString(PREF_KEY_REFRESH_TOKEN, refreshedToken.refreshToken).apply()
        oauthTokenPref.edit().putLong(PREF_KEY_EXPIRES_AT, refreshedToken.expiresAt).apply()
    }
}