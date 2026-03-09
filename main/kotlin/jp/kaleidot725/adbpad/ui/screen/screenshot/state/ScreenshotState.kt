package jp.kaleidot725.adbpad.ui.screen.screenshot.state

import jp.kaleidot725.pulse.mvi.PulseState
import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.domain.model.sort.SortType

data class ScreenshotState(
    val searchText: String = "",
    val preview: Screenshot = Screenshot(null),
    val previews: List<Screenshot> = emptyList(),
    val selectedCommand: ScreenshotCommand = ScreenshotCommand.Both,
    val commands: List<ScreenshotCommand> = emptyList(),
    val selectedDevice: Device? = null,
    val isCapturing: Boolean = false,
    val sortType: SortType = SortType.SORT_BY_NAME_ASC,
    val errorMessage: String? = null,
    val renameResetKey: Int = 0,
) : PulseState {
    val canExecute: Boolean = selectedDevice != null
}
