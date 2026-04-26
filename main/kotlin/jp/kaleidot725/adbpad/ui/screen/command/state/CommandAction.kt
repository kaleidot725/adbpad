package jp.kaleidot725.adbpad.ui.screen.command.state

import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.model.command.NormalCommandCategory
import jp.kaleidot725.pulse.mvi.PulseAction

sealed class CommandAction : PulseAction {
    data class ExecuteCommand(
        val command: NormalCommand,
    ) : CommandAction()

    data class ToggleFavorite(
        val command: NormalCommand,
    ) : CommandAction()

    data class ClickCategoryTab(
        val category: NormalCommandCategory,
    ) : CommandAction()

    data object ToggleLayoutMode : CommandAction()
}
