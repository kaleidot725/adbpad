package jp.kaleidot725.adbpad.domain.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.device.ListDevicesRequest
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.DeviceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class DeviceRepositoryImpl(
    private val deviceSettingsRepository: DeviceSettingsRepository,
) : DeviceRepository {
    private val adbClient = AndroidDebugBridgeClientFactory().build()

    private var lastSelectedDevice: Device? = null
    private val selectedDevice: MutableSharedFlow<Device?> = MutableSharedFlow(replay = 1)

    override suspend fun selectDevice(device: Device?): Boolean {
        lastSelectedDevice = device
        selectedDevice.emit(device)
        return true
    }

    override fun getSelectedDeviceFlow(): Flow<Device?> = selectedDevice.asSharedFlow()

    override suspend fun updateDevices(): List<Device> {
        val rawDevices = adbClient.execute(request = ListDevicesRequest())
        val devices =
            rawDevices.map { rawDevice ->
                // Create temporary device for settings lookup
                val tempDevice = Device(rawDevice.serial, "", rawDevice.state.convert())
                val deviceSettings = deviceSettingsRepository.getDeviceSettings(tempDevice)
                Device(
                    rawDevice.serial,
                    deviceSettings.customName ?: "",
                    rawDevice.state.convert(),
                )
            }
        if (devices.any { it.serial == lastSelectedDevice?.serial }.not()) {
            selectDevice(devices.firstOrNull())
        }
        return devices
    }

    private fun com.malinskiy.adam.request.device.DeviceState.convert(): DeviceState =
        when (this) {
            com.malinskiy.adam.request.device.DeviceState.OFFLINE -> DeviceState.OFFLINE
            com.malinskiy.adam.request.device.DeviceState.BOOTLOADER -> DeviceState.BOOTLOADER
            com.malinskiy.adam.request.device.DeviceState.DEVICE -> DeviceState.DEVICE
            com.malinskiy.adam.request.device.DeviceState.HOST -> DeviceState.HOST
            com.malinskiy.adam.request.device.DeviceState.RECOVERY -> DeviceState.RECOVERY
            com.malinskiy.adam.request.device.DeviceState.RESCUE -> DeviceState.RESCUE
            com.malinskiy.adam.request.device.DeviceState.SIDELOAD -> DeviceState.SIDELOAD
            com.malinskiy.adam.request.device.DeviceState.UNAUTHORIZED -> DeviceState.UNAUTHORIZED
            com.malinskiy.adam.request.device.DeviceState.AUTHORIZING -> DeviceState.AUTHORIZING
            com.malinskiy.adam.request.device.DeviceState.CONNECTING -> DeviceState.CONNECTING
            com.malinskiy.adam.request.device.DeviceState.UNKNOWN -> DeviceState.UNKNOWN
        }
}
