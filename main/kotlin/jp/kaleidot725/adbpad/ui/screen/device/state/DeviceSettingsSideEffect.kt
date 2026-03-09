package jp.kaleidot725.adbpad.ui.screen.device.state

import jp.kaleidot725.pulse.mvi.PulseEvent

sealed class DeviceSettingsSideEffect : PulseEvent {
    data object Saved : DeviceSettingsSideEffect()

    data object Cancelled : DeviceSettingsSideEffect()
}
