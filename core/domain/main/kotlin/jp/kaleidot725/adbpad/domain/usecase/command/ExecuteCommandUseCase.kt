package jp.kaleidot725.adbpad.domain.usecase.command

import jp.kaleidot725.adbpad.domain.model.command.CommandExecutionHistory
import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.NormalCommandOutputRepository
import jp.kaleidot725.adbpad.domain.repository.NormalCommandRepository

class ExecuteCommandUseCase(
    private val normalCommandRepository: NormalCommandRepository,
    private val normalCommandOutputRepository: NormalCommandOutputRepository,
) {
    suspend operator fun invoke(
        device: Device,
        command: NormalCommand,
        onStart: suspend () -> Unit,
        onFailed: suspend () -> Unit,
        onComplete: suspend () -> Unit,
    ) {
        normalCommandRepository.sendCommand(
            device = device,
            command = command,
            onStart = {
                normalCommandOutputRepository.clear()
                onStart()
            },
            onFailed = { formattedCommand, output ->
                normalCommandOutputRepository.setExecutionHistory(
                    CommandExecutionHistory(
                        command = formattedCommand,
                        output = output,
                    ),
                )
                onFailed()
            },
            onComplete = { formattedCommand, output ->
                normalCommandOutputRepository.setExecutionHistory(
                    CommandExecutionHistory(
                        command = formattedCommand,
                        output = output,
                    ),
                )
                onComplete()
            },
        )
    }
}
