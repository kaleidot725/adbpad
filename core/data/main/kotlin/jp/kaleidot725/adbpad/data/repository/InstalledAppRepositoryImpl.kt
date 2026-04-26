package jp.kaleidot725.adbpad.data.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.Feature
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import com.malinskiy.adam.request.sync.PullRequest
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.os.OSContext
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

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

    override suspend fun getInstalledAppIcon(
        device: Device,
        app: InstalledApp,
    ): File? =
        withContext(Dispatchers.IO) {
            val sourceDir = app.sourceDir ?: return@withContext null
            val appDirectory = getAppIconDirectory(device, app)
            appDirectory.mkdirs()

            val cachedIcon = findCachedIcon(appDirectory)
            if (cachedIcon != null) return@withContext cachedIcon

            val apkFile = File(appDirectory, "base.apk")
            apkFile.delete()
            apkFile.createNewFile()

            val pulled =
                adbClient.execute(
                    PullRequest(
                        source = sourceDir,
                        destination = apkFile,
                        supportedFeatures = emptyList<Feature>(),
                    ),
                    device.serial,
                )
            if (!pulled) return@withContext null

            return@withContext extractIcon(apkFile, appDirectory)
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

    private fun getAppIconDirectory(
        device: Device,
        app: InstalledApp,
    ): File {
        val baseDirectory = File(OSContext.resolveOSContext().directory, "app-icons")
        return File(File(baseDirectory, device.serial.sanitizeFileName()), app.packageName.sanitizeFileName())
    }

    private fun findCachedIcon(directory: File): File? =
        ICON_EXTENSIONS
            .map { extension -> File(directory, "$ICON_FILE_NAME.$extension") }
            .firstOrNull { file -> file.exists() && file.length() > 0L }

    private fun extractIcon(
        apkFile: File,
        destinationDirectory: File,
    ): File? {
        ZipFile(apkFile).use { zipFile ->
            val entry =
                zipFile
                    .entries()
                    .asSequence()
                    .filter { entry -> !entry.isDirectory && entry.name.isIconImagePath() }
                    .maxByOrNull { entry -> entry.iconScore() }
                    ?: return null

            val extension = entry.name.substringAfterLast('.', "png")
            val iconFile = File(destinationDirectory, "$ICON_FILE_NAME.$extension")
            zipFile.getInputStream(entry).use { input ->
                iconFile.outputStream().use { output -> input.copyTo(output) }
            }
            return iconFile
        }
    }

    private fun String.isIconImagePath(): Boolean {
        val lower = lowercase(Locale.getDefault())
        val isImage = ICON_EXTENSIONS.any { extension -> lower.endsWith(".$extension") }
        val isResource = lower.startsWith("res/mipmap") || lower.startsWith("res/drawable")
        val isIconLike = ICON_NAME_PARTS.any { part -> lower.contains(part) }
        return isImage && isResource && isIconLike
    }

    private fun ZipEntry.iconScore(): Long {
        val lower = name.lowercase(Locale.getDefault())
        val nameScore =
            when {
                lower.contains("ic_launcher") -> 4_000_000L
                lower.contains("launcher") -> 3_000_000L
                lower.contains("icon") -> 2_000_000L
                lower.contains("foreground") -> 1_000_000L
                else -> 0L
            }
        val densityScore =
            when {
                lower.contains("xxxhdpi") -> 600_000L
                lower.contains("xxhdpi") -> 500_000L
                lower.contains("xhdpi") -> 400_000L
                lower.contains("hdpi") -> 300_000L
                lower.contains("mdpi") -> 200_000L
                else -> 100_000L
            }

        return nameScore + densityScore + size.coerceAtLeast(0L)
    }

    private fun String.sanitizeFileName(): String = replace(Regex("[^A-Za-z0-9._-]"), "_")

    private companion object {
        const val PACKAGE_PREFIX = "package:"
        const val ICON_FILE_NAME = "icon"
        val ICON_EXTENSIONS = listOf("png", "webp", "jpg", "jpeg")
        val ICON_NAME_PARTS = listOf("ic_launcher", "launcher", "icon", "foreground")
    }
}
