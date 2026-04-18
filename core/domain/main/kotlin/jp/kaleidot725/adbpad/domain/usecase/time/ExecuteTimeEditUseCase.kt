package jp.kaleidot725.adbpad.domain.usecase.time

import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import jp.kaleidot725.adbpad.domain.model.command.DeviceControlCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem
import jp.kaleidot725.adbpad.domain.repository.DeviceControlCommandRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ExecuteTimeEditUseCase(
    private val deviceControlCommandRepository: DeviceControlCommandRepository,
) {
    suspend operator fun invoke(
        device: Device,
        item: TimeEditItem,
        onStart: suspend () -> Unit = {},
        onComplete: suspend () -> Unit = {},
        onFailed: suspend () -> Unit = {},
    ) {
        val command = item.toCommandOrNull() ?: run {
            onFailed()
            return
        }

        deviceControlCommandRepository.sendCommand(
            device = device,
            command = command,
            onStart = onStart,
            onComplete = onComplete,
            onFailed = onFailed,
        )
    }
}

private fun TimeEditItem.toCommandOrNull(): DeviceControlCommand? {
    val commands =
        buildList {
            if (isAutoTimeZone) {
                add("settings put global auto_time_zone 1")
            } else {
                add("settings put global auto_time_zone 0")
                add("cmd alarm set-timezone '$timeZoneId' || setprop persist.sys.timezone '$timeZoneId'")
            }

            if (isAutoDateTime) {
                add("settings put global auto_time 1")
            } else {
                val localDate = runCatching { LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull() ?: return null
                val localTime = parseLocalTime(time) ?: return null
                val dateArgument = LocalDateTime.of(localDate, localTime).format(DateTimeFormatter.ofPattern("MMddHHmmyyyy.ss"))

                add("settings put global auto_time 0")
                add("date $dateArgument")
            }
        }

    return ShellCommandList(commands)
}

private fun parseLocalTime(value: String): LocalTime? =
    listOf(
        DateTimeFormatter.ofPattern("HH:mm:ss"),
        DateTimeFormatter.ofPattern("HH:mm"),
    ).firstNotNullOfOrNull { formatter ->
        runCatching { LocalTime.parse(value.trim(), formatter) }.getOrNull()
    }?.let { parsed ->
        if (value.count { it == ':' } >= 2) {
            parsed.withNano(0)
        } else {
            parsed.withSecond(0).withNano(0)
        }
    }

private data class ShellCommandList(
    private val commands: List<String>,
) : DeviceControlCommand {
    override val requests: List<ShellCommandRequest> = commands.map(::ShellCommandRequest)
}
