package me.alberto.agrofarm.data.local

import android.content.SharedPreferences

abstract class BasePreferencesManager(private val sharedPreferences: SharedPreferences) {

    protected val defaultStringValue = "{}"
    protected val defaultLongValue = -1L
    protected val defaultIntValue = -1

    protected fun setStringPreference(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    protected fun getStringPreference(
        key: String,
        defaultValue: String = defaultStringValue
    ): String? {
        return sharedPreferences.getString(key, null)
    }

    protected fun setIntPreference(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    protected fun getIntPreference(key: String, defaultValue: Int = defaultIntValue): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }


    protected fun setLongPreference(key: String, value: Long) {
        sharedPreferences.edit()
            .putLong(key, value)
            .apply()
    }

    protected fun getLongPreference(key: String, defaultValue: Long = defaultLongValue): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    protected fun getBooleanPreference(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    protected fun setBooleanPreference(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    protected fun resetPreferences(): Boolean {
        sharedPreferences.edit().clear().apply()
        return true
    }

    protected fun removePreference(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }
}