package me.alberto.agrofarm.data.local

import me.alberto.agrofarm.util.crypto.EncryptedData

interface UserDataSource {
    interface Local {
        suspend fun setUserEncryptedData(encryptedData: EncryptedData)
        suspend fun getEncryptedData(): EncryptedData?
        suspend fun isBiometricEnabled(): Boolean
        suspend fun setBiomtricEnabled(enabled: Boolean)
    }
}