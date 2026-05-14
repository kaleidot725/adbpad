package jp.kaleidot725.adbpad.ui.screen.app.component

import java.util.Locale

internal fun formatAppFileSize(size: Long): String {
    if (size < 1024L) return "$size B"

    val units = listOf("KB", "MB", "GB", "TB")
    var value = size / 1024.0
    var unitIndex = 0
    while (value >= 1024.0 && unitIndex < units.lastIndex) {
        value /= 1024.0
        unitIndex++
    }

    return String.format(Locale.getDefault(), "%.1f %s", value, units[unitIndex])
}
