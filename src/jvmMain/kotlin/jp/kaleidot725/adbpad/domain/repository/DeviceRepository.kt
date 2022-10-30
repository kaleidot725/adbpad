package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    suspend fun selectDevice(device: Device)
    fun getDeviceFlow(): Flow<List<Device>>
    fun getSelectedDeviceFlow(): Flow<Device?>
}