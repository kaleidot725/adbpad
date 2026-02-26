package jp.kaleidot725.adbpad.data.local

import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ScrcpyNewDisplayProfileFileCreator {
    private val fileName = "scrcpy_profiles.json"
    private val json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }

    fun load(): List<ScrcpyNewDisplayProfile> {
        return try {
            val file = FilePathUtil.getFilePath(fileName)
            if (!file.exists()) return emptyList()
            json.decodeFromString(file.readText())
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun save(profiles: List<ScrcpyNewDisplayProfile>) {
        try {
            FilePathUtil.createDir()
            val file = FilePathUtil.getFilePath(fileName)
            file.writeText(json.encodeToString(profiles))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
