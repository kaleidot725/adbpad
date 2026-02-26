package jp.kaleidot725.adbpad.domain.usecase.scrcpy

import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProfileRepository

class DeleteScrcpyNewDisplayProfileUseCase(
    private val repository: ScrcpyNewDisplayProfileRepository,
) {
    suspend operator fun invoke(profileId: String) {
        repository.removeUserProfile(profileId)
    }
}
