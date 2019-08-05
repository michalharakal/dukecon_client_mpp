package org.dukecon.android.ui.storage

import android.content.Context
import android.preference.PreferenceManager
import org.dukecon.domain.aspects.storage.ApplicationStorage

class AndroidStorage(context: Context) : ApplicationStorage {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String = sharedPreferences.getString(key, null) ?: ""
}