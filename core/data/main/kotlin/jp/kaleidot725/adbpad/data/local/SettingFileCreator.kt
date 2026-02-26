package jp.kaleidot725.adbpad.data.local

import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.domain.model.setting.ScrcpySettings
import jp.kaleidot725.adbpad.domain.model.setting.SdkPath
import jp.kaleidot725.adbpad.domain.model.setting.WindowSize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException

object SettingFileCreator {
    private const val FILE_NAME = "config.json"

    fun save(setting: Setting): Boolean =
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

    fun load(): Setting =
        try {
            val content = FilePathUtil.getFilePath(FILE_NAME).readText()
            Json.decodeFromString(string = content)
        } catch (_: Exception) {
            Setting()
        }

    @Serializable
    data class Setting(
        val language: Language.Type = Language.Type.ENGLISH,
        val appearance: Appearance = Appearance.LIGHT,
        val sdkPath: SdkPath = SdkPath(),
        val windowSize: WindowSize = WindowSize.DEFAULT,
        val scrcpySettings: ScrcpySettings = ScrcpySettings(),
        val accentColor: AccentColor = AccentColor.BLUE,
    )
}
