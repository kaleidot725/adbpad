package jp.kaleidot725.adbpad.domain.repository

import com.github.michaelbull.result.Result
import jp.kaleidot725.adbpad.domain.model.app.AppDataDirectory
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import java.io.File

interface InstalledAppRepository {
    suspend fun getInstalledApps(device: Device): List<InstalledApp>

    suspend fun installPackage(
        device: Device,
        packageFile: File,
    ): Boolean

    suspend fun uninstallInstalledApp(
        device: Device,
        app: InstalledApp,
    ): Boolean

    suspend fun getAppFiles(
        device: Device,
        app: InstalledApp,
        directory: AppDataDirectory,
    ): Result<List<AppFileEntry>, Exception>

    suspend fun getAppFileChildren(
        device: Device,
        directory: AppFileEntry.Directory,
    ): Result<List<AppFileEntry>, Exception>
}
