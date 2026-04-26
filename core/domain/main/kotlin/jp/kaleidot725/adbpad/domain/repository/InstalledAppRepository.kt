package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import java.io.File

interface InstalledAppRepository {
    suspend fun getInstalledApps(device: Device): List<InstalledApp>

    suspend fun getInstalledAppIcon(
        device: Device,
        app: InstalledApp,
    ): File?
}
