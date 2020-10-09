package me.alberto.agrofarm.data.domain.usecase

import me.alberto.agrofarm.data.model.User

class SetLoginUseCase: UseCase<Unit, User>() {
    override suspend fun buildUseCase(params: User?) {

    }
}