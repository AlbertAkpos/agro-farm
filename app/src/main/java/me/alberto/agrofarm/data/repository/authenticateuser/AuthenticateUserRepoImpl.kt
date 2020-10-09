package me.alberto.agrofarm.data.repository.authenticateuser

import me.alberto.agrofarm.data.local.UserDataSource
import me.alberto.agrofarm.util.crypto.EncryptedData

class AuthenticateUserRepoImpl(private val localDataSource: UserDataSource.Local) :
    AuthenticateUserRepository {
    override suspend fun getEncryptedData(): EncryptedData? {
        return localDataSource.getEncryptedData()
    }

    override suspend fun setEncryptedData(encryptedData: EncryptedData) {
        localDataSource.setUserEncryptedData(encryptedData)
    }

    override suspend fun isBiometricEnabled(): Boolean {
        return localDataSource.isBiometricEnabled()
    }

    override suspend fun setIsBiometricEnabled(enabled: Boolean) {
        localDataSource.setBiomtricEnabled(enabled)
    }
}