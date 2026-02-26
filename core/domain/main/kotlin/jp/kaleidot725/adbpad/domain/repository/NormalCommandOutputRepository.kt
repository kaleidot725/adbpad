package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.command.CommandExecutionHistory
import kotlinx.coroutines.flow.StateFlow

interface NormalCommandOutputRepository {
    val executionHistory: StateFlow<CommandExecutionHistory?>

    suspend fun setExecutionHistory(history: CommandExecutionHistory)

    fun clear()
}
