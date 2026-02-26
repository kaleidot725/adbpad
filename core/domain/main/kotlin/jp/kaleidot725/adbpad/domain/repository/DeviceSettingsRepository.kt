package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.DeviceSettings

interface DeviceSettingsRepository {
    suspend fun getDeviceSettings(device: Device): DeviceSettings

    suspend fun saveDeviceSettings(
        device: Device,
        settings: DeviceSettings,
    ): Boolean

    suspend fun deleteDeviceSettings(device: Device): Boolean
}
