package jp.kaleidot725.adbpad.ui.screen.device.state

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.adbpad.domain.model.device.DeviceSettings
import jp.kaleidot725.adbpad.ui.screen.device.model.DeviceSettingCategory

sealed class DeviceSettingsAction : PulseAction {
    data class SelectCategory(
        val category: DeviceSettingCategory,
    ) : DeviceSettingsAction()

    data class UpdateSettings(
        val settings: DeviceSettings,
    ) : DeviceSettingsAction()

    data object Save : DeviceSettingsAction()

    data object Cancel : DeviceSettingsAction()
}
