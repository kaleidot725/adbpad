package jp.kaleidot725.adbpad.view.common.menu

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import jp.kaleidot725.adbpad.view.resource.StringRes

@Composable
fun DropDownDeviceMenu(
    selectedDevice: Device?,
    devices: List<Device>,
    onSelectDevice: (Device) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        Box(
            modifier = Modifier
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { if (!expanded && devices.isNotEmpty()) expanded = true }
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = selectedDevice?.serial ?: StringRes.NOT_FOUND_DEVICE,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.fillMaxWidth()
            )

            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Device DropDown Icon",
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            devices.forEach { device ->
                DropdownMenuItem(
                    onClick = {
                        onSelectDevice(device)
                        expanded = false
                    }
                ) {
                    Text(text = device.serial, style = MaterialTheme.typography.subtitle2)
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeviceList_Preview() {
    val sample = Device("DEVICE", DeviceState.DEVICE)
    DropDownDeviceMenu(
        devices = listOf(sample),
        selectedDevice = sample,
        onSelectDevice = {}
    )
}