package jp.kaleidot725.adbpad.ui.screen.screenshot.state

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.domain.model.sort.SortType

sealed class ScreenshotAction : PulseAction {
    data class UpdateSearchText(
        val text: String,
    ) : ScreenshotAction()

    data class UpdateSortType(
        val sortType: SortType,
    ) : ScreenshotAction()

    data class SelectScreenshotCommand(
        val command: ScreenshotCommand,
    ) : ScreenshotAction()

    data class TakeScreenshot(
        val command: ScreenshotCommand,
    ) : ScreenshotAction()

    data object OpenDirectory : ScreenshotAction()

    data object CopyScreenshotToClipboard : ScreenshotAction()

    data object DeleteScreenshotToClipboard : ScreenshotAction()

    data object EditScreenshot : ScreenshotAction()

    data class RenameScreenshot(
        val name: String,
        val isRealtime: Boolean = false,
    ) : ScreenshotAction()

    data class DeleteScreenshot(
        val screenshot: Screenshot,
    ) : ScreenshotAction()

    data class SelectScreenshot(
        val screenshot: Screenshot,
    ) : ScreenshotAction()

    data object NextScreenshot : ScreenshotAction()

    data object PreviousScreenshot : ScreenshotAction()

    data object DismissError : ScreenshotAction()
}
