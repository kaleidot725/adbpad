package jp.kaleidot725.adbpad.ui.screen.text

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.screen.screenshot.cursorForHorizontalResize
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandDetailMenu
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandEditor
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandHeader
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandList
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandAction
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun TextCommandScreen(
    state: TextCommandState,
    onAction: (TextCommandAction) -> Unit,
    splitterState: SplitPaneState,
) {
    HorizontalSplitPane(
        splitPaneState = splitterState,
        modifier = Modifier.fillMaxSize(),
    ) {
        first(minSize = 200.dp) {
            Column {
                TextCommandHeader(
                    searchText = state.searchText,
                    sortType = state.sortType,
                    onUpdateSortType = { onAction(TextCommandAction.UpdateSortType(it)) },
                    onUpdateSearchText = { onAction(TextCommandAction.UpdateSearchText(it)) },
                    onAddCommand = { onAction(TextCommandAction.AddNewText) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                )

                HorizontalDivider(color = UserColor.getSplitterColor())

                TextCommandList(
                    selectedCommand = state.selectedCommand,
                    commands = state.commands,
                    onSelectCommand = { onAction(TextCommandAction.SelectCommand(it)) },
                    onDeleteCommand = { onAction(TextCommandAction.DeleteCommandText(it)) },
                    onAddNewTextCommand = { onAction(TextCommandAction.AddNewText) },
                    onNextCommand = { onAction(TextCommandAction.NextCommand) },
                    onPreviousCommand = { onAction(TextCommandAction.PreviousCommand) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        second {
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                if (state.selectedCommand != null) {
                    TextCommandEditor(
                        command = state.selectedCommand,
                        option = state.selectedTextCommandOption,
                        onUpdateTitle = { id, title -> onAction(TextCommandAction.UpdateCommandTitle(id, title)) },
                        onUpdateText = { id, text -> onAction(TextCommandAction.UpdateCommandText(id, text)) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )

                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        color = UserColor.getSplitterColor(),
                    )

                    TextCommandDetailMenu(
                        command = state.selectedCommand,
                        canSend = state.canSend,
                        onSendText = { onAction(TextCommandAction.SendTextCommand) },
                        selectedOption = state.selectedTextCommandOption,
                        onUpdateTextCommandOption = { onAction(TextCommandAction.UpdateTextCommandOption(it)) },
                        modifier = Modifier.width(300.dp).fillMaxHeight(),
                    )
                }
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

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun InputTextScreen_Preview() {
    TextCommandScreen(
        state = TextCommandState(),
        onAction = {},
        splitterState = rememberSplitPaneState(),
    )
}
