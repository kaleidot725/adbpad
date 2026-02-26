package jp.kaleidot725.adbpad.domain.usecase.device

import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.DeviceRepository

class SelectDeviceUseCase(
    private val deviceRepository: DeviceRepository,
) {
    suspend operator fun invoke(device: Device) {
        deviceRepository.selectDevice(device)
    }
}
