package jp.kaleidot725.adbpad.ui.screen.setting.state

import jp.kaleidot725.pulse.mvi.PulseState
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.ui.screen.setting.model.SettingCategory

data class SettingState(
    val initialized: Boolean = false,
    val selectedCategory: SettingCategory = SettingCategory.APPEARANCE,
    val languages: List<Language.Type> = Language.Type.entries,
    val selectedLanguage: Language.Type = Language.Type.ENGLISH,
    val appearance: Appearance = Appearance.DARK,
    val accentColor: AccentColor = AccentColor.BLUE,
    val adbDirectoryPath: String = "",
    val adbPortNumber: String = "",
    val scrcpyBinaryPath: String = "",
    val isSaving: Boolean = false,
) : PulseState {
    val isValidAdbDirectoryPath: Boolean = adbDirectoryPath.isNotEmpty()
    val isValidAdbPortNumber: Boolean = adbPortNumber.toIntOrNull() != null
    val isValidScrcpyBinaryPath: Boolean = true

    val canSave: Boolean
        get() {
            return isValidAdbDirectoryPath && isValidAdbPortNumber && isValidScrcpyBinaryPath && !isSaving
        }

    val canCancel: Boolean
        get() {
            return !isSaving
        }
}
