package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.command.CommandExecutionHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NormalCommandOutputRepositoryImpl : NormalCommandOutputRepository {
    private val _executionHistory = MutableStateFlow<CommandExecutionHistory?>(null)
    override val executionHistory: StateFlow<CommandExecutionHistory?> = _executionHistory.asStateFlow()

    override suspend fun setExecutionHistory(history: CommandExecutionHistory) {
        _executionHistory.value = history
    }

    override fun clear() {
        _executionHistory.value = null
    }
}
