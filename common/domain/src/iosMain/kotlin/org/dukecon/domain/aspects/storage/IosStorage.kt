package org.dukecon.domain.aspects.storage

import platform.Foundation.NSUserDefaults

actual class ApplicationStorage {
    private val delegate: NSUserDefaults = NSUserDefaults.standardUserDefaults()

    actual fun putBoolean(key: String, value: Boolean) {
        delegate.setBool(value, key)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        if (hasKey(key)) delegate.boolForKey(key) else defaultValue

    actual fun putString(key: String, value: String) {
        delegate.setObject(value, key)
    }

    actual fun getString(key: String): String = delegate.stringForKey(key) ?: ""

    private fun hasKey(key: String): Boolean = delegate.objectForKey(key) != null
}