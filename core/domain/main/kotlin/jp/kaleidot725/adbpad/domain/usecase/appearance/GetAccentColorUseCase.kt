package jp.kaleidot725.adbpad.domain.usecase.appearance

import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class GetAccentColorUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): AccentColor = settingRepository.getAccentColor()
}
