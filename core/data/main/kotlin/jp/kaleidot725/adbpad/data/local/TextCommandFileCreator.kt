package jp.kaleidot725.adbpad.data.local

import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

object TextCommandFileCreator {
    private const val FILE_NAME = "text_command.json"

    fun save(setting: TextCommandSetting): Boolean =
        try {
            FilePathUtil.createDir()
            FilePathUtil.getFilePath(FILE_NAME).outputStream().apply {
                this.write(Json.encodeToString(setting).toByteArray())
                this.close()
            }
            true
        } catch (_: IOException) {
            false
        }

    fun load(): TextCommandSetting =
        try {
            val content = FilePathUtil.getFilePath(FILE_NAME).readText()
            Json.decodeFromString(string = content)
        } catch (_: Exception) {
            TextCommandSetting()
        }

    @Serializable
    data class TextCommandSetting(
        val values: List<TextCommand> = emptyList(),
    )
}
