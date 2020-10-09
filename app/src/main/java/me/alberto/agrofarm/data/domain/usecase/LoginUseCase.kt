package me.alberto.agrofarm.data.domain.usecase

import me.alberto.agrofarm.data.model.User

class LoginUseCase : UseCase<Unit, User>() {
    override suspend fun buildUseCase(params: User?) {

    }
}