package jp.kaleidot725.adbpad.ui.screen.screenshot

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.text.DefaultTextField
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotDetailMenu
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotExplorer
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotHeader
import jp.kaleidot725.adbpad.ui.screen.screenshot.component.ScreenshotViewer
import jp.kaleidot725.adbpad.ui.screen.screenshot.state.ScreenshotAction
import jp.kaleidot725.adbpad.ui.screen.screenshot.state.ScreenshotState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import java.awt.Cursor

fun Modifier.cursorForHorizontalResize(): Modifier = pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun ScreenshotScreen(
    state: ScreenshotState,
    onAction: (ScreenshotAction) -> Unit,
    screenshotSplitPaneState: SplitPaneState,
) {
    ScreenshotScreen(
        screenshot = state.preview,
        splitterState = screenshotSplitPaneState,
        screenshots = state.previews,
        canCapture = state.canExecute,
        isCapturing = state.isCapturing,
        selectCommand = state.selectedCommand,
        commands = state.commands,
        searchText = state.searchText,
        sortType = state.sortType,
        onOpenDirectory = {
            onAction(ScreenshotAction.OpenDirectory)
        },
        onEditScreenshot = {
            onAction(ScreenshotAction.EditScreenshot)
        },
        onCopyScreenshot = {
            onAction(ScreenshotAction.CopyScreenshotToClipboard)
        },
        onDeleteScreenshot = {
            onAction(ScreenshotAction.DeleteScreenshotToClipboard)
        },
        onDeleteSpecificScreenshot = { screenshot ->
            onAction(ScreenshotAction.DeleteScreenshot(screenshot))
        },
        onTakeScreenshot = { screenshot ->
            onAction(ScreenshotAction.TakeScreenshot(screenshot))
        },
        onSelectScreenshot = { screenshot ->
            onAction(ScreenshotAction.SelectScreenshot(screenshot))
        },
        onNextScreenshot = {
            onAction(ScreenshotAction.NextScreenshot)
        },
        onPreviousScreenshot = {
            onAction(ScreenshotAction.PreviousScreenshot)
        },
        onUpdateSearchText = {
            onAction(ScreenshotAction.UpdateSearchText(it))
        },
        onSelectCommand = {
            onAction(ScreenshotAction.SelectScreenshotCommand(it))
        },
        onUpdateSortType = {
            onAction(ScreenshotAction.UpdateSortType(it))
        },
        onRenameScreenshot = { name, isRealtime ->
            onAction(ScreenshotAction.RenameScreenshot(name, isRealtime))
        },
        errorMessage = state.errorMessage,
        renameResetKey = state.renameResetKey,
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
private fun ScreenshotScreen(
    screenshot: Screenshot,
    splitterState: SplitPaneState,
    screenshots: List<Screenshot>,
    canCapture: Boolean,
    isCapturing: Boolean,
    selectCommand: ScreenshotCommand,
    commands: List<ScreenshotCommand>,
    searchText: String,
    sortType: SortType,
    onOpenDirectory: () -> Unit,
    onEditScreenshot: () -> Unit,
    onCopyScreenshot: () -> Unit,
    onDeleteScreenshot: () -> Unit,
    onDeleteSpecificScreenshot: (Screenshot) -> Unit,
    onSelectCommand: (ScreenshotCommand) -> Unit,
    onTakeScreenshot: (ScreenshotCommand) -> Unit,
    onSelectScreenshot: (Screenshot) -> Unit,
    onNextScreenshot: () -> Unit,
    onPreviousScreenshot: () -> Unit,
    onUpdateSearchText: (String) -> Unit,
    onUpdateSortType: (SortType) -> Unit,
    onRenameScreenshot: (String, Boolean) -> Unit,
    errorMessage: String?,
    renameResetKey: Int,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        HorizontalSplitPane(
            splitPaneState = splitterState,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
        ) {
            first(minSize = 200.dp) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        ScreenshotHeader(
                            searchText = searchText,
                            sortType = sortType,
                            onUpdateSortType = onUpdateSortType,
                            onUpdateSearchText = onUpdateSearchText,
                            selectedCommand = selectCommand,
                            onSelectCommand = onSelectCommand,
                            commands = commands,
                            canCapture = canCapture,
                            isCapturing = isCapturing,
                            onTakeScreenshot = onTakeScreenshot,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        )

                        HorizontalDivider(color = UserColor.getSplitterColor())

                        ScreenshotExplorer(
                            selectedScreenshot = screenshot,
                            screenshots = screenshots,
                            onSelectScreenShot = onSelectScreenshot,
                            onDeleteScreenshot = onDeleteSpecificScreenshot,
                            onNextScreenshot = onNextScreenshot,
                            onPreviousScreenshot = onPreviousScreenshot,
                            modifier =
                                Modifier
                                    .weight(1.0f)
                                    .fillMaxWidth(),
                        )
                    }
                }
            }

            second {
                Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    Column(modifier = Modifier.weight(1.0f).fillMaxHeight()) {
                        if (screenshot.file != null) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                DefaultTextField(
                                    id = "screenshot-name-${screenshot.file!!.absolutePath}-$renameResetKey",
                                    initialText = screenshot.file!!.nameWithoutExtension,
                                    onUpdateText = { newText ->
                                        onRenameScreenshot(newText, true)
                                    },
                                    onConfirm = { newText ->
                                        onRenameScreenshot(newText, false)
                                    },
                                    errorMessage = errorMessage,
                                    placeHolder = Language.screenshotNamePlaceholder,
                                    modifier = Modifier.weight(1.0f).height(48.dp).padding(horizontal = 12.dp),
                                )
                            }
                            HorizontalDivider(color = UserColor.getSplitterColor())
                        }

                        ScreenshotViewer(
                            screenshot = screenshot,
                            isCapturing = isCapturing,
                            onOpenDirectory = onOpenDirectory,
                            onEditScreenshot = onEditScreenshot,
                            onCopyScreenshot = onCopyScreenshot,
                            modifier = Modifier.weight(1.0f).fillMaxWidth(),
                        )
                    }

                    androidx.compose.material3.VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = UserColor.getSplitterColor(),
                    )

                    ScreenshotDetailMenu(
                        screenshot = screenshot,
                        onOpenDirectory = onOpenDirectory,
                        onEditScreenshot = onEditScreenshot,
                        modifier =
                            Modifier
                                .width(300.dp)
                                .fillMaxHeight(),
                    )
                }
            }

            splitter {
                visiblePart {
                    Box(
                        Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(UserColor.getSplitterColor()),
                    )
                }

                handle {
                    Box(
                        Modifier
                            .markAsHandle()
                            .cursorForHorizontalResize()
                            .width(10.dp)
                            .fillMaxHeight()
                            .markAsHandle(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
@Preview
private fun ScreenshotScreen_Preview() {
    ScreenshotScreen(
        screenshot = Screenshot(null),
        splitterState = rememberSplitPaneState(),
        screenshots = emptyList(),
        canCapture = true,
        isCapturing = false,
        selectCommand = ScreenshotCommand.Both,
        commands = emptyList(),
        searchText = "",
        sortType = SortType.SORT_BY_NAME_ASC,
        onOpenDirectory = {},
        onEditScreenshot = {},
        onCopyScreenshot = {},
        onDeleteScreenshot = {},
        onDeleteSpecificScreenshot = {},
        onTakeScreenshot = {},
        onSelectScreenshot = {},
        onNextScreenshot = {},
        onPreviousScreenshot = {},
        onUpdateSearchText = {},
        onSelectCommand = {},
        onUpdateSortType = {},
        onRenameScreenshot = { _, _ -> },
        errorMessage = null,
        renameResetKey = 0,
    )
}
