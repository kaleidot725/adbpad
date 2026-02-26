package jp.kaleidot725.adbpad.domain.usecase.scrcpy

import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.ScrcpyOptions
import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile
import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProcessRepository
import jp.kaleidot725.adbpad.domain.repository.SettingRepository
import jp.kaleidot725.scrcpykt.ScrcpyClient
import jp.kaleidot725.scrcpykt.ScrcpyResult

class LaunchScrcpyNewDisplayUseCase(
    private val settingRepository: SettingRepository,
    private val scrcpyNewDisplayProcessRepository: ScrcpyNewDisplayProcessRepository,
) {
    suspend operator fun invoke(
        device: Device,
        profile: ScrcpyNewDisplayProfile,
        options: ScrcpyOptions,
    ): Boolean {
        val scrcpySettings = settingRepository.getScrcpySettings()
        val adbSettings = settingRepository.getSdkPath()

        if (scrcpySettings.binaryPath.isBlank()) {
            return false
        }

        if (adbSettings.adbDirectory.isBlank()) {
            return false
        }

        scrcpyNewDisplayProcessRepository.getProcess(device.serial, profile.id)?.let { existingProcess ->
            try {
                existingProcess.terminate()
            } finally {
                scrcpyNewDisplayProcessRepository.removeProcess(device.serial, profile.id)
            }
        }

        val client = ScrcpyClient.create(binaryPath = scrcpySettings.binaryPath, adbPath = adbSettings.adbDirectory)
        val title = "${profile.displayName} - Virtual Display"

        val result =
            client.mirror {
                connection {
                    serial(device.serial)
                }
                display {
                    val finalTitle = options.windowTitle ?: title
                    windowTitle(finalTitle)
                    newDisplay(profile.width, profile.height)

                    options.displayId?.let { displayId(it) }
                    options.windowX?.let { x ->
                        options.windowY?.let { y -> windowPosition(x, y) }
                    }
                    options.windowWidth?.let { width ->
                        options.windowHeight?.let { height -> windowSize(width, height) }
                    }
                    if (options.alwaysOnTop) alwaysOnTop()
                    if (options.fullscreen) fullscreen()
                }

                video {
                    options.maxSize?.let { maxSize(it) }
                    options.videoBitRate?.let { bitRate(it) }
                    options.maxFps?.let { maxFps(it) }
                    options.videoCodec?.let { codec(it) }
                    options.videoSource?.let { source(it) }
                    if (options.noVideo) disableVideo()
                }

                audio {
                    if (options.noAudio) {
                        disableAudio()
                    } else {
                        options.audioBitRate?.let { bitRate(it) }
                        options.audioCodec?.let { codec(it) }
                        options.audioSource?.let { source(it) }
                        options.audioBuffer?.let { buffer(it) }
                    }
                }

                control {
                    if (options.stayAwake) stayAwake()
                    if (options.turnScreenOff) turnScreenOff()
                    if (options.powerOffOnClose) powerOffOnClose()
                    if (options.showTouches) showTouches()
                    if (options.disableScreensaver) disableScreensaver()
                }
            }

        return when (result) {
            is ScrcpyResult.Success -> {
                scrcpyNewDisplayProcessRepository.storeProcess(device.serial, profile.id, result.process)
                true
            }

            is ScrcpyResult.Error -> false
        }
    }
}
