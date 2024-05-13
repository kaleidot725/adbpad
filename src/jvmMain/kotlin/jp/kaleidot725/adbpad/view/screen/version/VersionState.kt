package jp.kaleidot725.adbpad.view.screen.version

import com.google.common.collect.ImmutableList
import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.version.Version

data class VersionState(
    val versions: List<Version> = emptyList(),
    val isLoading: Boolean = true,
    val hasError: Boolean = false
)