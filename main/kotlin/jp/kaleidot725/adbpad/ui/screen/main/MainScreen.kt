package jp.kaleidot725.adbpad.ui.screen.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import jp.kaleidot725.adbpad.core.mvi.MVIChildContent
import jp.kaleidot725.adbpad.core.mvi.MVIDialogContent
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.getWindowSize
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.rail.NavigationRail
import jp.kaleidot725.adbpad.ui.screen.CommandScreen
import jp.kaleidot725.adbpad.ui.screen.ScreenLayout
import jp.kaleidot725.adbpad.ui.screen.command.CommandStateHolder
import jp.kaleidot725.adbpad.ui.screen.device.DeviceSettingsScreen
import jp.kaleidot725.adbpad.ui.screen.device.DeviceSettingsStateHolder
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsAction
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsSideEffect
import jp.kaleidot725.adbpad.ui.screen.error.AdbErrorScreen
import jp.kaleidot725.adbpad.ui.screen.main.state.MainAction
import jp.kaleidot725.adbpad.ui.screen.main.state.MainCategory
import jp.kaleidot725.adbpad.ui.screen.main.state.MainDialog
import jp.kaleidot725.adbpad.ui.screen.main.state.MainState
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
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.rememberSplitPaneState

private fun createLightColorScheme(accentColor: AccentColor) =
    ColorScheme(
        primary = accentColor.lightColor,
        onPrimary = UserColor.Light.ON_PRIMARY,
        primaryContainer = accentColor.lightColor.copy(alpha = 0.1f),
        onPrimaryContainer = accentColor.lightColor,
        inversePrimary = accentColor.lightColor.copy(alpha = 0.8f),
        secondary = UserColor.Light.SECONDARY,
        onSecondary = UserColor.Light.ON_SECONDARY,
        secondaryContainer = UserColor.Light.SECONDARY_VARIANT,
        onSecondaryContainer = UserColor.Light.ON_SECONDARY,
        tertiary = UserColor.Light.SECONDARY,
        onTertiary = UserColor.Light.ON_SECONDARY,
        tertiaryContainer = UserColor.Light.SECONDARY_VARIANT,
        onTertiaryContainer = UserColor.Light.ON_SECONDARY,
        background = UserColor.Light.BACKGROUND,
        onBackground = UserColor.Light.ON_BACKGROUND,
        surface = UserColor.Light.SURFACE,
        onSurface = UserColor.Light.ON_SURFACE,
        surfaceVariant = UserColor.Light.SURFACE.copy(alpha = 0.8f),
        onSurfaceVariant = UserColor.Light.ON_SURFACE.copy(alpha = 0.8f),
        surfaceTint = accentColor.lightColor,
        inverseSurface = UserColor.Light.ON_SURFACE,
        inverseOnSurface = UserColor.Light.SURFACE,
        error = UserColor.Light.ERROR,
        onError = UserColor.Light.ON_ERROR,
        errorContainer = UserColor.Light.ERROR.copy(alpha = 0.1f),
        onErrorContainer = UserColor.Light.ERROR,
        outline = UserColor.Light.ON_SURFACE.copy(alpha = 0.5f),
        outlineVariant = UserColor.Light.ON_SURFACE.copy(alpha = 0.3f),
        scrim = UserColor.Light.ON_SURFACE.copy(alpha = 0.9f),
        surfaceBright = UserColor.Light.SURFACE,
        surfaceDim = UserColor.Light.SURFACE.copy(alpha = 0.9f),
        surfaceContainer = UserColor.Light.SURFACE.copy(alpha = 0.8f),
        surfaceContainerHigh = UserColor.Light.SURFACE.copy(alpha = 0.9f),
        surfaceContainerHighest = UserColor.Light.SURFACE,
        surfaceContainerLow = UserColor.Light.SURFACE.copy(alpha = 0.6f),
        surfaceContainerLowest = UserColor.Light.SURFACE.copy(alpha = 0.4f),
    )

private fun createDarkColorScheme(accentColor: AccentColor) =
    ColorScheme(
        primary = accentColor.darkColor,
        onPrimary = UserColor.Dark.ON_PRIMARY,
        primaryContainer = accentColor.darkColor.copy(alpha = 0.2f),
        onPrimaryContainer = accentColor.darkColor,
        inversePrimary = accentColor.darkColor.copy(alpha = 0.8f),
        secondary = UserColor.Dark.SECONDARY,
        onSecondary = UserColor.Dark.ON_SECONDARY,
        secondaryContainer = UserColor.Dark.SECONDARY_VARIANT,
        onSecondaryContainer = UserColor.Dark.ON_SECONDARY,
        tertiary = UserColor.Dark.SECONDARY,
        onTertiary = UserColor.Dark.ON_SECONDARY,
        tertiaryContainer = UserColor.Dark.SECONDARY_VARIANT,
        onTertiaryContainer = UserColor.Dark.ON_SECONDARY,
        background = UserColor.Dark.BACKGROUND,
        onBackground = UserColor.Dark.ON_BACKGROUND,
        surface = UserColor.Dark.SURFACE,
        onSurface = UserColor.Dark.ON_SURFACE,
        surfaceVariant = UserColor.Dark.SURFACE.copy(alpha = 0.8f),
        onSurfaceVariant = UserColor.Dark.ON_SURFACE.copy(alpha = 0.8f),
        surfaceTint = accentColor.darkColor,
        inverseSurface = UserColor.Dark.ON_SURFACE,
        inverseOnSurface = UserColor.Dark.SURFACE,
        error = UserColor.Dark.ERROR,
        onError = UserColor.Dark.ON_ERROR,
        errorContainer = UserColor.Dark.ERROR.copy(alpha = 0.1f),
        onErrorContainer = UserColor.Dark.ERROR,
        outline = UserColor.Dark.ON_SURFACE.copy(alpha = 0.5f),
        outlineVariant = UserColor.Dark.ON_SURFACE.copy(alpha = 0.3f),
        scrim = UserColor.Dark.ON_SURFACE.copy(alpha = 0.9f),
        surfaceBright = UserColor.Dark.SURFACE,
        surfaceDim = UserColor.Dark.SURFACE.copy(alpha = 0.9f),
        surfaceContainer = UserColor.Dark.SURFACE.copy(alpha = 0.8f),
        surfaceContainerHigh = UserColor.Dark.SURFACE.copy(alpha = 0.9f),
        surfaceContainerHighest = UserColor.Dark.SURFACE,
        surfaceContainerLow = UserColor.Dark.SURFACE.copy(alpha = 0.6f),
        surfaceContainerLowest = UserColor.Dark.SURFACE.copy(alpha = 0.4f),
    )

@Composable
fun MainScreen(
    state: MainState,
    onAction: (MainAction) -> Unit,
    mainStateHolder: MainStateHolder,
    onExitApplication: () -> Unit,
) {
    val windowState by remember(state.size.width, state.size.height) {
        mutableStateOf(WindowState(width = state.size.width.dp, height = state.size.height.dp))
    }

    Window(
        title = Language.windowTitle,
        icon = painterResource("icon.png"),
        onCloseRequest = {
            onAction(MainAction.Shutdown)
            onExitApplication()
        },
        state = windowState,
        alwaysOnTop = state.isAlwaysOnTop,
    ) {
        MenuBar {
            Menu(Language.menuWindow) {
                CheckboxItem(
                    text = Language.menuWindowMaximize,
                    checked = windowState.placement == WindowPlacement.Maximized,
                    onCheckedChange = {
                        windowState.placement =
                            if (windowState.placement == WindowPlacement.Maximized) {
                                WindowPlacement.Floating
                            } else {
                                WindowPlacement.Maximized
                            }
                    },
                )
                CheckboxItem(
                    text = Language.menuWindowMinimize,
                    checked = windowState.isMinimized,
                    onCheckedChange = {
                        windowState.isMinimized = !windowState.isMinimized
                    },
                )
                CheckboxItem(
                    text = Language.menuWindowFullscreen,
                    checked = windowState.placement == WindowPlacement.Fullscreen,
                    onCheckedChange = {
                        windowState.placement =
                            if (windowState.placement == WindowPlacement.Fullscreen) {
                                WindowPlacement.Floating
                            } else {
                                WindowPlacement.Fullscreen
                            }
                    },
                )
                CheckboxItem(
                    text = Language.menuWindowAlwaysOnTop,
                    checked = state.isAlwaysOnTop,
                    onCheckedChange = {
                        onAction(MainAction.ToggleAlwaysOnTop)
                    },
                )
            }
        }
        DisposableEffect(Unit) { onDispose { onAction(MainAction.SaveSetting(this@Window.getWindowSize())) } }
        MaterialTheme(
            colorScheme =
                if (state.isDark == true) {
                    createDarkColorScheme(state.accentColor)
                } else {
                    createLightColorScheme(
                        state.accentColor,
                    )
                },
        ) {
            App(
                state = state,
                onMainAction = onAction,
                onMainRefresh = { mainStateHolder.onRefresh() },
                commandStateHolder = mainStateHolder.commandStateHolder,
                textCommandStateHolder = mainStateHolder.textCommandStateHolder,
                screenshotStateHolder = mainStateHolder.screenshotStateHolder,
                scrcpyNewDisplayStateHolder = mainStateHolder.scrcpyNewDisplayStateHolder,
                topStateHolder = mainStateHolder.topStateHolder,
                deviceSettingsStateHolder = mainStateHolder.deviceSettingsStateHolder,
                settingStateHolder = mainStateHolder.settingStateHolder,
            )
        }
    }
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
private fun App(
    state: MainState,
    onMainAction: (MainAction) -> Unit,
    onMainRefresh: () -> Unit,
    commandStateHolder: CommandStateHolder,
    textCommandStateHolder: TextCommandStateHolder,
    screenshotStateHolder: ScreenshotStateHolder,
    scrcpyNewDisplayStateHolder: ScrcpyNewDisplayStateHolder,
    topStateHolder: TopStateHolder,
    deviceSettingsStateHolder: DeviceSettingsStateHolder,
    settingStateHolder: SettingStateHolder,
) {
    val textSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.1f,
        )
    val screenshotSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.1f,
        )
    val scrcpyNewDisplaySplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.1f,
        )

    Crossfade(state.language) {
        Surface {
            ScreenLayout(
                top = {
                    MVIChildContent(
                        mvi = topStateHolder,
                        content = { topState, onTopAction ->
                            TopSection(
                                topState = topState,
                                onTopAction = onTopAction,
                                onOpenDeviceSettings = { device -> onMainAction(MainAction.OpenDeviceSettings(device)) },
                                onRefreshDevices = onMainRefresh,
                                onToggleNavigationRail = { onMainAction(MainAction.ToggleNavigationRail) },
                            )
                        },
                    )
                },
                navigationRail = {
                    NavigationRail(
                        category = state.category,
                        isCollapsed = state.isNavigationRailCollapsed,
                        onSelectCategory = { onMainAction(MainAction.ClickCategory(it)) },
                        onOpenSetting = { onMainAction(MainAction.OpenSetting) },
                    )
                },
                content = {
                    when (state.category) {
                        MainCategory.Command -> {
                            MVIChildContent(
                                mvi = commandStateHolder,
                                content = { state, onAction ->
                                    CommandScreen(
                                        state = state,
                                        onAction = onAction,
                                    )
                                },
                            )
                        }

                        MainCategory.Text -> {
                            MVIChildContent(
                                mvi = textCommandStateHolder,
                                content = { state, onAction ->
                                    TextCommandScreen(
                                        state = state,
                                        onAction = onAction,
                                        splitterState = textSplitPaneState,
                                    )
                                },
                            )
                        }

                        MainCategory.Screenshot -> {
                            MVIChildContent(
                                mvi = screenshotStateHolder,
                                content = { state, onAction ->
                                    ScreenshotScreen(
                                        state = state,
                                        onAction = onAction,
                                        screenshotSplitPaneState = screenshotSplitPaneState,
                                    )
                                },
                            )
                        }

                        MainCategory.ScrcpyNewDisplay -> {
                            MVIChildContent(
                                mvi = scrcpyNewDisplayStateHolder,
                                content = { state, onAction ->
                                    ScrcpyNewDisplayScreen(
                                        state = state,
                                        onAction = onAction,
                                        splitterState = scrcpyNewDisplaySplitPaneState,
                                    )
                                },
                            )
                        }

                        MainCategory.File -> {
                            Text("TEST")
                        }
                    }
                },
                right = {},
                dialog = {
                    when (state.dialog) {
                        MainDialog.Setting -> {
                            MVIDialogContent(
                                mvi = settingStateHolder,
                                onSideEffect = {
                                    when (it) {
                                        SettingSideEffect.Saved -> {
                                            onMainRefresh()
                                        }
                                    }
                                },
                            ) { state, onAction ->
                                SettingScreen(
                                    state = state,
                                    onAction = onAction,
                                    onMainRefresh = onMainRefresh,
                                )
                            }
                        }

                        is MainDialog.DeviceSettings -> {
                            MVIDialogContent(
                                mvi = deviceSettingsStateHolder,
                                onSideEffect = {
                                    when (it) {
                                        DeviceSettingsSideEffect.Saved -> onMainRefresh()
                                        DeviceSettingsSideEffect.Cancelled -> onMainRefresh()
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
                        }

                        MainDialog.AdbError -> {
                            AdbErrorScreen(
                                onOpenSetting = { onMainAction(MainAction.OpenSetting) },
                            )
                        }

                        MainDialog.Empty -> {
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
