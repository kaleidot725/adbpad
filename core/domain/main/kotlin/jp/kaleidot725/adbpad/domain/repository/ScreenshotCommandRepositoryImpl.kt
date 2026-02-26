package jp.kaleidot725.adbpad.domain.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.request.framebuffer.RawImageScreenCaptureAdapter
import com.malinskiy.adam.request.framebuffer.ScreenCaptureRequest
import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.os.OSContext
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.util.Date
import javax.imageio.ImageIO
import kotlin.math.max

class ScreenshotCommandRepositoryImpl : ScreenshotCommandRepository {
    private val adbClient = AndroidDebugBridgeClientFactory().build()

    init {
        createDirectory()
    }

    override fun getCommands(): List<ScreenshotCommand> =
        listOf(
            ScreenshotCommand.Both,
            ScreenshotCommand.Current,
            ScreenshotCommand.Light,
            ScreenshotCommand.Dark,
        )

    override suspend fun captureScreenshot(
        device: Device,
        command: ScreenshotCommand,
        onStart: suspend () -> Unit,
        onComplete: suspend (Screenshot) -> Unit,
        onFailed: suspend () -> Unit,
    ) {
        val date = Date()
        withContext(Dispatchers.IO) {
            onStart()

            val result =
                when (command) {
                    is ScreenshotCommand.Both -> sendBothCommand(device, date)
                    is ScreenshotCommand.Light -> sendLightCommand(device, date)
                    is ScreenshotCommand.Dark -> sendDarkCommand(device, date)
                    is ScreenshotCommand.Current -> sendCurrentCommand(device, date)
                    else -> false
                }

            delay(300)

            if (result) onComplete(Screenshot(getFileResult(date.time))) else onFailed()
        }
    }

    override suspend fun getScreenshots(
        searchText: String,
        sortType: SortType,
    ): List<Screenshot> =
        withContext(Dispatchers.IO) {
            val files = getDirectory().listFiles() ?: emptyArray()
            val filteredFiles =
                files
                    .filter { file -> file.isFile }
                    .map { file -> Screenshot(file) }
                    .filter { (it.file?.name ?: "").startsWith(searchText) }
            when (sortType) {
                SortType.SORT_BY_NAME_ASC -> {
                    filteredFiles.sortedBy { screenshot -> screenshot.file?.name ?: "" }
                }

                SortType.SORT_BY_NAME_DESC -> {
                    filteredFiles.sortedByDescending { screenshot -> screenshot.file?.name ?: "" }
                }
            }
        }

    override suspend fun rename(
        screenshot: Screenshot,
        name: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val file = screenshot.file ?: return@withContext false
            if (!file.exists()) return@withContext false

            val extension = file.extension
            val newName = if (name.endsWith(".$extension")) name else "$name.$extension"
            val newFile = File(file.parent, newName)

            file.renameTo(newFile)
        }
    }

    override suspend fun delete(screenshot: Screenshot) {
        withContext(Dispatchers.IO) {
            screenshot.file?.delete()
        }
    }

    private suspend fun sendBothCommand(
        device: Device,
        date: Date,
    ): Boolean {
        if (!sendCommand(device, NormalCommand.DarkThemeOn())) {
            return false
        }

        if (!capture(device, getFileA())) {
            return false
        }

        if (!sendCommand(device, NormalCommand.DarkThemeOff())) {
            return false
        }

        if (!capture(device, getFileB())) {
            return false
        }

        return concat(getFileA(), getFileB(), getFileResult(date.time))
    }

    private suspend fun sendDarkCommand(
        device: Device,
        date: Date,
    ): Boolean {
        if (!sendCommand(device, NormalCommand.DarkThemeOn())) {
            return false
        }

        return capture(device, getFileResult(date.time))
    }

    private suspend fun sendLightCommand(
        device: Device,
        date: Date,
    ): Boolean {
        if (!sendCommand(device, NormalCommand.DarkThemeOff())) {
            return false
        }

        return capture(device, getFileResult(date.time))
    }

    private suspend fun sendCurrentCommand(
        device: Device,
        date: Date,
    ): Boolean = capture(device, getFileResult(date.time))

    private suspend fun capture(
        device: Device,
        file: File,
        delay: Long = DEFAULT_DELAY,
    ): Boolean {
        delay(delay)
        val adb = AndroidDebugBridgeClientFactory().build()
        val adapter = RawImageScreenCaptureAdapter()
        val image = adb.execute(request = ScreenCaptureRequest(adapter), serial = device.serial).toBufferedImage()
        return ImageIO.write(image, EXTENSION_NAME, file)
    }

    private fun concat(
        fileA: File,
        fileB: File,
        outputFile: File,
    ): Boolean =
        try {
            val inputA = ImageIO.read(fileA)
            val inputB = ImageIO.read(fileB)
            val totalWidth = inputA.width + inputB.width
            val maxHeight = max(inputA.height, inputB.height)
            val output = BufferedImage(totalWidth, maxHeight, BufferedImage.TYPE_INT_ARGB)

            output.graphics.drawImage(inputA, 0, 0, null)
            output.graphics.drawImage(inputB, inputA.width, 0, null)
            ImageIO.write(output, "PNG", outputFile)

            fileA.delete()
            fileB.delete()

            true
        } catch (e: IOException) {
            false
        }

    private suspend fun sendCommand(
        device: Device,
        command: NormalCommand,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            command.requests.forEach { request ->
                val result = adbClient.execute(request, device.serial)
                if (result.exitCode != 0) {
                    return@withContext false
                }
            }
            return@withContext true
        }
    }

    companion object {
        private const val FILE_NAME_A = "screenshotA.png"
        private const val FILE_NAME_B = "screenshotB.png"
        private const val FILE_NAME_RESULT = "Screenshot"
        private const val EXTENSION_NAME = "png"
        private const val DEFAULT_DELAY = 500L

        private fun createDirectory() {
            getDirectory().mkdir()
        }

        private fun getDirectory(): File {
            val osContext = OSContext.resolveOSContext()
            return File(osContext.screenshotDirectory)
        }

        private fun getFileA(): File {
            val osContext = OSContext.resolveOSContext()
            return File(osContext.screenshotDirectory + FILE_NAME_A)
        }

        private fun getFileB(): File {
            val osContext = OSContext.resolveOSContext()
            return File(osContext.screenshotDirectory + FILE_NAME_B)
        }

        private fun getFileResult(time: Long): File {
            val osContext = OSContext.resolveOSContext()
            val fileName = "${FILE_NAME_RESULT}_$time.png"
            return File(osContext.screenshotDirectory + fileName)
        }
    }
}
