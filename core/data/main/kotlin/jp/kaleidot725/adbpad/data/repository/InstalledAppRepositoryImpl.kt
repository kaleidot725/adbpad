package jp.kaleidot725.adbpad.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.Feature
import com.malinskiy.adam.request.device.FetchDeviceFeaturesRequest
import com.malinskiy.adam.request.pkg.StreamingPackageInstallRequest
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import com.malinskiy.adam.request.sync.PullRequest
import com.malinskiy.adam.request.sync.PushRequest
import com.malinskiy.adam.request.sync.compat.CompatListFileRequest
import jp.kaleidot725.adbpad.domain.model.app.AppDataDirectory
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.AppFilePreview
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.Locale
import kotlin.io.path.createTempFile
import com.malinskiy.adam.request.shell.v2.ShellCommandRequest as ShellV2CommandRequest

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
                val files = listRemoteFiles(device, rootPath)
                Ok(files)
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
                val files = listRemoteFiles(device, directory.path)
                Ok(files)
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    override suspend fun getAppFilePreview(
        device: Device,
        entry: AppFileEntry,
    ): Result<AppFilePreview, Exception> =
        withContext(Dispatchers.IO) {
            try {
                if (entry !is AppFileEntry.File) return@withContext Ok(AppFilePreview.Unsupported(entry))

                when {
                    entry.size == 0L && entry.isTextFile() -> Ok(AppFilePreview.Text(entry, ""))
                    entry.size == 0L -> Ok(AppFilePreview.Unsupported(entry))
                    entry.isImageFile() -> Ok(AppFilePreview.Image(entry, pullAppFile(device, entry)))
                    entry.isTextFile() -> Ok(AppFilePreview.Text(entry, pullAppFile(device, entry).readText()))
                    else -> Ok(AppFilePreview.Unsupported(entry))
                }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    override suspend fun saveAppFile(
        device: Device,
        entry: AppFileEntry.File,
        destination: File,
    ): Result<Unit, Exception> =
        withContext(Dispatchers.IO) {
            try {
                val target = prepareDestinationFile(destination)
                if (entry.size == 0L) {
                    target.outputStream().use { }
                } else {
                    pullAppFile(device, entry, target)
                }
                Ok(Unit)
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    override suspend fun overwriteAppFile(
        device: Device,
        source: File,
        destination: AppFileEntry.File,
    ): Result<Unit, Exception> =
        withContext(Dispatchers.IO) {
            try {
                if (!source.isFile) throw IOException("${source.name} is not a file")

                val privateDataPath = AppFileEntryMapper.toPrivateDataPath(destination.path)
                if (privateDataPath != null) {
                    overwritePrivateDataFile(device, source, privateDataPath, destination)
                } else {
                    val supportedFeatures = adbClient.execute(FetchDeviceFeaturesRequest(device.serial), device.serial)
                    val isPushed = adbClient.execute(PushRequest(source, destination.path, supportedFeatures), device.serial)
                    if (!isPushed) throw IOException("Failed to overwrite ${destination.name}")
                }
                Ok(Unit)
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    override suspend fun deleteAppFile(
        device: Device,
        entry: AppFileEntry.File,
    ): Result<Unit, Exception> =
        withContext(Dispatchers.IO) {
            try {
                val privateDataPath = AppFileEntryMapper.toPrivateDataPath(entry.path)
                val result =
                    if (privateDataPath != null) {
                        executeRunAsCommand(
                            device = device,
                            packageName = privateDataPath.packageName,
                            command = "rm -f ${AppFileEntryMapper.shellQuote(privateDataPath.relativePath)}",
                        )
                    } else {
                        adbClient
                            .execute(ShellCommandRequest("rm -f ${AppFileEntryMapper.shellQuote(entry.path)}"), device.serial)
                            .toShellOutput()
                    }

                if (result.exitCode != 0) throw IOException(result.errorMessage("Failed to delete ${entry.name}"))
                Ok(Unit)
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                Err(exception)
            }
        }

    private suspend fun pullAppFile(
        device: Device,
        entry: AppFileEntry.File,
    ): File {
        val localFile = createPreviewFile(entry)
        pullAppFile(device, entry, localFile)
        return localFile
    }

    private suspend fun pullAppFile(
        device: Device,
        entry: AppFileEntry.File,
        localFile: File,
    ) {
        val privateDataPath = AppFileEntryMapper.toPrivateDataPath(entry.path)
        if (privateDataPath != null) {
            pullPrivateDataFile(device, privateDataPath, entry, localFile)
            return
        }

        val supportedFeatures = adbClient.execute(FetchDeviceFeaturesRequest(device.serial), device.serial)
        val isPulled = adbClient.execute(PullRequest(entry.path, localFile, supportedFeatures), device.serial)
        if (!isPulled) throw IOException("Failed to load ${entry.name}")
    }

    private suspend fun pullPrivateDataFile(
        device: Device,
        privateDataPath: AppFileEntryMapper.PrivateDataPath,
        entry: AppFileEntry.File,
        localFile: File,
    ) {
        val result =
            executeRunAsCommand(
                device = device,
                packageName = privateDataPath.packageName,
                command = "cat ${AppFileEntryMapper.shellQuote(privateDataPath.relativePath)}",
                requireShellV2 = true,
            )
        if (result.exitCode != 0) throw IOException(result.errorMessage("Failed to load ${entry.name}"))
        localFile.outputStream().use { it.write(result.stdout) }
    }

    private suspend fun overwritePrivateDataFile(
        device: Device,
        source: File,
        privateDataPath: AppFileEntryMapper.PrivateDataPath,
        destination: AppFileEntry.File,
    ) {
        val supportedFeatures = adbClient.execute(FetchDeviceFeaturesRequest(device.serial), device.serial)
        val remoteTempFile = "/data/local/tmp/adbpad-${System.nanoTime()}-${AppFileEntryMapper.sanitizeRemoteFileName(source.name)}"
        try {
            val isPushed =
                adbClient.execute(
                    PushRequest(source, remoteTempFile, supportedFeatures, mode = "0644"),
                    device.serial,
                )
            if (!isPushed) throw IOException("Failed to upload ${source.name}")

            val result =
                executeRunAsCommand(
                    device = device,
                    packageName = privateDataPath.packageName,
                    command =
                        "cp ${AppFileEntryMapper.shellQuote(remoteTempFile)} " +
                            AppFileEntryMapper.shellQuote(privateDataPath.relativePath),
                )
            if (result.exitCode != 0) throw IOException(result.errorMessage("Failed to overwrite ${destination.name}"))
        } finally {
            adbClient.execute(ShellCommandRequest("rm -f ${AppFileEntryMapper.shellQuote(remoteTempFile)}"), device.serial)
        }
    }

    private fun createPreviewFile(entry: AppFileEntry.File): File {
        val extension = entry.extension()
        val suffix = if (extension.isBlank()) ".tmp" else ".$extension"
        return createTempFile(prefix = "adbpad-preview-", suffix = suffix)
            .toFile()
            .apply { deleteOnExit() }
    }

    private fun prepareDestinationFile(destination: File): File {
        if (destination.exists() && destination.isDirectory) {
            throw IOException("${destination.name} is a directory")
        }

        destination.parentFile?.mkdirs()
        if (!destination.exists() && !destination.createNewFile()) {
            throw IOException("Failed to create ${destination.name}")
        }

        return destination
    }

    private fun getRootPath(
        app: InstalledApp,
        directory: AppDataDirectory,
    ): String =
        when (directory) {
            AppDataDirectory.Data -> app.dataDir
            AppDataDirectory.SdCardData -> app.sdCardDataDir
        }

    private suspend fun listRemoteFiles(
        device: Device,
        directory: String,
    ): List<AppFileEntry> {
        val privateDataPath = AppFileEntryMapper.toPrivateDataPath(directory)
        if (privateDataPath != null) {
            val result =
                executeRunAsCommand(
                    device = device,
                    packageName = privateDataPath.packageName,
                    command = "ls -la ${AppFileEntryMapper.shellQuote(privateDataPath.relativePath)}",
                )
            if (result.exitCode != 0) throw IOException(result.errorMessage("Failed to load $directory"))
            return AppFileEntryMapper.fromRunAsLsOutput(directory, result.output)
        }

        val supportedFeatures = adbClient.execute(FetchDeviceFeaturesRequest(device.serial), device.serial)
        return adbClient
            .execute(CompatListFileRequest(directory, supportedFeatures), device.serial)
            .let { AppFileEntryMapper.fromSyncEntries(directory, it) }
    }

    private fun String.toInstalledApp(): InstalledApp? {
        val line = trim()
        if (!line.startsWith(PACKAGE_PREFIX)) return null

        return InstalledApp(packageName = line.removePrefix(PACKAGE_PREFIX))
    }

    private suspend fun executeRunAsCommand(
        device: Device,
        packageName: String,
        command: String,
        requireShellV2: Boolean = false,
    ): ShellOutput {
        val shellCommand = "run-as ${AppFileEntryMapper.shellQuote(packageName)} $command"
        val supportedFeatures = adbClient.execute(FetchDeviceFeaturesRequest(device.serial), device.serial)
        return if (supportedFeatures.contains(Feature.SHELL_V2)) {
            adbClient.execute(ShellV2CommandRequest(shellCommand), device.serial).toShellOutput()
        } else {
            if (requireShellV2) throw IOException("run-as file transfer requires shell v2")
            adbClient.execute(ShellCommandRequest(shellCommand), device.serial).toShellOutput()
        }
    }

    private fun ShellOutput.errorMessage(fallback: String): String =
        errorOutput
            .trim()
            .ifBlank { output.trim() }
            .ifBlank { fallback }

    private fun com.malinskiy.adam.request.shell.v1.ShellCommandResult.toShellOutput(): ShellOutput =
        ShellOutput(
            stdout = stdout,
            output = output,
            errorOutput = "",
            exitCode = exitCode,
        )

    private fun com.malinskiy.adam.request.shell.v2.ShellCommandResult.toShellOutput(): ShellOutput =
        ShellOutput(
            stdout = stdout,
            output = output,
            errorOutput = errorOutput,
            exitCode = exitCode,
        )

    private data class ShellOutput(
        val stdout: ByteArray,
        val output: String,
        val errorOutput: String,
        val exitCode: Int,
    )

    private fun AppFileEntry.File.isImageFile(): Boolean = extension() in IMAGE_FILE_EXTENSIONS

    private fun AppFileEntry.File.isTextFile(): Boolean {
        val normalizedName = name.lowercase(Locale.getDefault())
        return extension() in TEXT_FILE_EXTENSIONS || normalizedName in TEXT_FILE_NAMES
    }

    private fun AppFileEntry.File.extension(): String =
        name
            .substringAfterLast('.', missingDelimiterValue = "")
            .lowercase(Locale.getDefault())

    companion object {
        private const val PACKAGE_PREFIX = "package:"
        private val IMAGE_FILE_EXTENSIONS = setOf("bmp", "gif", "jpeg", "jpg", "png", "webp")
        private val TEXT_FILE_EXTENSIONS =
            setOf(
                "cfg",
                "conf",
                "css",
                "csv",
                "gradle",
                "htm",
                "html",
                "ini",
                "java",
                "js",
                "json",
                "kt",
                "kts",
                "log",
                "md",
                "properties",
                "sh",
                "toml",
                "txt",
                "xml",
                "yaml",
                "yml",
            )
        private val TEXT_FILE_NAMES = setOf("changelog", "license", "notice", "readme")
    }
}
