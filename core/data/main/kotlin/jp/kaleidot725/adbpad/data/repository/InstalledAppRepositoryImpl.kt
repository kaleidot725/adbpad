package jp.kaleidot725.adbpad.data.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    private companion object {
        const val PACKAGE_PREFIX = "package:"
    }
}
