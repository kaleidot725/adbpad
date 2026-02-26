package jp.kaleidot725.adbpad.domain.usecase.screenshot

import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.repository.ScreenshotCommandRepository

class GetScreenshotCommandUseCase(
    private val screenshotCommandRepository: ScreenshotCommandRepository,
) {
    operator fun invoke(): List<ScreenshotCommand> = screenshotCommandRepository.getCommands()
}
