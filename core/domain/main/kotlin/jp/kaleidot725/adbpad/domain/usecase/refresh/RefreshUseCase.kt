package jp.kaleidot725.adbpad.domain.usecase.refresh

import jp.kaleidot725.adbpad.domain.repository.NormalCommandRepository
import jp.kaleidot725.adbpad.domain.repository.ScreenshotCommandRepository
import jp.kaleidot725.adbpad.domain.repository.TextCommandRepository

class RefreshUseCase(
    private val commandRepository: NormalCommandRepository,
    private val screenshotCommandRepository: ScreenshotCommandRepository,
    private val textCommandRepository: TextCommandRepository,
) {
    operator fun invoke() {
        commandRepository.clear()
        textCommandRepository.clear()
    }
}
