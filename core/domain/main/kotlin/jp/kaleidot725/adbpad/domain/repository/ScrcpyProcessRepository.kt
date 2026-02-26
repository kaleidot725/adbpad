package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.scrcpykt.ScrcpyProcess

interface ScrcpyProcessRepository {
    fun storeProcess(
        deviceSerial: String,
        process: ScrcpyProcess,
    )

    fun getProcess(deviceSerial: String): ScrcpyProcess?

    fun removeProcess(deviceSerial: String)

    fun terminateAllProcesses()
}
