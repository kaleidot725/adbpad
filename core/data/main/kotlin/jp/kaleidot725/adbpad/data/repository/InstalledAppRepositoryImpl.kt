package jp.kaleidot725.adbpad.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.device.FetchDeviceFeaturesRequest
import com.malinskiy.adam.request.pkg.StreamingPackageInstallRequest
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import com.malinskiy.adam.request.sync.AndroidFile
import com.malinskiy.adam.request.sync.AndroidFileType
import com.malinskiy.adam.request.sync.ListFilesRequest
import jp.kaleidot725.adbpad.domain.model.app.AppDataDirectory
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.AppFileType
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class InstalledAppRepositoryImpl : InstalledAppRepository {
    private val adbClient = AndroidDebugBridgeClientFactory().build()

    override suspend fun getInstalledApps(device: Device): List<InstalledApp> =
        withContext(Dispatchers.IO) {
            try {
                val result = adbClient.execute(ShellCommandRequest("pm list packages -3"), device.serial)
                if (result.exitCode != 0) return@withContext emptyList()

                result.output
                    .lineSequence()
                    .mapNotNull { it.toInstalledApp() }
                    .sortedBy { it.packageName.lowercase(Locale.getDefault()) }
                    .toList()
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                emptyList()
            }
        }

    override suspend fun installPackage(
        device: Device,
        packageFile: File,
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
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
                result.success
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                false
            }
        }

    override suspend fun uninstallInstalledApp(
        device: Device,
        app: InstalledApp,
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val result = adbClient.execute(ShellCommandRequest("pm uninstall ${app.packageName}"), device.serial)
                result.exitCode == 0 && !result.output.contains("Failure", ignoreCase = true)
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                false
            }
        }

    override suspend fun getAppFiles(
        device: Device,
        app: InstalledApp,
        directory: AppDataDirectory,
        path: String,
    ): Result<List<AppFileEntry>, Exception> =
        withContext(Dispatchers.IO) {
            try {
                val rootPath = directory.getRootPath(app)
                val targetPath = if (path == rootPath || path.startsWith("$rootPath/")) path else rootPath
                val files = adbClient.execute(ListFilesRequest(targetPath), device.serial)
                Ok(files.toAppFileEntries())
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    // Parses `pm list packages` lines like `package:com.example.app`.
    private fun String.toInstalledApp(): InstalledApp? {
        val line = trim()
        if (!line.startsWith(PACKAGE_PREFIX)) return null

        return InstalledApp(packageName = line.removePrefix(PACKAGE_PREFIX))
    }

    private fun List<AndroidFile>.toAppFileEntries(): List<AppFileEntry> =
        asSequence()
            .filterNot { it.name == "." || it.name == ".." }
            .map { it.toAppFileEntry() }
            .sortedWith(
                compareByDescending<AppFileEntry> { it.isDirectory }
                    .thenBy { it.name.lowercase(Locale.getDefault()) },
            ).toList()

    private fun AndroidFile.toAppFileEntry(): AppFileEntry =
        AppFileEntry(
            name = name,
            path = directory.resolveChildPath(name),
            type = type.toAppFileType(),
            permissions = permissions,
            size = size,
            modifiedAt = "$date $time",
        )

    private fun AndroidFileType.toAppFileType(): AppFileType =
        when (this) {
            AndroidFileType.DIRECTORY -> AppFileType.Directory
            AndroidFileType.REGULAR_FILE -> AppFileType.File
            AndroidFileType.SYMBOLIC_LINK -> AppFileType.Link
            else -> AppFileType.Other
        }

    private fun String.resolveChildPath(name: String): String =
        if (endsWith("/")) {
            "$this$name"
        } else {
            "$this/$name"
        }

    companion object {
        private const val PACKAGE_PREFIX = "package:"
    }
}
