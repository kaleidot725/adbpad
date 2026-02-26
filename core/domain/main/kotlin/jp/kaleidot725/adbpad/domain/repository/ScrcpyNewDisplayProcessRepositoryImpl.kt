package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.scrcpykt.ScrcpyProcess

class ScrcpyNewDisplayProcessRepositoryImpl : ScrcpyNewDisplayProcessRepository {
    private data class EntryKey(
        val deviceSerial: String,
        val profileId: String,
    )

    private val runningProcesses = mutableMapOf<EntryKey, ScrcpyProcess>()

    override fun storeProcess(
        deviceSerial: String,
        profileId: String,
        process: ScrcpyProcess,
    ) {
        val key = EntryKey(deviceSerial, profileId)
        runningProcesses[key] = process
    }

    override fun getProcess(
        deviceSerial: String,
        profileId: String,
    ): ScrcpyProcess? {
        val key = EntryKey(deviceSerial, profileId)
        return runningProcesses[key]
    }

    override fun removeProcess(
        deviceSerial: String,
        profileId: String,
    ) {
        val key = EntryKey(deviceSerial, profileId)
        runningProcesses.remove(key)
    }

    override fun terminateAllProcesses() {
        println("Terminating all Scrcpy new-display processes...")
        val snapshot = runningProcesses.toMap()
        snapshot.forEach { (key, process) ->
            try {
                process.terminate()
                println(
                    "Terminated Scrcpy new-display process for device: ${key.deviceSerial} profile: ${key.profileId}",
                )
            } catch (e: Exception) {
                println(
                    "Error terminating Scrcpy new-display process for device ${key.deviceSerial} profile ${key.profileId}: ${e.message}",
                )
            } finally {
                runningProcesses.remove(key)
            }
        }
    }
}
