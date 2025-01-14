package jp.kaleidot725.adbpad.ui.screen.screenshot

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.UserColor
import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotGallery
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotMenu
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotViewer

@Composable
fun ScreenshotScreen(
    screenshot: Screenshot,
    screenshots: List<Screenshot>,
    canCapture: Boolean,
    isCapturing: Boolean,
    commands: List<ScreenshotCommand>,
    onOpenDirectory: () -> Unit,
    onCopyScreenshot: () -> Unit,
    onDeleteScreenshot: () -> Unit,
    onTakeScreenshot: (ScreenshotCommand) -> Unit,
    onSelectScreenshot: (Screenshot) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1.0f)
                    .border(
                        border = BorderStroke(1.dp, UserColor.getSplitterColor()),
                        shape = RoundedCornerShape(4.dp),
                    ),
        ) {
            ScreenshotViewer(
                screenshot = screenshot,
                isCapturing = isCapturing,
                onOpenDirectory = onOpenDirectory,
                onCopyScreenshot = onCopyScreenshot,
                onDeleteScreenshot = onDeleteScreenshot,
                modifier =
                    Modifier
                        .weight(1.0f)
                        .fillMaxHeight(),
            )

            if (screenshots.isNotEmpty()) {
                Spacer(Modifier.width(1.dp).fillMaxHeight().border(BorderStroke(1.dp, UserColor.getSplitterColor())))

                ScreenshotGallery(
                    selectedScreenshot = screenshot,
                    screenshots = screenshots,
                    onSelectScreenShot = onSelectScreenshot,
                    modifier =
                        Modifier
                            .wrapContentWidth()
                            .fillMaxHeight(),
                )
            }
        }

        ScreenshotMenu(
            commands = commands,
            canCapture = canCapture,
            isCapturing = isCapturing,
            onTakeScreenshot = onTakeScreenshot,
            modifier = Modifier.wrapContentSize().align(Alignment.End),
        )
    }
}

@Composable
@Preview
private fun ScreenshotScreen_Preview() {
    ScreenshotScreen(
        screenshot = Screenshot(null),
        screenshots = emptyList(),
        canCapture = true,
        isCapturing = false,
        commands = emptyList(),
        onOpenDirectory = {},
        onCopyScreenshot = {},
        onDeleteScreenshot = {},
        onTakeScreenshot = {},
        onSelectScreenshot = {},
    )
}
