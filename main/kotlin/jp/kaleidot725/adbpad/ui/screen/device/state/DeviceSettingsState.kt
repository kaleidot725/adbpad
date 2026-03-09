package jp.kaleidot725.adbpad.ui.screen.device.state

import jp.kaleidot725.pulse.mvi.PulseState
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.DeviceSettings
import jp.kaleidot725.adbpad.ui.screen.device.model.DeviceSettingCategory

data class DeviceSettingsState(
    val device: Device? = null,
    val deviceSettings: DeviceSettings? = null,
    val selectedCategory: DeviceSettingCategory = DeviceSettingCategory.DEVICE,
    val isLoaded: Boolean = false,
    val isSaving: Boolean = false,
) : PulseState {
    val isReady: Boolean
        get() = isLoaded && device != null && deviceSettings != null

    inline fun <T> ifReady(block: (Device, DeviceSettings) -> T): T? =
        if (isReady && device != null && deviceSettings != null) {
            block(device, deviceSettings)
        } else {
            null
        }
}
