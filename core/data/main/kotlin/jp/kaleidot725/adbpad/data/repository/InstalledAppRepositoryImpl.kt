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
    ): Result<List<AppFileEntry>, Exception> =
        withContext(Dispatchers.IO) {
            try {
                val rootPath = getRootPath(app, directory)
                val files = adbClient.execute(ListFilesRequest(rootPath), device.serial)
                Ok(files.toAppFileEntries())
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    override suspend fun getAppFileChildren(
        device: Device,
        directory: AppFileEntry.Directory,
    ): Result<List<AppFileEntry>, Exception> =
        withContext(Dispatchers.IO) {
            try {
                val files = adbClient.execute(ListFilesRequest(directory.path), device.serial)
                Ok(files.toAppFileEntries())
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    private fun getRootPath(
        app: InstalledApp,
        directory: AppDataDirectory,
    ): String =
        when (directory) {
            AppDataDirectory.Data -> app.dataDir
            AppDataDirectory.SdCardData -> app.sdCardDataDir
        }

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

    private fun AndroidFile.toAppFileEntry(): AppFileEntry {
        val path = directory.resolveChildPath(name)
        return when (type) {
            AndroidFileType.DIRECTORY ->
                AppFileEntry.Directory(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            AndroidFileType.REGULAR_FILE ->
                AppFileEntry.File(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            AndroidFileType.SYMBOLIC_LINK ->
                AppFileEntry.Link(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            else ->
                AppFileEntry.Other(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
        }
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
