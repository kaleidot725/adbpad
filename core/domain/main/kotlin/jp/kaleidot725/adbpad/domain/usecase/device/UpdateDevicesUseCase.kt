package jp.kaleidot725.adbpad.domain.usecase.device

import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.DeviceRepository

class UpdateDevicesUseCase(
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke(): List<Device> = deviceRepository.updateDevices()
}
