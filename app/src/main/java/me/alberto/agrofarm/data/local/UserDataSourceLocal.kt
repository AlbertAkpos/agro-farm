package me.alberto.agrofarm.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import me.alberto.agrofarm.util.crypto.EncryptedData

class UserDataSourceLocal(pref: SharedPreferences, private val gson: Gson) :
    BasePreferencesManager(pref), UserDataSource.Local {
    override suspend fun setUserEncryptedData(encryptedData: EncryptedData) {
        val encryptedDataString = gson.toJson(encryptedData)
        setStringPreference(ENCRYPTED_DATA_PREF_KEY, encryptedDataString)
    }

    override suspend fun getEncryptedData(): EncryptedData? {
        val encryptedDataString = getStringPreference(ENCRYPTED_DATA_PREF_KEY)
        return gson.fromJson(encryptedDataString, EncryptedData::class.java)
    }

    override suspend fun isBiometricEnabled(): Boolean {
        return getBooleanPreference(BIOMETRIC_ENABLED, false)
    }

    override suspend fun setBiomtricEnabled(enabled: Boolean) {
        setBooleanPreference(BIOMETRIC_ENABLED, enabled)
    }

    companion object {
        const val ENCRYPTED_DATA_PREF_KEY = "encrypted_data_pref_keys"
        const val BIOMETRIC_ENABLED = "biometric_enabled"
    }
}