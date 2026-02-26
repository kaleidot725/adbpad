package jp.kaleidot725.adbpad.domain.model.command

import com.malinskiy.adam.request.shell.v1.ShellCommandRequest

interface DeviceControlCommand {
    val requests: List<ShellCommandRequest>

    data object Power : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 26"),
            )
    }

    data object VolumeUp : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 24"),
            )
    }

    data object VolumeDown : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 25"),
            )
    }

    data object VolumeMute : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 164"),
            )
    }

    data object Back : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 4"),
            )
    }

    data object Home : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 3"),
            )
    }

    data object Recents : DeviceControlCommand {
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("input keyevent 187"),
            )
    }
}
