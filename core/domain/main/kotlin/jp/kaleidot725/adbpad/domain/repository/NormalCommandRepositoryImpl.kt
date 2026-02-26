package jp.kaleidot725.adbpad.domain.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class NormalCommandRepositoryImpl : NormalCommandRepository {
    private val runningCommands: MutableSet<NormalCommand> = mutableSetOf()
    private val adbClient = AndroidDebugBridgeClientFactory().build()

    override fun getCommands(): List<NormalCommand> =
        listOf(
            NormalCommand.PointerLocationOn(runningCommands.any { it is NormalCommand.PointerLocationOn }),
            NormalCommand.PointerLocationOff(runningCommands.any { it is NormalCommand.PointerLocationOff }),
            NormalCommand.LayoutBorderOn(runningCommands.any { it is NormalCommand.LayoutBorderOn }),
            NormalCommand.LayoutBorderOff(runningCommands.any { it is NormalCommand.LayoutBorderOff }),
            NormalCommand.TapEffectOn(runningCommands.any { it is NormalCommand.TapEffectOn }),
            NormalCommand.TapEffectOff(runningCommands.any { it is NormalCommand.TapEffectOff }),
            NormalCommand.SleepModeOff(runningCommands.any { it is NormalCommand.SleepModeOff }),
            NormalCommand.SleepModeOn(runningCommands.any { it is NormalCommand.SleepModeOn }),
            NormalCommand.DarkThemeOn(runningCommands.any { it is NormalCommand.DarkThemeOn }),
            NormalCommand.DarkThemeOff(runningCommands.any { it is NormalCommand.DarkThemeOff }),
            NormalCommand.WifiOn(runningCommands.any { it is NormalCommand.WifiOn }),
            NormalCommand.WifiOff(runningCommands.any { it is NormalCommand.WifiOff }),
            NormalCommand.DataOn(runningCommands.any { it is NormalCommand.DataOn }),
            NormalCommand.DataOff(runningCommands.any { it is NormalCommand.DataOff }),
            NormalCommand.WifiAndDataOn(runningCommands.any { it is NormalCommand.WifiAndDataOn }),
            NormalCommand.WifiAndDataOff(runningCommands.any { it is NormalCommand.WifiAndDataOff }),
            NormalCommand.ScreenPinningOff(runningCommands.any { it is NormalCommand.ScreenPinningOff }),
            NormalCommand.EnableGestureNavigation(runningCommands.any { it is NormalCommand.EnableGestureNavigation }),
            NormalCommand.EnableTwoButtonNavigation(runningCommands.any { it is NormalCommand.EnableTwoButtonNavigation }),
            NormalCommand.EnableThreeButtonNavigation(runningCommands.any { it is NormalCommand.EnableThreeButtonNavigation }),
            NormalCommand.AirplaneModeOn(runningCommands.any { it is NormalCommand.AirplaneModeOn }),
            NormalCommand.AirplaneModeOff(runningCommands.any { it is NormalCommand.AirplaneModeOff }),
            NormalCommand.BluetoothOn(runningCommands.any { it is NormalCommand.BluetoothOn }),
            NormalCommand.BluetoothOff(runningCommands.any { it is NormalCommand.BluetoothOff }),
            NormalCommand.LocationOn(runningCommands.any { it is NormalCommand.LocationOn }),
            NormalCommand.LocationOff(runningCommands.any { it is NormalCommand.LocationOff }),
            NormalCommand.AnimationsOn(runningCommands.any { it is NormalCommand.AnimationsOn }),
            NormalCommand.AnimationsOff(runningCommands.any { it is NormalCommand.AnimationsOff }),
            NormalCommand.AutoRotateOn(runningCommands.any { it is NormalCommand.AutoRotateOn }),
            NormalCommand.AutoRotateOff(runningCommands.any { it is NormalCommand.AutoRotateOff }),
            NormalCommand.FontScaleSmall(runningCommands.any { it is NormalCommand.FontScaleSmall }),
            NormalCommand.FontScaleNormal(runningCommands.any { it is NormalCommand.FontScaleNormal }),
            NormalCommand.FontScaleLarge(runningCommands.any { it is NormalCommand.FontScaleLarge }),
            NormalCommand.FontScaleHuge(runningCommands.any { it is NormalCommand.FontScaleHuge }),
            NormalCommand.FontScaleExtraLarge(runningCommands.any { it is NormalCommand.FontScaleExtraLarge }),
            NormalCommand.FontScaleMaximum(runningCommands.any { it is NormalCommand.FontScaleMaximum }),
            NormalCommand.RtlLayoutOn(runningCommands.any { it is NormalCommand.RtlLayoutOn }),
            NormalCommand.RtlLayoutOff(runningCommands.any { it is NormalCommand.RtlLayoutOff }),
            NormalCommand.BatterySaverOn(runningCommands.any { it is NormalCommand.BatterySaverOn }),
            NormalCommand.BatterySaverOff(runningCommands.any { it is NormalCommand.BatterySaverOff }),
            NormalCommand.DataSaverOn(runningCommands.any { it is NormalCommand.DataSaverOn }),
            NormalCommand.DataSaverOff(runningCommands.any { it is NormalCommand.DataSaverOff }),
            NormalCommand.DozeModeOn(runningCommands.any { it is NormalCommand.DozeModeOn }),
            NormalCommand.DozeModeOff(runningCommands.any { it is NormalCommand.DozeModeOff }),
            NormalCommand.ScreenRotation0(runningCommands.any { it is NormalCommand.ScreenRotation0 }),
            NormalCommand.ScreenRotation90(runningCommands.any { it is NormalCommand.ScreenRotation90 }),
            NormalCommand.ScreenRotation180(runningCommands.any { it is NormalCommand.ScreenRotation180 }),
            NormalCommand.ScreenRotation270(runningCommands.any { it is NormalCommand.ScreenRotation270 }),
        )

    override suspend fun sendCommand(
        device: Device,
        command: NormalCommand,
        onStart: suspend () -> Unit,
        onComplete: suspend (command: String, output: String) -> Unit,
        onFailed: suspend (command: String, output: String) -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            try {
                runningCommands.add(command)
                onStart()

                delay(300)

                val outputs = mutableListOf<String>()
                val formattedCommand =
                    command.commandStrings.joinToString("\n") { "$ adb shell $it" }

                command.requests.forEach { request ->
                    val result = adbClient.execute(request, device.serial)

                    // 標準出力を取得
                    val output = result.output.trim()
                    if (output.isNotEmpty()) {
                        outputs.add(output)
                    }

                    if (result.exitCode != 0) {
                        runningCommands.remove(command)
                        onFailed(
                            formattedCommand,
                            output.ifEmpty { "Error: Command failed with exit code ${result.exitCode}" },
                        )
                        return@withContext
                    }
                }

                runningCommands.remove(command)
                onComplete(
                    formattedCommand,
                    outputs.joinToString("\n").ifEmpty { "Success" },
                )
            } catch (e: Exception) {
                runningCommands.remove(command)
                onFailed(
                    command.commandStrings.joinToString("\n") { "$ adb shell $it" },
                    "Exception: ${e.message}",
                )
            }
        }
    }

    override fun clear() {
        runningCommands.clear()
    }
}
