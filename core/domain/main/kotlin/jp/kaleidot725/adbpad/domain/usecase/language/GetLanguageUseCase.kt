package jp.kaleidot725.adbpad.domain.usecase.language

import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class GetLanguageUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): Language.Type = settingRepository.getLanguage()
}
