package jp.kaleidot725.adbpad.domain.usecase.app

import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProcessRepository
import jp.kaleidot725.adbpad.domain.repository.ScrcpyProcessRepository

class ShutdownAppUseCase(
    private val scrcpyProcessRepository: ScrcpyProcessRepository,
    private val scrcpyNewDisplayProcessRepository: ScrcpyNewDisplayProcessRepository,
) {
    operator fun invoke() {
        // Terminate all running Scrcpy processes
        scrcpyProcessRepository.terminateAllProcesses()
        scrcpyNewDisplayProcessRepository.terminateAllProcesses()

        println("Application shutdown complete.")
    }
}
