package jp.kaleidot725.adbpad.domain.model.command

import jp.kaleidot725.adbpad.domain.model.language.Language

interface ScreenshotCommand {
    val title: String

    data object Dark : ScreenshotCommand {
        override val title: String get() = Language.screenshotTakeByDarkTheme
    }

    data object Light : ScreenshotCommand {
        override val title: String get() = Language.screenshotTakeByLightTheme
    }

    data object Current : ScreenshotCommand {
        override val title: String get() = Language.screenshotTakeByCurrentTheme
    }

    data object Both : ScreenshotCommand {
        override val title: String get() = Language.screenshotTakeByBothTheme
    }
}
