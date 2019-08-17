package org.dukecon.domain.aspects.storage

actual class ApplicationStorage {
    actual fun putBoolean(key: String, value: Boolean) {

    }
    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean    {
        return true;
    }
    actual fun putString(key: String, value: String) {

    }
    actual fun getString(key: String): String {
        return "";
    }
}