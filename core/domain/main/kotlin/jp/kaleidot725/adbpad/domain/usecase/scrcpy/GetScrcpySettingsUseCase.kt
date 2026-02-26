package jp.kaleidot725.adbpad.domain.usecase.scrcpy

import jp.kaleidot725.adbpad.domain.model.setting.ScrcpySettings
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class GetScrcpySettingsUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): ScrcpySettings = settingRepository.getScrcpySettings()
}
