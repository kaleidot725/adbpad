package jp.kaleidot725.adbpad.data.repository

import jp.kaleidot725.adbpad.data.local.DeviceSettingsFileCreator
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.DeviceSettings
import jp.kaleidot725.adbpad.domain.repository.DeviceSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceSettingsRepositoryImpl : DeviceSettingsRepository {
    override suspend fun getDeviceSettings(device: Device): DeviceSettings =
        withContext(Dispatchers.IO) {
            DeviceSettingsFileCreator.load(device.serial)
        }

    override suspend fun saveDeviceSettings(
        device: Device,
        settings: DeviceSettings,
    ): Boolean =
        withContext(Dispatchers.IO) {
            DeviceSettingsFileCreator.save(settings)
        }

    override suspend fun deleteDeviceSettings(device: Device): Boolean =
        withContext(Dispatchers.IO) {
            DeviceSettingsFileCreator.delete(device.serial)
        }
}
