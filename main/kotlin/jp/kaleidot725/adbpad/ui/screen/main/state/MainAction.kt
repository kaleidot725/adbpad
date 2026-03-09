package jp.kaleidot725.adbpad.ui.screen.main.state

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.adbpad.domain.model.MainCategory
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.setting.WindowSize

sealed class MainAction : PulseAction {
    data class SaveSetting(
        val windowSize: WindowSize,
    ) : MainAction()

    data class ClickCategory(
        val category: MainCategory,
    ) : MainAction()

    data object OpenSetting : MainAction()

    data class OpenDeviceSettings(
        val device: Device,
    ) : MainAction()

    data object ToggleAlwaysOnTop : MainAction()

    data object ToggleNavigationRail : MainAction()

    data object Shutdown : MainAction()
}
