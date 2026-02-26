package jp.kaleidot725.adbpad.domain.model.device

import kotlinx.serialization.Serializable

@Serializable
data class DeviceSettings(
    val deviceId: String,
    val customName: String? = null,
    val scrcpyOptions: ScrcpyOptions = ScrcpyOptions(),
)
