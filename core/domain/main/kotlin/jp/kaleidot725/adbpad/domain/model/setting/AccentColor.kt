package jp.kaleidot725.adbpad.domain.model.setting

import androidx.compose.ui.graphics.Color
import jp.kaleidot725.adbpad.domain.model.language.Language

enum class AccentColor(
    val lightColor: Color,
    val darkColor: Color,
) {
    BLUE(Color(0xFF2196F3), Color(0xFF64B5F6)),
    PURPLE(Color(0xFF9C27B0), Color(0xFFBA68C8)),
    GREEN(Color(0xFF4CAF50), Color(0xFF81C784)),
    ORANGE(Color(0xFFFF9800), Color(0xFFFFB74D)),
    RED(Color(0xFFF44336), Color(0xFFE57373)),
    TEAL(Color(0xFF009688), Color(0xFF4DB6AC)),
    INDIGO(Color(0xFF3F51B5), Color(0xFF7986CB)),
    ;

    fun getColor(isLight: Boolean): Color = if (isLight) lightColor else darkColor

    fun getTitle(): String =
        when (this) {
            BLUE -> Language.accentColorBlue
            PURPLE -> Language.accentColorPurple
            GREEN -> Language.accentColorGreen
            ORANGE -> Language.accentColorOrange
            RED -> Language.accentColorRed
            TEAL -> Language.accentColorTeal
            INDIGO -> Language.accentColorIndigo
        }
}
