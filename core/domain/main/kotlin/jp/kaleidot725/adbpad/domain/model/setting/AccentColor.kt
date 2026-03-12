package jp.kaleidot725.adbpad.domain.model.setting

import androidx.compose.ui.graphics.Color
import jp.kaleidot725.adbpad.domain.model.language.Language

enum class AccentColor(
    val lightColor: Color,
    val darkColor: Color,
) {
    BLUE(Color(0xFF4A8FB8), Color(0xFF7AAED4)),
    PURPLE(Color(0xFF8B60A8), Color(0xFFA888C4)),
    GREEN(Color(0xFF4A9068), Color(0xFF78B594)),
    ORANGE(Color(0xFFC47A45), Color(0xFFD4A070)),
    RED(Color(0xFFB85555), Color(0xFFD48888)),
    TEAL(Color(0xFF3D8C8C), Color(0xFF6CB0B0)),
    INDIGO(Color(0xFF5063A0), Color(0xFF8896C4)),
    MONO(Color(0xFF000000), Color(0xFFFFFFFF)),
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
            MONO -> Language.accentColorMono
        }
}
