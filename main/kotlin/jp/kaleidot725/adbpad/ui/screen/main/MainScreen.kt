package jp.kaleidot725.adbpad.ui.screen.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import jp.kaleidot725.adbpad.domain.model.MainCategory
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.getWindowSize
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.rail.NavigationRail
import jp.kaleidot725.adbpad.ui.screen.ScreenLayout
import jp.kaleidot725.adbpad.ui.screen.error.AdbErrorScreen
import jp.kaleidot725.adbpad.ui.screen.main.state.MainAction
import jp.kaleidot725.adbpad.ui.screen.main.state.MainDialog
import jp.kaleidot725.adbpad.ui.screen.main.state.MainState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitPaneState
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
    onRefresh: () -> Unit,
    onExitApplication: () -> Unit,
    topContent: @Composable () -> Unit,
    commandContent: @Composable (SplitPaneState) -> Unit,
    textCommandContent: @Composable (SplitPaneState, SplitPaneState) -> Unit,
    screenshotContent: @Composable (SplitPaneState, SplitPaneState) -> Unit,
    scrcpyNewDisplayContent: @Composable (SplitPaneState, SplitPaneState) -> Unit,
    settingContent: @Composable () -> Unit,
    deviceSettingsContent: @Composable () -> Unit,
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
                    createLightColorScheme(state.accentColor)
                },
        ) {
            App(
                state = state,
                onMainAction = onAction,
                topContent = topContent,
                commandContent = commandContent,
                textCommandContent = textCommandContent,
                screenshotContent = screenshotContent,
                scrcpyNewDisplayContent = scrcpyNewDisplayContent,
                settingContent = settingContent,
                deviceSettingsContent = deviceSettingsContent,
            )
        }
    }
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
private fun App(
    state: MainState,
    onMainAction: (MainAction) -> Unit,
    topContent: @Composable () -> Unit,
    commandContent: @Composable (SplitPaneState) -> Unit,
    textCommandContent: @Composable (SplitPaneState, SplitPaneState) -> Unit,
    screenshotContent: @Composable (SplitPaneState, SplitPaneState) -> Unit,
    scrcpyNewDisplayContent: @Composable (SplitPaneState, SplitPaneState) -> Unit,
    settingContent: @Composable () -> Unit,
    deviceSettingsContent: @Composable () -> Unit,
) {
    val commandSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.7f,
        )
    val textSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.1f,
        )
    val textRightSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.8f,
        )
    val screenshotSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.1f,
        )
    val screenshotRightSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.8f,
        )
    val scrcpyNewDisplaySplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.1f,
        )
    val scrcpyNewDisplayRightSplitPaneState =
        rememberSplitPaneState(
            initialPositionPercentage = 0.8f,
        )

    Crossfade(state.language) {
        Surface {
            ScreenLayout(
                top = topContent,
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
                            commandContent(commandSplitPaneState)
                        }

                        MainCategory.Text -> {
                            textCommandContent(textSplitPaneState, textRightSplitPaneState)
                        }

                        MainCategory.Screenshot -> {
                            screenshotContent(screenshotSplitPaneState, screenshotRightSplitPaneState)
                        }

                        MainCategory.ScrcpyNewDisplay -> {
                            scrcpyNewDisplayContent(scrcpyNewDisplaySplitPaneState, scrcpyNewDisplayRightSplitPaneState)
                        }

                        MainCategory.File -> {
                            Text("TEST")
                        }
                    }
                },
                dialog = {
                    when (state.dialog) {
                        MainDialog.Setting -> {
                            settingContent()
                        }

                        is MainDialog.DeviceSettings -> {
                            deviceSettingsContent()
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
