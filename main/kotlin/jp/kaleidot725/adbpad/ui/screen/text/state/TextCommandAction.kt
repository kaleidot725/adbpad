package jp.kaleidot725.adbpad.ui.screen.text.state

import jp.kaleidot725.pulse.mvi.PulseAction
import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.sort.SortType

sealed class TextCommandAction : PulseAction {
    data class UpdateSearchText(
        val text: String,
    ) : TextCommandAction()

    data object AddNewText : TextCommandAction()

    data class UpdateSortType(
        val type: SortType,
    ) : TextCommandAction()

    data class UpdateCommandTitle(
        val id: String,
        val value: String,
    ) : TextCommandAction()

    data class UpdateCommandText(
        val id: String,
        val value: String,
    ) : TextCommandAction()

    data object SendTextCommand : TextCommandAction()

    data object DeleteSelectedCommandText : TextCommandAction()

    data class DeleteCommandText(
        val command: TextCommand,
    ) : TextCommandAction()

    data object NextCommand : TextCommandAction()

    data class SelectCommand(
        val command: TextCommand,
    ) : TextCommandAction()

    data object PreviousCommand : TextCommandAction()

    data class UpdateTextCommandOption(
        val value: TextCommand.Option,
    ) : TextCommandAction()
}
