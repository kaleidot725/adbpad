package jp.kaleidot725.adbpad.domain.usecase.text

import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.repository.TextCommandRepository

class AddTextCommandUseCase(
    private val textCommandRepository: TextCommandRepository,
) {
    suspend operator fun invoke(
        title: String,
        text: String,
    ): Boolean = textCommandRepository.addTextCommand(TextCommand.createNew(title, text))
}
