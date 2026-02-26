package jp.kaleidot725.adbpad.domain.usecase.screenshot

import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.domain.repository.ScreenshotCommandRepository

class RenameScreenshotUseCase(
    private val repository: ScreenshotCommandRepository,
) {
    suspend operator fun invoke(
        screenshot: Screenshot,
        name: String,
    ): Boolean = repository.rename(screenshot, name)
}
