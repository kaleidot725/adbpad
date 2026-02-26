package jp.kaleidot725.adbpad.data.local

import jp.kaleidot725.adbpad.domain.model.device.DeviceSettings
import kotlinx.serialization.json.Json
import java.io.IOException

object DeviceSettingsFileCreator {
    fun save(settings: DeviceSettings): Boolean =
        try {
            FilePathUtil.createDir()
            val deviceFile = FilePathUtil.getFilePath("device_${settings.deviceId}.json")
            val jsonContent = Json.encodeToString(DeviceSettings.serializer(), settings)
            deviceFile.writeText(jsonContent)
            true
        } catch (_: IOException) {
            false
        }

    fun load(deviceId: String): DeviceSettings {
        return try {
            val deviceFile = FilePathUtil.getFilePath("device_$deviceId.json")
            if (!deviceFile.exists()) {
                return DeviceSettings(deviceId = deviceId)
            }

            val content = deviceFile.readText()
            Json.decodeFromString(DeviceSettings.serializer(), content)
        } catch (_: Exception) {
            DeviceSettings(deviceId = deviceId)
        }
    }

    fun delete(deviceId: String): Boolean =
        try {
            val deviceFile = FilePathUtil.getFilePath("device_$deviceId.json")
            if (deviceFile.exists()) {
                deviceFile.delete()
            }
            true
        } catch (_: IOException) {
            false
        }
}
