package jp.kaleidot725.adbpad.domain.model.device

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val serial: String,
    val name: String = "",
    val state: DeviceState,
) {
    val displayName: String = if (name.isNotBlank()) name else serial
}
