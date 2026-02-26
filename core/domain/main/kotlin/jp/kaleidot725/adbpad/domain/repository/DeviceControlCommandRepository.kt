package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.command.DeviceControlCommand
import jp.kaleidot725.adbpad.domain.model.device.Device

interface DeviceControlCommandRepository {
    suspend fun sendCommand(
        device: Device,
        command: DeviceControlCommand,
        onStart: suspend () -> Unit = {},
        onComplete: suspend () -> Unit = {},
        onFailed: suspend () -> Unit = {},
    )
}
