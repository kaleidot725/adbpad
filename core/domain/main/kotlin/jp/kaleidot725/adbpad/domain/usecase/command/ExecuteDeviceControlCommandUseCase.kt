package jp.kaleidot725.adbpad.domain.usecase.command

import jp.kaleidot725.adbpad.domain.model.command.DeviceControlCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.DeviceControlCommandRepository

class ExecuteDeviceControlCommandUseCase(
    private val deviceControlCommandRepository: DeviceControlCommandRepository,
) {
    suspend operator fun invoke(
        device: Device,
        command: DeviceControlCommand,
    ) {
        deviceControlCommandRepository.sendCommand(
            device = device,
            command = command,
        )
    }
}
