package jp.kaleidot725.adbpad.ui.screen.newdisplay.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.device.ScrcpyOptions
import jp.kaleidot725.adbpad.ui.common.resource.UserColor

@Composable
fun ScrcpyNewDisplayMenu(
    isEditable: Boolean,
    selectedProfileId: String?,
    inputWidth: String,
    inputHeight: String,
    inputDpi: String,
    scrcpyOptions: ScrcpyOptions,
    isLaunching: Boolean,
    canLaunch: Boolean,
    onUpdateInputWidth: (String) -> Unit,
    onUpdateInputHeight: (String) -> Unit,
    onUpdateInputDpi: (String) -> Unit,
    onUpdateScrcpyOptions: (ScrcpyOptions) -> Unit,
    onLaunch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 50.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ScrcpyNewDisplayOptions(
                isEditable = isEditable,
                selectedProfileId = selectedProfileId,
                inputWidth = inputWidth,
                inputHeight = inputHeight,
                inputDpi = inputDpi,
                scrcpyOptions = scrcpyOptions,
                onUpdateInputWidth = onUpdateInputWidth,
                onUpdateInputHeight = onUpdateInputHeight,
                onUpdateInputDpi = onUpdateInputDpi,
                onUpdateScrcpyOptions = onUpdateScrcpyOptions,
            )

            Spacer(Modifier.height(100.dp))
        }

        Column(
            Modifier.align(Alignment.BottomCenter),
        ) {
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = UserColor.getSplitterColor())

            Spacer(Modifier.height(12.dp))

            ScrcpyLaunchButton(
                isLaunching = isLaunching,
                canLaunch = canLaunch,
                onLaunch = onLaunch,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun ScrcpyNewDisplayMenuPreview() {
    ScrcpyNewDisplayMenu(
        isEditable = true,
        selectedProfileId = "sample",
        inputWidth = "1080",
        inputHeight = "1920",
        inputDpi = "440",
        scrcpyOptions = ScrcpyOptions(),
        isLaunching = false,
        canLaunch = true,
        onUpdateInputWidth = {},
        onUpdateInputHeight = {},
        onUpdateInputDpi = {},
        onUpdateScrcpyOptions = {},
        onLaunch = {},
    )
}
