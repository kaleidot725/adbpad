package jp.kaleidot725.adbpad

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.application
import jp.kaleidot725.adbpad.di.domainModule
import jp.kaleidot725.adbpad.di.repositoryModule
import jp.kaleidot725.adbpad.di.stateHolderModule
import jp.kaleidot725.adbpad.domain.model.setting.WindowSize
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.container.AppContainer
import jp.kaleidot725.adbpad.ui.screen.CommandScreen
import jp.kaleidot725.adbpad.ui.screen.command.CommandStateHolder
import jp.kaleidot725.adbpad.ui.screen.device.DeviceSettingsScreen
import jp.kaleidot725.adbpad.ui.screen.device.DeviceSettingsStateHolder
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsAction
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsSideEffect
import jp.kaleidot725.adbpad.ui.screen.main.MainScreen
import jp.kaleidot725.adbpad.ui.screen.main.MainStateHolder
import jp.kaleidot725.adbpad.ui.screen.main.state.MainAction
import jp.kaleidot725.adbpad.ui.screen.main.state.MainDialog
import jp.kaleidot725.adbpad.ui.screen.newdisplay.ScrcpyNewDisplayScreen
import jp.kaleidot725.adbpad.ui.screen.newdisplay.ScrcpyNewDisplayStateHolder
import jp.kaleidot725.adbpad.ui.screen.screenshot.ScreenshotScreen
import jp.kaleidot725.adbpad.ui.screen.screenshot.ScreenshotStateHolder
import jp.kaleidot725.adbpad.ui.screen.setting.SettingScreen
import jp.kaleidot725.adbpad.ui.screen.setting.SettingStateHolder
import jp.kaleidot725.adbpad.ui.screen.setting.state.SettingSideEffect
import jp.kaleidot725.adbpad.ui.screen.text.TextCommandScreen
import jp.kaleidot725.adbpad.ui.screen.text.TextCommandStateHolder
import jp.kaleidot725.adbpad.ui.section.top.TopSection
import jp.kaleidot725.adbpad.ui.section.top.TopStateHolder
import jp.kaleidot725.pulse.mvi.PulseApp
import jp.kaleidot725.pulse.mvi.PulseContent
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

@OptIn(ExperimentalSplitPaneApi::class)
fun main() {
    startKoin { modules(repositoryModule, domainModule, stateHolderModule) }
    application {
        val mainStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<MainStateHolder>()) }
        val commandStateHolder: CommandStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<CommandStateHolder>()) }
        val textCommandStateHolder: TextCommandStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<TextCommandStateHolder>()) }
        val screenshotStateHolder: ScreenshotStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<ScreenshotStateHolder>()) }
        val scrcpyNewDisplayStateHolder: ScrcpyNewDisplayStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<ScrcpyNewDisplayStateHolder>()) }
        val topStateHolder: TopStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<TopStateHolder>()) }
        val deviceSettingsStateHolder: DeviceSettingsStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<DeviceSettingsStateHolder>()) }
        val settingStateHolder: SettingStateHolder
            by remember { mutableStateOf(GlobalContext.get().get<SettingStateHolder>()) }
        val appContainer by remember {
            mutableStateOf(
                AppContainer(
                    stores =
                        listOf(
                            mainStateHolder,
                            commandStateHolder,
                            textCommandStateHolder,
                            screenshotStateHolder,
                            scrcpyNewDisplayStateHolder,
                            topStateHolder,
                            deviceSettingsStateHolder,
                            settingStateHolder,
                        ),
                ),
            )
        }
        PulseApp(appContainer) { _, onBroadcast ->
            PulseContent(store = mainStateHolder) { state, onAction ->
                if (state.size == WindowSize.UNKNOWN) return@PulseContent
                if (state.isDark == null) return@PulseContent
                MainScreen(
                    state = state,
                    onAction = onAction,
                    onRefresh = { onBroadcast(AppBroadCast.Refresh) },
                    onExitApplication = { exitApplication() },
                    topContent = {
                        PulseContent(
                            store = topStateHolder,
                            content = { topState, onTopAction ->
                                TopSection(
                                    topState = topState,
                                    onTopAction = onTopAction,
                                    onOpenDeviceSettings = { device -> onAction(MainAction.OpenDeviceSettings(device)) },
                                    onRefreshDevices = { onBroadcast(AppBroadCast.Refresh) },
                                    onToggleNavigationRail = { onAction(MainAction.ToggleNavigationRail) },
                                )
                            },
                        )
                    },
                    commandContent = { splitterState ->
                        PulseContent(
                            store = commandStateHolder,
                            content = { state, onAction ->
                                CommandScreen(
                                    state = state,
                                    onAction = onAction,
                                    splitterState = splitterState,
                                )
                            },
                        )
                    },
                    textCommandContent = { splitterState, rightSplitterState ->
                        PulseContent(
                            store = textCommandStateHolder,
                            content = { state, onAction ->
                                TextCommandScreen(
                                    state = state,
                                    onAction = onAction,
                                    splitterState = splitterState,
                                    rightSplitterState = rightSplitterState,
                                )
                            },
                        )
                    },
                    screenshotContent = { screenshotSplitPaneState, rightSplitterState ->
                        PulseContent(
                            store = screenshotStateHolder,
                            content = { state, onAction ->
                                ScreenshotScreen(
                                    state = state,
                                    onAction = onAction,
                                    screenshotSplitPaneState = screenshotSplitPaneState,
                                    rightSplitterState = rightSplitterState,
                                )
                            },
                        )
                    },
                    scrcpyNewDisplayContent = { splitterState, rightSplitterState ->
                        PulseContent(
                            store = scrcpyNewDisplayStateHolder,
                            content = { state, onAction ->
                                ScrcpyNewDisplayScreen(
                                    state = state,
                                    onAction = onAction,
                                    splitterState = splitterState,
                                    rightSplitterState = rightSplitterState,
                                )
                            },
                        )
                    },
                    settingContent = {
                        PulseContent(
                            store = settingStateHolder,
                            onEvent = {
                                when (it) {
                                    SettingSideEffect.Saved -> {
                                        onBroadcast(AppBroadCast.Refresh)
                                    }
                                }
                            },
                        ) { state, onAction ->
                            SettingScreen(
                                state = state,
                                onAction = onAction,
                                onMainRefresh = { onBroadcast(AppBroadCast.Refresh) },
                            )
                        }
                    },
                    deviceSettingsContent = {
                        PulseContent(
                            store = deviceSettingsStateHolder,
                            onEvent = {
                                when (it) {
                                    DeviceSettingsSideEffect.Saved -> onBroadcast(AppBroadCast.Refresh)
                                    DeviceSettingsSideEffect.Cancelled -> onBroadcast(AppBroadCast.Refresh)
                                }
                            },
                        ) { deviceSettingsState, onDeviceSettingsAction ->
                            deviceSettingsState.ifReady { device, deviceSettings ->
                                DeviceSettingsScreen(
                                    device = device,
                                    deviceSettings = deviceSettings,
                                    selectedCategory = deviceSettingsState.selectedCategory,
                                    onCategorySelected = { category ->
                                        onDeviceSettingsAction(DeviceSettingsAction.SelectCategory(category))
                                    },
                                    onUpdateDeviceSettings = { settings ->
                                        onDeviceSettingsAction(DeviceSettingsAction.UpdateSettings(settings))
                                    },
                                    onSave = { onDeviceSettingsAction(DeviceSettingsAction.Save) },
                                    onCancel = { onDeviceSettingsAction(DeviceSettingsAction.Cancel) },
                                    isSaving = deviceSettingsState.isSaving,
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}
