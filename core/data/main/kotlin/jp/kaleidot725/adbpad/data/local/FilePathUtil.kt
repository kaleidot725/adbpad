package jp.kaleidot725.adbpad.data.local

import jp.kaleidot725.adbpad.domain.model.os.OSContext
import java.io.File

object FilePathUtil {
    fun createDir() {
        try {
            val file = File(getDirPath())
            if (!file.exists()) file.mkdir()
        } catch (_: Exception) {
            return
        }
    }

    fun getFilePath(fileName: String) = File(getDirPath() + fileName)

    private fun getDirPath() = OSContext.resolveOSContext().directory
}
