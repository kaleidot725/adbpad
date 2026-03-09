package jp.kaleidot725.adbpad.ui.screen.setting

import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.domain.usecase.adb.RestartAdbUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.GetAccentColorUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.GetAppearanceUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.SaveAccentColorUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.SaveAppearanceUseCase
import jp.kaleidot725.adbpad.domain.usecase.language.GetLanguageUseCase
import jp.kaleidot725.adbpad.domain.usecase.language.SaveLanguageUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.GetScrcpySettingsUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.SaveScrcpySettingsUseCase
import jp.kaleidot725.adbpad.domain.usecase.sdkpath.GetSdkPathUseCase
import jp.kaleidot725.adbpad.domain.usecase.sdkpath.SaveSdkPathUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.setting.state.SettingAction
import jp.kaleidot725.adbpad.ui.screen.setting.state.SettingSideEffect
import jp.kaleidot725.adbpad.ui.screen.setting.state.SettingState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.launch

class SettingStateHolder(
    private val getSdkPathUseCase: GetSdkPathUseCase,
    private val saveSdkPathUseCase: SaveSdkPathUseCase,
    private val getAppearanceUseCase: GetAppearanceUseCase,
    private val saveAppearanceUseCase: SaveAppearanceUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getScrcpySettingsUseCase: GetScrcpySettingsUseCase,
    private val saveScrcpySettingsUseCase: SaveScrcpySettingsUseCase,
    private val restartAdbUseCase: RestartAdbUseCase,
    private val getAccentColorUseCase: GetAccentColorUseCase,
    private val saveAccentColorUseCase: SaveAccentColorUseCase,
) : PulseStore<SettingState, SettingAction, SettingSideEffect, AppBroadCast>(initialUiState = SettingState()) {
    private var oldAdbDirectoryPath: String = ""
    private var oldAdbPortNumber: Int = 0

    override fun onSetup() {
        coroutineScope.launch {
            val language = getLanguageUseCase()
            val appearance = getAppearanceUseCase()
            val accentColor = getAccentColorUseCase()
            val sdkPath = getSdkPathUseCase()
            val scrcpySettings = getScrcpySettingsUseCase()
            val adbDirectoryPath = sdkPath.adbDirectory
            val adbPortNumber = sdkPath.adbServerPort.toString()
            val scrcpyBinaryPath = scrcpySettings.binaryPath

            oldAdbDirectoryPath = sdkPath.adbDirectory
            oldAdbPortNumber = sdkPath.adbServerPort

            update {
                this.copy(
                    selectedLanguage = language,
                    appearance = appearance,
                    accentColor = accentColor,
                    adbDirectoryPath = adbDirectoryPath,
                    adbPortNumber = adbPortNumber,
                    scrcpyBinaryPath = scrcpyBinaryPath,
                    initialized = true,
                )
            }
        }
    }

    override fun onAction(uiAction: SettingAction) {
        coroutineScope.launch {
            when (uiAction) {
                SettingAction.Save -> save()
                is SettingAction.SelectCategory -> selectCategory(uiAction.category)
                is SettingAction.UpdateAdbDirectoryPath -> updateAdbDirectoryPath(uiAction.value)
                is SettingAction.UpdateAdbPortNumberPath -> updateAdbPortNumberPath(uiAction.value)
                is SettingAction.UpdateScrcpyBinaryPath -> updateScrcpyBinaryPath(uiAction.value)
                is SettingAction.UpdateAppearance -> updateAppearance(uiAction.value)
                is SettingAction.UpdateLanguage -> updateLanguage(uiAction.value)
                is SettingAction.UpdateAccentColor -> updateAccentColor(uiAction.value)
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {}
        }
    }

    private suspend fun save() {
        update { this.copy(isSaving = true) }
        saveLanguageUseCase(currentState.selectedLanguage)
        saveAppearanceUseCase(currentState.appearance)
        saveAccentColorUseCase(currentState.accentColor)
        saveSdkPathUseCase(currentState.adbDirectoryPath, currentState.adbPortNumber.toIntOrNull())
        saveScrcpySettingsUseCase(currentState.scrcpyBinaryPath)
        restartAdbUseCase(oldAdbDirectory = oldAdbDirectoryPath, oldServerPort = oldAdbPortNumber)
        event(SettingSideEffect.Saved)
        update { this.copy(isSaving = false) }
    }

    private fun updateLanguage(value: Language.Type) {
        update { this.copy(selectedLanguage = value) }
    }

    private fun updateAppearance(value: Appearance) {
        update { this.copy(appearance = value) }
    }

    private fun updateAdbDirectoryPath(value: String) {
        update { this.copy(adbDirectoryPath = value) }
    }

    private fun updateAdbPortNumberPath(value: String) {
        update { this.copy(adbPortNumber = value) }
    }

    private fun updateScrcpyBinaryPath(value: String) {
        update { this.copy(scrcpyBinaryPath = value) }
    }

    private fun updateAccentColor(value: AccentColor) {
        update { this.copy(accentColor = value) }
    }

    private fun selectCategory(category: jp.kaleidot725.adbpad.ui.screen.setting.model.SettingCategory) {
        update { this.copy(selectedCategory = category) }
    }
}
