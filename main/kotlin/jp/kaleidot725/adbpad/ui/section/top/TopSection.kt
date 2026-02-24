package jp.kaleidot725.adbpad.ui.section.top

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.composables.icons.lucide.Power
import com.composables.icons.lucide.RefreshCcw
import com.composables.icons.lucide.ScreenShare
import com.composables.icons.lucide.Settings2
import com.composables.icons.lucide.Square
import com.composables.icons.lucide.Triangle
import jp.kaleidot725.adbpad.domain.model.command.DeviceControlCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.DeviceState
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground
import jp.kaleidot725.adbpad.ui.component.button.CommandIconButton
import jp.kaleidot725.adbpad.ui.section.top.component.DropDownDeviceMenu
import jp.kaleidot725.adbpad.ui.section.top.state.TopAction
import jp.kaleidot725.adbpad.ui.section.top.state.TopState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSection(
    topState: TopState,
    onTopAction: (TopAction) -> Unit,
    onOpenDeviceSettings: (Device) -> Unit,
    onRefreshDevices: () -> Unit,
    onToggleNavigationRail: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(50.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Device Selector & Actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TopSectionIconButton(
                    tooltip = "Menu",
                    icon = Lucide.Menu,
                    onClick = onToggleNavigationRail,
                )

                DropDownDeviceMenu(
                    devices = topState.devices,
                    selectedDevice = topState.selectedDevice,
                    onSelectDevice = { onTopAction(TopAction.SelectDevice(it)) },
                    modifier = Modifier.width(200.dp),
                )

                TopSectionIconButton(
                    tooltip = Language.tooltipRefresh,
                    icon = Lucide.RefreshCcw,
                    onClick = onRefreshDevices,
                    rotateOnPress = true,
                )

                TopSectionIconButton(
                    tooltip = Language.tooltipScrcpy,
                    icon = Lucide.ScreenShare,
                    onClick = { onTopAction(TopAction.LaunchScrcpy) },
                )

                TopSectionIconButton(
                    tooltip = Language.tooltipSetting,
                    icon = Lucide.Settings2,
                    onClick = {
                        topState.selectedDevice?.let { onOpenDeviceSettings(it) }
                    },
                    enabled = topState.selectedDevice != null,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Right: Device Control Commands
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val enabled = topState.selectedDevice != null

                CommandTooltip(text = Language.tooltipPower) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Lucide.Power,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.Power)) },
                        padding = 2.dp,
                        enabled = enabled,
                    )
                }

                CommandTooltip(text = Language.tooltipVolumeUp) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Icons.AutoMirrored.Filled.VolumeUp,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.VolumeUp)) },
                        padding = 0.dp,
                        enabled = enabled,
                    )
                }

                CommandTooltip(text = Language.tooltipVolumeDown) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Icons.AutoMirrored.Filled.VolumeDown,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.VolumeDown)) },
                        padding = 0.dp,
                        enabled = enabled,
                    )
                }

                CommandTooltip(text = Language.tooltipVolumeMute) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Icons.AutoMirrored.Filled.VolumeOff,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.VolumeMute)) },
                        padding = 0.dp,
                        enabled = enabled,
                    )
                }

                CommandTooltip(text = Language.tooltipBack) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Lucide.Triangle,
                        degrees = -90f,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.Back)) },
                        padding = 2.dp,
                        enabled = enabled,
                    )
                }

                CommandTooltip(text = Language.tooltipHome) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Lucide.Circle,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.Home)) },
                        padding = 2.dp,
                        enabled = enabled,
                    )
                }

                CommandTooltip(text = Language.tooltipRecents) {
                    CommandIconButton(
                        modifier = Modifier.wrapContentSize(),
                        image = Lucide.Square,
                        onClick = { onTopAction(TopAction.ExecuteCommand(DeviceControlCommand.Recents)) },
                        padding = 2.dp,
                        enabled = enabled,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun TopSectionIconButton(
    tooltip: String,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    rotateOnPress: Boolean = false,
) {
    val backgroundShape = remember { RoundedCornerShape(8.dp) }
    var isPressed by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (rotateOnPress && isPressed) -90f else 0f)
    TooltipArea(
        delayMillis = 300,
        tooltip = {
            Card(
                modifier = Modifier.border(1.dp, UserColor.getSplitterColor()),
            ) {
                Text(
                    text = tooltip,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                )
            }
        },
        tooltipPlacement =
            TooltipPlacement.ComponentRect(
                anchor = Alignment.TopCenter,
                alignment = Alignment.BottomCenter,
                offset = DpOffset(0.dp, 30.dp),
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(backgroundShape)
                    .clickableBackground(isSelected = false, shape = backgroundShape)
                    .onPointerEvent(PointerEventType.Press) { if (rotateOnPress) isPressed = true }
                    .onPointerEvent(PointerEventType.Release) { if (rotateOnPress) isPressed = false }
                    .clickable(enabled = enabled) {
                        if (rotateOnPress) isPressed = false
                        onClick()
                    }.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tooltip,
                modifier = Modifier.size(18.dp).graphicsLayer(rotationZ = rotation),
                tint = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommandTooltip(
    text: String,
    tooltipPlacement: TooltipPlacement =
        TooltipPlacement.ComponentRect(
            anchor = Alignment.TopCenter,
            alignment = Alignment.BottomCenter,
            offset = DpOffset(0.dp, 30.dp),
        ),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TooltipArea(
        delayMillis = 300,
        tooltip = {
            Card(
                modifier = Modifier.border(1.dp, UserColor.getSplitterColor()),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                )
            }
        },
        tooltipPlacement = tooltipPlacement,
        modifier = modifier,
        content = content,
    )
}

@Preview
@Composable
private fun Preview() {
    TopSection(
        topState =
            TopState(
                selectedDevice = Device(serial = "test", name = "Test Device", state = DeviceState.DEVICE),
            ),
        onTopAction = {},
        onOpenDeviceSettings = {},
        onRefreshDevices = {},
        onToggleNavigationRail = {},
    )
}
