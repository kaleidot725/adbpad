package jp.kaleidot725.adbpad.ui.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.command.CommandExecutionHistory
import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.model.command.NormalCommandCategory
import jp.kaleidot725.adbpad.domain.model.command.NormalCommandGroup
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.layout.VerticalPaneLayout
import jp.kaleidot725.adbpad.ui.screen.command.component.CommandLayoutToggle
import jp.kaleidot725.adbpad.ui.screen.command.component.CommandList
import jp.kaleidot725.adbpad.ui.screen.command.component.CommandOutput
import jp.kaleidot725.adbpad.ui.screen.command.component.CommandTab
import jp.kaleidot725.adbpad.ui.screen.command.model.CommandLayoutMode
import jp.kaleidot725.adbpad.ui.screen.command.state.CommandAction
import jp.kaleidot725.adbpad.ui.screen.command.state.CommandState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun CommandScreen(
    state: CommandState,
    onAction: (CommandAction) -> Unit,
    splitterState: SplitPaneState,
) {
    CommandScreen(
        commands = state.commands,
        filtered = state.filtered,
        layoutMode = state.layoutMode,
        executionHistory = state.executionHistory,
        onClickFilter = { onAction(CommandAction.ClickCategoryTab(it)) },
        onToggleLayout = { onAction(CommandAction.ToggleLayoutMode) },
        canExecute = state.canExecuteCommand,
        onExecute = { command -> onAction(CommandAction.ExecuteCommand(command)) },
        onToggleFavorite = { command -> onAction(CommandAction.ToggleFavorite(command)) },
        splitterState = splitterState,
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
private fun CommandScreen(
    commands: NormalCommandGroup,
    filtered: NormalCommandCategory,
    layoutMode: CommandLayoutMode,
    executionHistory: CommandExecutionHistory?,
    onClickFilter: (NormalCommandCategory) -> Unit,
    onToggleLayout: () -> Unit,
    canExecute: Boolean,
    onExecute: (NormalCommand) -> Unit,
    onToggleFavorite: (NormalCommand) -> Unit,
    splitterState: SplitPaneState,
) {
    VerticalPaneLayout(
        splitterState = splitterState,
        top = {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .wrapContentHeight()
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 4.dp),
                ) {
                    CommandTab(
                        filtered = filtered,
                        onClick = onClickFilter,
                        modifier = Modifier.weight(1f),
                    )

                    CommandLayoutToggle(
                        layoutMode = layoutMode,
                        onToggle = onToggleLayout,
                    )
                }

                HorizontalDivider(color = UserColor.getSplitterColor())

                CommandList(
                    commands =
                        when (filtered) {
                            NormalCommandCategory.COM -> commands.communication
                            NormalCommandCategory.NAVIGATION -> commands.navigation
                            NormalCommandCategory.THEME -> commands.theme
                            NormalCommandCategory.DISPLAY -> commands.display
                            NormalCommandCategory.DEVICE -> commands.device
                            NormalCommandCategory.FAVORITE -> commands.favorite
                            NormalCommandCategory.ALL -> commands.all
                        },
                    canExecute = canExecute,
                    onExecute = onExecute,
                    onToggleFavorite = onToggleFavorite,
                    layoutMode = layoutMode,
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                )
            }
        },
        bottom = {
            CommandOutput(
                executionHistory = executionHistory,
                modifier = Modifier.fillMaxSize(),
            )
        },
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun CommandScreen_Card_Preview() {
    CommandScreen(
        commands =
            NormalCommandGroup(
                all = listOf(NormalCommand.DarkThemeOn(), NormalCommand.DarkThemeOff(), NormalCommand.WifiOn()),
                communication = listOf(NormalCommand.WifiOn()),
                navigation = emptyList(),
                theme = listOf(NormalCommand.DarkThemeOn(), NormalCommand.DarkThemeOff()),
                display = emptyList(),
                device = emptyList(),
                favorite = emptyList(),
            ),
        filtered = NormalCommandCategory.ALL,
        layoutMode = CommandLayoutMode.CARD,
        executionHistory = null,
        onClickFilter = {},
        onToggleLayout = {},
        canExecute = true,
        onExecute = {},
        onToggleFavorite = {},
        splitterState = rememberSplitPaneState(initialPositionPercentage = 0.7f),
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun CommandScreen_List_Preview() {
    CommandScreen(
        commands =
            NormalCommandGroup(
                all = listOf(NormalCommand.DarkThemeOn(), NormalCommand.DarkThemeOff(), NormalCommand.WifiOn()),
                communication = listOf(NormalCommand.WifiOn()),
                navigation = emptyList(),
                theme = listOf(NormalCommand.DarkThemeOn(), NormalCommand.DarkThemeOff()),
                display = emptyList(),
                device = emptyList(),
                favorite = emptyList(),
            ),
        filtered = NormalCommandCategory.ALL,
        layoutMode = CommandLayoutMode.LIST,
        executionHistory = null,
        onClickFilter = {},
        onToggleLayout = {},
        canExecute = true,
        onExecute = {},
        onToggleFavorite = {},
        splitterState = rememberSplitPaneState(initialPositionPercentage = 0.7f),
    )
}
