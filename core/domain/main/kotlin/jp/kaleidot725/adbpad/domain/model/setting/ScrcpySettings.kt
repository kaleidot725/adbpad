package jp.kaleidot725.adbpad.domain.model.setting

import kotlinx.serialization.Serializable

@Serializable
data class ScrcpySettings(
    val binaryPath: String = "",
)
