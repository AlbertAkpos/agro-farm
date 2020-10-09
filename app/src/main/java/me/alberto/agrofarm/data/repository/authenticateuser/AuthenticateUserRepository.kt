package me.alberto.agrofarm.data.repository.authenticateuser

import me.alberto.agrofarm.util.crypto.EncryptedData

interface AuthenticateUserRepository {
    suspend fun getEncryptedData(): EncryptedData?
    suspend fun setEncryptedData(encryptedData: EncryptedData)
    suspend fun isBiometricEnabled(): Boolean
    suspend fun setIsBiometricEnabled(enabled: Boolean)
}