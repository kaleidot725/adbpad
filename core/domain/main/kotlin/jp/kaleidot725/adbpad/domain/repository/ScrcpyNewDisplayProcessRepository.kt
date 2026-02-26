package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.scrcpykt.ScrcpyProcess

interface ScrcpyNewDisplayProcessRepository {
    fun storeProcess(
        deviceSerial: String,
        profileId: String,
        process: ScrcpyProcess,
    )

    fun getProcess(
        deviceSerial: String,
        profileId: String,
    ): ScrcpyProcess?

    fun removeProcess(
        deviceSerial: String,
        profileId: String,
    )

    fun terminateAllProcesses()
}
