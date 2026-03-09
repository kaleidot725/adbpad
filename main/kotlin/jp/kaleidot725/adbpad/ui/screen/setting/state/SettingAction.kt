package jp.kaleidot725.adbpad.ui.screen.setting.state

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.ui.screen.setting.model.SettingCategory

sealed class SettingAction : PulseAction {
    data object Save : SettingAction()

    data class SelectCategory(
        val category: SettingCategory,
    ) : SettingAction()

    data class UpdateLanguage(
        val value: Language.Type,
    ) : SettingAction()

    data class UpdateAppearance(
        val value: Appearance,
    ) : SettingAction()

    data class UpdateAdbDirectoryPath(
        val value: String,
    ) : SettingAction()

    data class UpdateAdbPortNumberPath(
        val value: String,
    ) : SettingAction()

    data class UpdateScrcpyBinaryPath(
        val value: String,
    ) : SettingAction()

    data class UpdateAccentColor(
        val value: AccentColor,
    ) : SettingAction()
}
