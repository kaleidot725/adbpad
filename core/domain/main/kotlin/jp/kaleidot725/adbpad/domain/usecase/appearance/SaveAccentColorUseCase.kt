package jp.kaleidot725.adbpad.domain.usecase.appearance

import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class SaveAccentColorUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(accentColor: AccentColor): Boolean = settingRepository.updateAccentColor(accentColor)
}
