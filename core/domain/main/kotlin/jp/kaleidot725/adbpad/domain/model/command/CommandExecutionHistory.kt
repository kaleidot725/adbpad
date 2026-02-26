package jp.kaleidot725.adbpad.domain.model.command

data class CommandExecutionHistory(
    val command: String,
    val output: String,
)
