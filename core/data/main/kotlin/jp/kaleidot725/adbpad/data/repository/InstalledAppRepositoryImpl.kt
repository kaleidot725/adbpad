package jp.kaleidot725.adbpad.data.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.device.FetchDeviceFeaturesRequest
import com.malinskiy.adam.request.pkg.StreamingPackageInstallRequest
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class InstalledAppRepositoryImpl : InstalledAppRepository {
    private val adbClient = AndroidDebugBridgeClientFactory().build()

    override suspend fun getInstalledApps(device: Device): List<InstalledApp> =
        withContext(Dispatchers.IO) {
            val result = adbClient.execute(ShellCommandRequest("pm list packages -f -3"), device.serial)
            if (result.exitCode != 0) {
                error(result.output.ifBlank { "pm list packages failed with exit code ${result.exitCode}" })
            }

            result.output
                .lineSequence()
                .mapNotNull { it.toInstalledApp() }
                .sortedBy { it.packageName.lowercase(Locale.getDefault()) }
                .toList()
        }

    override suspend fun installPackage(
        device: Device,
        packageFile: File,
    ) {
        withContext(Dispatchers.IO) {
            val supportedFeatures = adbClient.execute(FetchDeviceFeaturesRequest(device.serial), device.serial)
            val result =
                adbClient.execute(
                    StreamingPackageInstallRequest(
                        pkg = packageFile,
                        supportedFeatures = supportedFeatures,
                        reinstall = true,
                    ),
                    device.serial,
                )
            if (!result.success) {
                error(result.output.ifBlank { "Package install failed: ${packageFile.absolutePath}" })
            }
        }
    }

    override suspend fun uninstallInstalledApp(
        device: Device,
        app: InstalledApp,
    ) {
        withContext(Dispatchers.IO) {
            val result = adbClient.execute(ShellCommandRequest("pm uninstall ${app.packageName}"), device.serial)
            if (result.exitCode != 0 || result.output.contains("Failure", ignoreCase = true)) {
                error(result.output.ifBlank { "pm uninstall failed with exit code ${result.exitCode}" })
            }
        }
    }

    // Parses `pm list packages -f` lines like `package:/path/base.apk=com.example.app`.
    private fun String.toInstalledApp(): InstalledApp? {
        val line = trim()
        if (!line.startsWith(PACKAGE_PREFIX)) return null

        val value = line.removePrefix(PACKAGE_PREFIX)
        val separatorIndex = value.lastIndexOf('=')
        return if (separatorIndex > 0 && separatorIndex < value.lastIndex) {
            InstalledApp(
                packageName = value.substring(separatorIndex + 1),
                sourceDir = value.substring(0, separatorIndex),
            )
        } else {
            InstalledApp(packageName = value)
        }
    }

    companion object {
        private const val PACKAGE_PREFIX = "package:"
    }
}
