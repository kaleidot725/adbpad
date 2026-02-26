package jp.kaleidot725.adbpad.domain.model.setting

import kotlinx.serialization.Serializable

@Serializable
data class SdkPath(
    val adbDirectory: String = "",
    val adbServerPort: Int = 30000,
)
