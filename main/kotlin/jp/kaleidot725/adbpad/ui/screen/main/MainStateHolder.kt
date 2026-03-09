package jp.kaleidot725.adbpad.ui.screen.main

import jp.kaleidot725.adbpad.domain.model.MainCategory
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.WindowSize
import jp.kaleidot725.adbpad.domain.usecase.adb.StartAdbUseCase
import jp.kaleidot725.adbpad.domain.usecase.app.ShutdownAppUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.GetAccentColorUseCase
import jp.kaleidot725.adbpad.domain.usecase.language.GetLanguageUseCase
import jp.kaleidot725.adbpad.domain.usecase.refresh.RefreshUseCase
import jp.kaleidot725.adbpad.domain.usecase.theme.GetDarkModeFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.window.GetWindowSizeUseCase
import jp.kaleidot725.adbpad.domain.usecase.window.SaveWindowSizeUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.main.state.MainAction
import jp.kaleidot725.adbpad.ui.screen.main.state.MainDialog
import jp.kaleidot725.adbpad.ui.screen.main.state.MainSideEffect
import jp.kaleidot725.adbpad.ui.screen.main.state.MainState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainStateHolder(
    private val getWindowSizeUseCase: GetWindowSizeUseCase,
    private val saveWindowSizeUseCase: SaveWindowSizeUseCase,
    private val startAdbUseCase: StartAdbUseCase,
    private val getDarkModeFlowUseCase: GetDarkModeFlowUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getAccentColorUseCase: GetAccentColorUseCase,
    private val refreshUseCase: RefreshUseCase,
    private val shutdownAppUseCase: ShutdownAppUseCase,
) : PulseStore<MainState, MainAction, MainSideEffect, AppBroadCast>(initialUiState = MainState()) {
    override fun onSetup() {
        restoreWindowSize()
        startSyncDarkMode()
        checkAdbServer()
        syncLanguage()
        syncAccentColor()
    }

    override fun onAction(uiAction: MainAction) {
        when (uiAction) {
            is MainAction.OpenSetting -> openSetting()
            is MainAction.OpenDeviceSettings -> openDeviceSettings(uiAction.device)
            is MainAction.SaveSetting -> saveSetting(uiAction.windowSize)
            is MainAction.ClickCategory -> clickCategory(uiAction.category)
            is MainAction.ToggleAlwaysOnTop -> toggleAlwaysOnTop()
            is MainAction.ToggleNavigationRail -> toggleNavigationRail()
            is MainAction.Shutdown -> shutdown()
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {
                startSyncDarkMode()
                checkAdbServer()
                syncLanguage()
                syncAccentColor()
                refreshUseCase()
            }
        }
    }

    private fun toggleNavigationRail() {
        update { copy(isNavigationRailCollapsed = !isNavigationRailCollapsed) }
    }

    private fun saveSetting(windowSize: WindowSize) {
        saveWindowSize(windowSize)
    }

    private fun openSetting() {
        update { copy(dialog = MainDialog.Setting) }
    }

    private fun openDeviceSettings(device: Device) {
        update { copy(dialog = MainDialog.DeviceSettings(device)) }
    }

    private fun clickCategory(category: MainCategory) {
        update { copy(category = category) }
    }

    private fun toggleAlwaysOnTop() {
        update { copy(isAlwaysOnTop = !isAlwaysOnTop) }
    }

    private fun shutdown() {
        // Terminate all running Scrcpy processes before app shutdown
        shutdownAppUseCase()

        // Cancel any ongoing coroutines
        themeFlowJob?.cancel()
    }

    private var themeFlowJob: Job? = null

    private fun startSyncDarkMode() {
        themeFlowJob?.cancel()
        themeFlowJob =
            coroutineScope.launch {
                getDarkModeFlowUseCase().collectLatest { update { copy(isDark = it) } }
            }
    }

    private fun saveWindowSize(windowSize: WindowSize) {
        coroutineScope.launch {
            saveWindowSizeUseCase(windowSize)
        }
    }

    private fun restoreWindowSize() {
        coroutineScope.launch {
            val size = getWindowSizeUseCase()
            update { copy(size = size) }
        }
    }

    private fun checkAdbServer() {
        coroutineScope.launch {
            val dialog = if (startAdbUseCase()) MainDialog.Empty else MainDialog.AdbError
            update { copy(dialog = dialog) }
        }
    }

    private fun syncLanguage() {
        coroutineScope.launch {
            val type = getLanguageUseCase()
            Language.switch(type)
            update { copy(language = type) }
        }
    }

    private fun syncAccentColor() {
        coroutineScope.launch {
            val accentColor = getAccentColorUseCase()
            update { copy(accentColor = accentColor) }
        }
    }
}
