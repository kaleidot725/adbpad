package jp.kaleidot725.adbpad.domain.model.setting

import jp.kaleidot725.adbpad.domain.model.language.Language

enum class Appearance(
    val value: String,
) {
    DARK(Language.dark),
    LIGHT(Language.light),
    SYSTEM(Language.system),
}
