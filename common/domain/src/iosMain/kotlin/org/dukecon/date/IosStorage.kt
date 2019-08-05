package org.dukecon.date

import org.dukecon.domain.aspects.storage.ApplicationStorage
import platform.Foundation.NSUserDefaults

class IosStorage : ApplicationStorage {
    private val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults()

    override fun putBoolean(key: String, value: Boolean) {
        delegate.setBool(value, key)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        if (hasKey(key)) delegate.boolForKey(key) else defaultValue

    override fun putString(key: String, value: String) {
        delegate.setObject(value, key)
    }

    override fun getString(key: String): String = delegate.stringForKey(key) ?: ""

    private fun hasKey(key: String): Boolean = delegate.objectForKey(key) != null
}