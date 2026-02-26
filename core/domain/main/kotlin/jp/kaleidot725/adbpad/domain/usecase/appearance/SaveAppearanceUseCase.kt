package jp.kaleidot725.adbpad.domain.usecase.appearance

import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class SaveAppearanceUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(appearance: Appearance): Boolean = settingRepository.updateAppearance(appearance)
}
