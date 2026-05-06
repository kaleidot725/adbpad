package jp.kaleidot725.adbpad.ui.screen.text

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.layout.ThreePaneLayout
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandDetailMenu
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandEditor
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandHeader
import jp.kaleidot725.adbpad.ui.screen.text.component.TextCommandList
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandAction
import jp.kaleidot725.adbpad.ui.screen.text.state.TextCommandState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun TextCommandScreen(
    state: TextCommandState,
    onAction: (TextCommandAction) -> Unit,
    splitterState: SplitPaneState,
    rightSplitterState: SplitPaneState,
) {
    val selectedCommand = state.selectedCommand
    ThreePaneLayout(
        splitterState = splitterState,
        rightSplitterState = rightSplitterState,
        left = {
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
        },
        center = {
            if (selectedCommand != null) {
                TextCommandEditor(
                    command = selectedCommand,
                    option = state.selectedTextCommandOption,
                    onUpdateTitle = { id, title -> onAction(TextCommandAction.UpdateCommandTitle(id, title)) },
                    onUpdateText = { id, text -> onAction(TextCommandAction.UpdateCommandText(id, text)) },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = Language.notFoundInputText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        },
        right =
            selectedCommand?.let { command ->
                {
                    TextCommandDetailMenu(
                        command = command,
                        canSend = state.canSend,
                        onSendText = { onAction(TextCommandAction.SendTextCommand) },
                        selectedOption = state.selectedTextCommandOption,
                        onUpdateTextCommandOption = { onAction(TextCommandAction.UpdateTextCommandOption(it)) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            },
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun InputTextScreen_Preview() {
    TextCommandScreen(
        state = TextCommandState(),
        onAction = {},
        splitterState = rememberSplitPaneState(),
        rightSplitterState = rememberSplitPaneState(initialPositionPercentage = 0.7f),
    )
}
