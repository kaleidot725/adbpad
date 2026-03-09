package jp.kaleidot725.adbpad.ui.section.top.state

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.adbpad.domain.model.command.DeviceControlCommand
import jp.kaleidot725.adbpad.domain.model.device.Device

sealed class TopAction : PulseAction {
    data class SelectDevice(
        val device: Device,
    ) : TopAction()

    data class ExecuteCommand(
        val command: DeviceControlCommand,
    ) : TopAction()

    data object LaunchScrcpy : TopAction()
}
