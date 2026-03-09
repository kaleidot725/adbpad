package jp.kaleidot725.adbpad.ui.screen.main.state

import jp.kaleidot725.pulse.mvi.PulseState
import jp.kaleidot725.adbpad.domain.model.MainCategory
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.WindowSize

data class MainState(
    val language: Language.Type = Language.Type.ENGLISH,
    val isDark: Boolean? = null,
    val size: WindowSize = WindowSize.UNKNOWN,
    val dialog: MainDialog = MainDialog.Empty,
    val category: MainCategory = MainCategory.Command,
    val selectedDevice: Device? = null,
    val isAlwaysOnTop: Boolean = false,
    val isNavigationRailCollapsed: Boolean = true,
    val accentColor: AccentColor = AccentColor.BLUE,
) : PulseState
