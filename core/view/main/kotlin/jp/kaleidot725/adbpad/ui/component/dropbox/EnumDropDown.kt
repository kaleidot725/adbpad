package jp.kaleidot725.adbpad.ui.component.dropbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.ui.common.resource.UserColor

@Composable
fun <T> EnumDropDown(
    selectedValue: T?,
    values: List<T>,
    onValueSelected: (T?) -> Unit,
    displayName: (T?) -> String,
    label: String,
    enabled: Boolean = true,
    showNullOption: Boolean = true,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = displayName(selectedValue),
            onValueChange = { },
            label = { Text(label, style = MaterialTheme.typography.bodySmall) },
            readOnly = true,
            enabled = enabled,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
        )

        // Invisible clickable overlay
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .clickable(enabled = enabled) {
                        if (enabled) expanded = true
                    },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier =
                Modifier
                    .background(
                        color = UserColor.getDropdownBackgroundColor(),
                        shape = MaterialTheme.shapes.extraSmall,
                    ).border(
                        width = 1.dp,
                        color = UserColor.getSplitterColor(),
                        shape = MaterialTheme.shapes.extraSmall,
                    ),
        ) {
            if (showNullOption) {
                DropdownMenuItem(
                    text = { Text(displayName(null), style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onValueSelected(null)
                        expanded = false
                    },
                )
            }

            values.forEach { value ->
                DropdownMenuItem(
                    text = { Text(displayName(value), style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onValueSelected(value)
                        expanded = false
                    },
                )
            }
        }
    }
}
