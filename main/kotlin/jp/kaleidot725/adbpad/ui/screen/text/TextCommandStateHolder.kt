package jp.kaleidot725.adbpad.ui.screen.text

import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.repository.TextCommandRepository
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.text.ExecuteTextCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.text.GetTextCommandUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandAction
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandSideEffect
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.launch

class TextCommandStateHolder(
    private val textCommandRepository: TextCommandRepository,
    private val getTextCommandUseCase: GetTextCommandUseCase,
    private val executeTextCommandUseCase: ExecuteTextCommandUseCase,
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
) : PulseStore<TextCommandState, TextCommandAction, TextCommandSideEffect, AppBroadCast>(initialUiState = TextCommandState()) {
    override fun onSetup() {
        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collect {
                update { this.copy(selectedDevice = it) }
            }
        }
        coroutineScope.launch {
            val commands =
                getTextCommandUseCase(
                    searchText = currentState.searchText,
                    sortType = currentState.sortType,
                )
            update {
                val currentId = selectedCommandId
                val isValid = commands.any { it.id == currentId }
                val nextSelectedId = if (isValid) currentId else commands.firstOrNull()?.id
                this.copy(commands = commands, selectedCommandId = nextSelectedId)
            }
        }
    }

    override fun onAction(uiAction: TextCommandAction) {
        coroutineScope.launch {
            when (uiAction) {
                is TextCommandAction.DeleteSelectedCommandText -> {
                    deleteInputText()
                }

                is TextCommandAction.DeleteCommandText -> {
                    deleteCommandText(uiAction.command)
                }

                is TextCommandAction.SendTextCommand -> {
                    sendTextCommand()
                }

                is TextCommandAction.NextCommand -> {
                    nextCommand()
                }

                is TextCommandAction.PreviousCommand -> {
                    previousCommand()
                }

                is TextCommandAction.SelectCommand -> {
                    selectCommand(uiAction.command)
                }

                TextCommandAction.AddNewText -> {
                    addNewTextCommand()
                }

                is TextCommandAction.UpdateSearchText -> {
                    updateSearchText(uiAction.text)
                }

                is TextCommandAction.UpdateCommandText -> {
                    updateTextCommandValue(uiAction.id, uiAction.value)
                }

                is TextCommandAction.UpdateCommandTitle -> {
                    updateTextCommandTitle(uiAction.id, uiAction.value)
                }

                is TextCommandAction.UpdateTextCommandOption -> {
                    updateTextCommandOption(uiAction.value)
                }

                is TextCommandAction.UpdateSortType -> {
                    updateSortType(uiAction.type)
                }
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {
                coroutineScope.launch {
                    val commands =
                        getTextCommandUseCase(
                            searchText = currentState.searchText,
                            sortType = currentState.sortType,
                        )
                    update {
                        val currentId = selectedCommandId
                        val isValid = commands.any { it.id == currentId }
                        val nextSelectedId = if (isValid) currentId else commands.firstOrNull()?.id
                        this.copy(commands = commands, selectedCommandId = nextSelectedId)
                    }
                }
            }
        }
    }

    private suspend fun updateSearchText(searchText: String) {
        val commands =
            getTextCommandUseCase(
                searchText = searchText,
                sortType = currentState.sortType,
            )
        update {
            copy(
                searchText = searchText,
                commands = commands,
            )
        }
    }

    private suspend fun updateSortType(type: SortType) {
        val commands =
            getTextCommandUseCase(
                searchText = currentState.searchText,
                sortType = type,
            )
        update {
            copy(
                sortType = type,
                commands = commands,
            )
        }
    }

    private val ascii = (0..255).map { it.toChar() }

    private suspend fun updateTextCommandValue(
        id: String,
        value: String,
    ) {
        val isAscii = value.none { it !in ascii }
        if (isAscii) {
            textCommandRepository.updateTextCommandValue(id, value)
            val commands =
                getTextCommandUseCase(
                    searchText = currentState.searchText,
                    sortType = currentState.sortType,
                )
            update { copy(commands = commands) }
        }
    }

    private suspend fun updateTextCommandTitle(
        id: String,
        value: String,
    ) {
        textCommandRepository.updateTextCommandTitle(id, value)
        val commands =
            getTextCommandUseCase(
                searchText = currentState.searchText,
                sortType = currentState.sortType,
            )
        update { copy(commands = commands) }
    }

    private suspend fun sendTextCommand() {
        val selectedDevice = currentState.selectedDevice ?: return
        val selectedCommand = currentState.selectedCommand ?: return

        executeTextCommandUseCase(
            device = selectedDevice,
            command = selectedCommand,
            onStart = {
                val commands =
                    getTextCommandUseCase(
                        searchText = currentState.searchText,
                        sortType = currentState.sortType,
                    )
                update { copy(commands = commands) }
            },
            onFailed = {
                val commands =
                    getTextCommandUseCase(
                        searchText = currentState.searchText,
                        sortType = currentState.sortType,
                    )
                update { copy(commands = commands) }
            },
            onComplete = {
                val commands =
                    getTextCommandUseCase(
                        searchText = currentState.searchText,
                        sortType = currentState.sortType,
                    )
                update { copy(commands = commands) }
            },
        )
    }

    private suspend fun addNewTextCommand() {
        val command =
            TextCommand(
                title = "",
                text = "",
            )
        textCommandRepository.addTextCommand(command)
        val commands =
            getTextCommandUseCase(
                searchText = currentState.searchText,
                sortType = currentState.sortType,
            )

        update {
            copy(
                commands = commands,
                selectedCommandId = command.id,
            )
        }
    }

    private suspend fun deleteInputText() {
        val selectedCommand = currentState.selectedCommand ?: return
        val selectedCommandIndex = currentState.commands.indexOf(selectedCommand)
        textCommandRepository.removeTextCommand(selectedCommand)

        val commands =
            getTextCommandUseCase(
                searchText = currentState.searchText,
                sortType = currentState.sortType,
            )

        if (commands.isEmpty()) {
            update {
                copy(
                    commands = commands,
                    selectedCommandId = null,
                )
            }
        } else {
            val newSelectedCommand = commands.getOrNull(selectedCommandIndex)
            val newSelectedCommandIndex = if (newSelectedCommand == null) commands.lastIndex else selectedCommandIndex
            val newSelectedCommandId = commands[newSelectedCommandIndex].id
            update {
                copy(
                    commands = commands,
                    selectedCommandId = newSelectedCommandId,
                )
            }
        }
    }

    private fun nextCommand() {
        val nextIndex = currentState.commands.indexOf(currentState.selectedCommand) + 1
        if (0 <= nextIndex && nextIndex <= currentState.commands.lastIndex) {
            val id = currentState.commands[nextIndex].id
            update { copy(selectedCommandId = id) }
        }
    }

    private fun previousCommand() {
        val previousIndex = currentState.commands.indexOf(currentState.selectedCommand) - 1
        if (0 <= previousIndex && previousIndex <= currentState.commands.lastIndex) {
            val id = currentState.commands[previousIndex].id
            update { copy(selectedCommandId = id) }
        }
    }

    private fun selectCommand(command: TextCommand) {
        val index = currentState.commands.indexOf(command)
        val id = currentState.commands[index].id
        update { copy(selectedCommandId = id) }
    }

    private suspend fun updateTextCommandOption(value: TextCommand.Option) {
        val selectedCommandId = currentState.selectedCommandId ?: return
        textCommandRepository.updateTextCommandOption(selectedCommandId, value)
        val commands =
            getTextCommandUseCase(
                searchText = currentState.searchText,
                sortType = currentState.sortType,
            )
        update { copy(commands = commands) }
    }

    private suspend fun deleteCommandText(command: TextCommand) {
        val commandIndex = currentState.commands.indexOf(command)
        textCommandRepository.removeTextCommand(command)

        val commands =
            getTextCommandUseCase(
                searchText = currentState.searchText,
                sortType = currentState.sortType,
            )

        if (commands.isEmpty()) {
            update {
                copy(
                    commands = commands,
                    selectedCommandId = null,
                )
            }
        } else {
            val currentSelectedId = currentState.selectedCommandId
            val wasSelectedCommandDeleted = command.id == currentSelectedId

            if (wasSelectedCommandDeleted) {
                val newSelectedCommand = commands.getOrNull(commandIndex)
                val newSelectedCommandIndex = if (newSelectedCommand == null) commands.lastIndex else commandIndex
                val newSelectedCommandId = commands[newSelectedCommandIndex].id
                update {
                    copy(
                        commands = commands,
                        selectedCommandId = newSelectedCommandId,
                    )
                }
            } else {
                update {
                    copy(
                        commands = commands,
                    )
                }
            }
        }
    }
}
