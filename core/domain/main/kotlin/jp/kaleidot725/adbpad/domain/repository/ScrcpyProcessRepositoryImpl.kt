package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.scrcpykt.ScrcpyProcess

class ScrcpyProcessRepositoryImpl : ScrcpyProcessRepository {
    private val runningProcesses = mutableMapOf<String, ScrcpyProcess>()

    override fun storeProcess(
        deviceSerial: String,
        process: ScrcpyProcess,
    ) {
        runningProcesses[deviceSerial] = process
    }

    override fun getProcess(deviceSerial: String): ScrcpyProcess? = runningProcesses[deviceSerial]

    override fun removeProcess(deviceSerial: String) {
        runningProcesses.remove(deviceSerial)
    }

    override fun terminateAllProcesses() {
        println("Terminating all Scrcpy processes...")
        val devices = runningProcesses.keys.toList()
        devices.forEach { deviceSerial ->
            try {
                val process = runningProcesses[deviceSerial]
                process?.terminate()
                removeProcess(deviceSerial)
                println("Terminated Scrcpy process for device: $deviceSerial")
            } catch (e: Exception) {
                println("Error terminating Scrcpy process for device $deviceSerial: ${e.message}")
            }
        }
    }
}
