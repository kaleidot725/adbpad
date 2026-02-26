package jp.kaleidot725.adbpad.ui.common.resource

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object UserColor {
    @Composable
    fun getSplitterColor(): Color {
        val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5
        return if (isLight) {
            Color(0xFF000000).copy(alpha = 0.12f)
        } else {
            Color(0xFFFFFFFF).copy(alpha = 0.12f)
        }
    }

    @Composable
    fun getFloatingBackgroundColor(): Color {
        val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5
        return if (isLight) {
            Color(0xFFE5E5E5)
        } else {
            Color(0xFF2C2C2C)
        }
    }

    @Composable
    fun getContentBackgroundColor(): Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)

    @Composable
    fun getDropdownBackgroundColor(): Color {
        val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5
        return if (isLight) {
            Light.SURFACE
        } else {
            Dark.SURFACE
        }
    }

    object Light {
        val PRIMARY = Color(0xFF2196F3) // Bright Blue
        val PRIMARY_VARIANT = Color(0xFFE3F2FD) // Light Blue Surface
        val ON_PRIMARY = Color(0xFFFFFFFF) // White
        val SECONDARY = Color(0xFF757575) // Medium Gray
        val SECONDARY_VARIANT = Color(0xFFE0E0E0) // Light Gray
        val ON_SECONDARY = Color(0xFFFFFFFF) // White
        val ERROR = Color(0xFFD32F2F) // Red
        val ON_ERROR = Color(0xFFFFFFFF) // White
        val BACKGROUND = Color(0xFFFAFAFA) // Very Light Gray
        val ON_BACKGROUND = Color(0xFF212121) // Dark Gray
        val SURFACE = Color(0xFFFFFFFF) // Pure White
        val ON_SURFACE = Color(0xFF212121) // Dark Gray
    }

    object Dark {
        val PRIMARY = Color(0xFF64B5F6) // Light Blue
        val PRIMARY_VARIANT = Color(0xFF1E3A8A) // Dark Blue Surface
        val ON_PRIMARY = Color(0xFF000000) // Black
        val SECONDARY = Color(0xFF9E9E9E) // Light Gray
        val SECONDARY_VARIANT = Color(0xFF424242) // Dark Gray
        val ON_SECONDARY = Color(0xFFFFFFFF) // White
        val ERROR = Color(0xFFEF5350) // Light Red
        val ON_ERROR = Color(0xFF000000) // Black
        val BACKGROUND = Color(0xFF121212) // Very Dark Gray
        val ON_BACKGROUND = Color(0xFFE0E0E0) // Light Gray
        val SURFACE = Color(0xFF1E1E1E) // Dark Gray
        val ON_SURFACE = Color(0xFFE0E0E0) // Light Gray
    }
}
