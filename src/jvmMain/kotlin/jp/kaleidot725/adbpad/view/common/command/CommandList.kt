package jp.kaleidot725.adbpad.view.common.command

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.model.data.Command
import jp.kaleidot725.adbpad.view.resource.StringRes

@Composable
fun CommandList(
    commands: List<Command>,
    onExecute: (Command) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            commands.forEach { command ->
                Card(modifier, elevation = 1.dp) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(0.9f, true)) {
                            Text(text = command.title)
                            Text(text = command.details)
                        }
                        Button(onClick = { onExecute(command) }) {
                            Text(text = StringRes.EXECUTE)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CommandList_Preview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CommandList(
            commands = listOf(Command.DarkThemeOn, Command.DarkThemeOff),
            onExecute = {},
            modifier = Modifier.fillMaxWidth().weight(0.5f)
        )

        CommandList(
            commands = emptyList(),
            onExecute = {},
            modifier = Modifier.fillMaxWidth().weight(0.5f).background(Color.LightGray)
        )
    }
}