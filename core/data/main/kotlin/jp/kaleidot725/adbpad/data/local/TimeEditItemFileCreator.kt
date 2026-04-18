package jp.kaleidot725.adbpad.data.local

import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

object TimeEditItemFileCreator {
    private const val FILE_NAME = "time_edit_items.json"
    private val json = Json { ignoreUnknownKeys = true }

    fun save(setting: TimeEditItemSetting): Boolean =
        try {
            FilePathUtil.createDir()
            FilePathUtil.getFilePath(FILE_NAME).outputStream().apply {
                this.write(json.encodeToString(setting).toByteArray())
                this.close()
            }
            true
        } catch (_: IOException) {
            false
        }

    fun load(): TimeEditItemSetting =
        try {
            val content = FilePathUtil.getFilePath(FILE_NAME).readText()
            json.decodeFromString(string = content)
        } catch (_: Exception) {
            TimeEditItemSetting()
        }

    @Serializable
    data class TimeEditItemSetting(
        val values: List<TimeEditItem> = emptyList(),
    )
}
