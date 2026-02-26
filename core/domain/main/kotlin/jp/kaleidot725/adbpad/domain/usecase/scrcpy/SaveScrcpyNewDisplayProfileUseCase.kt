package jp.kaleidot725.adbpad.domain.usecase.scrcpy

import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile
import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProfileRepository

class SaveScrcpyNewDisplayProfileUseCase(
    private val repository: ScrcpyNewDisplayProfileRepository,
) {
    suspend operator fun invoke(profile: ScrcpyNewDisplayProfile) {
        repository.addUserProfile(profile)
    }
}
