package jp.kaleidot725.adbpad.ui.component.text

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DefaultOutlineTextField(
    id: Any? = null,
    label: String,
    initialText: String,
    placeHolder: String,
    isError: Boolean,
    onUpdateText: (String) -> Unit,
    maxLines: Int = 1,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    var localText by remember(id) { mutableStateOf(initialText) }
    val textStyle = MaterialTheme.typography.bodySmall
    OutlinedTextField(
        label = { Text(label, style = textStyle) },
        placeholder = { Text(placeHolder, style = textStyle) },
        value = localText,
        onValueChange = {
            localText = it
            onUpdateText(it)
        },
        maxLines = maxLines,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
        isError = isError,
        enabled = enabled,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    DefaultOutlineTextField(
        initialText = "",
        placeHolder = "Search",
        onUpdateText = {},
        label = "LABEL",
        isError = false,
    )
}
