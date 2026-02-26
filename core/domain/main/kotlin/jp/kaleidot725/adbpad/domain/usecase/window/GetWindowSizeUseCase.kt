package jp.kaleidot725.adbpad.domain.usecase.window

import jp.kaleidot725.adbpad.domain.model.setting.WindowSize
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class GetWindowSizeUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): WindowSize = settingRepository.getWindowSize()
}
