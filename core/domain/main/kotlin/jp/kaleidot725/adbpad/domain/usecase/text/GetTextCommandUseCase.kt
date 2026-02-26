package jp.kaleidot725.adbpad.domain.usecase.text

import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.repository.TextCommandRepository

class GetTextCommandUseCase(
    private val textCommandRepository: TextCommandRepository,
) {
    suspend operator fun invoke(
        searchText: String,
        sortType: SortType,
    ): List<TextCommand> {
        val commands = textCommandRepository.getAllTextCommand()
        val filteredCommands = commands.filter { it.title.startsWith(searchText) }
        return when (sortType) {
            SortType.SORT_BY_NAME_ASC -> filteredCommands.sortedBy { it.title }
            SortType.SORT_BY_NAME_DESC -> filteredCommands.sortedByDescending { it.title }
        }
    }
}
