package jp.kaleidot725.adbpad.domain.usecase.language

import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class SaveLanguageUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(language: Language.Type): Boolean = settingRepository.updateLanguage(language)
}
