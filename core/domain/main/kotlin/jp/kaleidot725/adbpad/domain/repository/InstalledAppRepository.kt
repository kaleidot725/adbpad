package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device

interface InstalledAppRepository {
    suspend fun getInstalledApps(device: Device): List<InstalledApp>
}
