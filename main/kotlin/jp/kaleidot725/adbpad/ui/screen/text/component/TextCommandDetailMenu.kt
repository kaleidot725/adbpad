package jp.kaleidot725.adbpad.ui.screen.text.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.component.dropbox.EnumDropDown
import jp.kaleidot725.adbpad.ui.component.indicator.RunningIndicator
import jp.kaleidot725.adbpad.ui.component.layout.ExpandableSection

@Composable
fun TextCommandDetailMenu(
    command: TextCommand,
    canSend: Boolean,
    onSendText: () -> Unit,
    selectedOption: TextCommand.Option,
    onUpdateTextCommandOption: (TextCommand.Option) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ExpandableSection(title = Language.textCommandLineBreakSection) {
                EnumDropDown(
                    selectedValue = selectedOption,
                    values = TextCommand.Option.entries.toList(),
                    onValueSelected = { if (it != null) onUpdateTextCommandOption(it) },
                    displayName = {
                        when (it) {
                            TextCommand.Option.SendWithTab -> Language.textCommandLineBreakOptionTab
                            TextCommand.Option.SendWithNewLine -> Language.textCommandLineBreakOptionEnter
                            null -> ""
                        }
                    },
                    label = Language.textCommandLineBreakOptionLabel,
                    showNullOption = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        TextCommandActionButton(
            text = Language.execute,
            icon = Lucide.Play,
            onClick = onSendText,
            enabled = canSend,
            isSending = command.isRunning,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}

@Composable
private fun TextCommandActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    isSending: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isSending,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier,
    ) {
        if (isSending) {
            RunningIndicator(modifier = Modifier.size(16.dp))
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Text(text = text)
            }
        }
    }
}
