package jp.kaleidot725.adbpad.data.local

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

object NormalCommandFavoriteFileCreator {
    private const val FILE_NAME = "normal_command_favorites.json"

    fun save(favorites: List<String>): Boolean =
        try {
            FilePathUtil.createDir()
            FilePathUtil.getFilePath(FILE_NAME).outputStream().apply {
                this.write(Json.encodeToString(favorites).toByteArray())
                this.close()
            }
            true
        } catch (_: IOException) {
            false
        }

    fun load(): List<String> =
        try {
            val content = FilePathUtil.getFilePath(FILE_NAME).readText()
            Json.decodeFromString(string = content)
        } catch (_: Exception) {
            emptyList()
        }
}
