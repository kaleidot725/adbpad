package jp.kaleidot725.adbpad.domain.usecase.scrcpy

import jp.kaleidot725.adbpad.domain.model.setting.ScrcpySettings
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class SaveScrcpySettingsUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(binaryPath: String): Boolean {
        val scrcpySettings = ScrcpySettings(binaryPath = binaryPath)
        return settingRepository.updateScrcpySettings(scrcpySettings)
    }
}
