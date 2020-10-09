package me.alberto.agrofarm.data.domain.usecase

import me.alberto.agrofarm.data.local.UserDataSource
import me.alberto.agrofarm.data.model.User

class GetLoginUseCase(private val localDataSource: UserDataSource.Local): UseCase<User, Unit>() {
    override suspend fun buildUseCase(params: Unit?): User {
        localDataSource.getEncryptedData()
    }
}