package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.setting.AccentColor
import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.domain.model.setting.ScrcpySettings
import jp.kaleidot725.adbpad.domain.model.setting.SdkPath
import jp.kaleidot725.adbpad.domain.model.setting.WindowSize

interface SettingRepository {
    suspend fun updateAppearance(appearance: Appearance): Boolean

    suspend fun getAppearance(): Appearance

    suspend fun updateWindowSize(size: WindowSize): Boolean

    suspend fun getWindowSize(): WindowSize

    suspend fun updateSdkPath(sdkPath: SdkPath): Boolean

    suspend fun getSdkPath(): SdkPath

    suspend fun updateLanguage(language: Language.Type): Boolean

    suspend fun getLanguage(): Language.Type

    suspend fun updateScrcpySettings(scrcpySettings: ScrcpySettings): Boolean

    suspend fun getScrcpySettings(): ScrcpySettings

    suspend fun updateAccentColor(accentColor: AccentColor): Boolean

    suspend fun getAccentColor(): AccentColor
}
