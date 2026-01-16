package jp.kaleidot725.adbpad.ui.screen.command.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.screen.command.model.CommandLayoutMode

@Composable
fun CommandList(
    commands: List<NormalCommand>,
    canExecute: Boolean,
    onExecute: (NormalCommand) -> Unit,
    onToggleFavorite: (NormalCommand) -> Unit,
    layoutMode: CommandLayoutMode,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        if (commands.isNotEmpty()) {
            when (layoutMode) {
                CommandLayoutMode.CARD -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(250.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(commands) { command ->
                            CommandItemCard(
                                title = command.title,
                                detail = command.details,
                                isRunning = command.isRunning,
                                isFavorite = command.isFavorite,
                                onToggleFavorite = { onToggleFavorite(command) },
                                canExecute = canExecute,
                                onExecute = { onExecute(command) },
                                modifier = Modifier.height(175.dp).fillMaxWidth().padding(2.dp),
                            )
                        }
                    }
                }
                CommandLayoutMode.LIST -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(commands) { command ->
                            CommandItemList(
                                title = command.title,
                                detail = command.details,
                                isRunning = command.isRunning,
                                isFavorite = command.isFavorite,
                                onToggleFavorite = { onToggleFavorite(command) },
                                canExecute = canExecute,
                                onExecute = { onExecute(command) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = Language.notFoundCommand,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Preview
@Composable
private fun CommandList_Card_Preview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CommandList(
            commands = listOf(NormalCommand.DarkThemeOn(), NormalCommand.DarkThemeOff(), NormalCommand.WifiOn()),
            canExecute = true,
            onExecute = {},
            onToggleFavorite = {},
            layoutMode = CommandLayoutMode.CARD,
            modifier = Modifier.fillMaxWidth().weight(0.5f),
        )

        CommandList(
            commands = emptyList(),
            canExecute = true,
            onExecute = {},
            onToggleFavorite = {},
            layoutMode = CommandLayoutMode.CARD,
            modifier = Modifier.fillMaxWidth().weight(0.5f).background(Color.LightGray),
        )
    }
}

@Preview
@Composable
private fun CommandList_List_Preview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CommandList(
            commands = listOf(NormalCommand.DarkThemeOn(), NormalCommand.DarkThemeOff(), NormalCommand.WifiOn()),
            canExecute = true,
            onExecute = {},
            onToggleFavorite = {},
            layoutMode = CommandLayoutMode.LIST,
            modifier = Modifier.fillMaxWidth().weight(0.5f),
        )

        CommandList(
            commands = emptyList(),
            canExecute = true,
            onExecute = {},
            onToggleFavorite = {},
            layoutMode = CommandLayoutMode.LIST,
            modifier = Modifier.fillMaxWidth().weight(0.5f).background(Color.LightGray),
        )
    }
}
