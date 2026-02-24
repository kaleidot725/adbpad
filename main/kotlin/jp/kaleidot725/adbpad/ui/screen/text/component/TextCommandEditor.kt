package jp.kaleidot725.adbpad.ui.screen.text.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.text.DefaultTextField

@Composable
fun TextCommandEditor(
    command: TextCommand,
    option: TextCommand.Option,
    onUpdateTitle: (id: String, value: String) -> Unit,
    onUpdateText: (id: String, value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            DefaultTextField(
                id = command.id,
                initialText = command.title,
                placeHolder = Language.textCommandUnTitle,
                onUpdateText = { onUpdateTitle(command.id, it) },
                modifier = Modifier.weight(1.0f).height(48.dp).padding(horizontal = 12.dp),
            )
        }

        HorizontalDivider(color = UserColor.getSplitterColor())

        val primaryColor = MaterialTheme.colorScheme.primary
        val transformation by remember(primaryColor, option) {
            mutableStateOf(LineBreakTransformation(option, primaryColor))
        }
        var commandText by remember(command.id) { mutableStateOf(command.text) }
        BasicTextField(
            value = commandText,
            onValueChange = { textValue ->
                commandText = textValue
                onUpdateText(command.id, commandText)
            },
            visualTransformation = transformation,
            maxLines = Int.MAX_VALUE,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 12.dp),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TextCommandEditor(
        command = TextCommand(title = "Title", text = "Text"),
        option = TextCommand.Option.SendWithTab,
        onUpdateTitle = { _, _ -> },
        onUpdateText = { _, _ -> },
    )
}

private class LineBreakTransformation(
    private val option: TextCommand.Option,
    private val optionTextColor: Color = Color.Gray,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val optionCharacter =
            when (option) {
                TextCommand.Option.SendWithTab -> '⇥'
                TextCommand.Option.SendWithNewLine -> '↵'
            }

        val builder = AnnotatedString.Builder()
        val originalText = text.toString()
        var lastIndex = 0

        val newlineIndices = originalText.indices.filter { originalText[it] == '\n' }
        for (newlineIndex in newlineIndices) {
            builder.append(originalText.substring(lastIndex, newlineIndex))

            builder.withStyle(SpanStyle(color = optionTextColor)) {
                append(optionCharacter)
            }

            builder.append("\n")

            lastIndex = newlineIndex + 1
        }
        if (lastIndex < originalText.length) builder.append(originalText.substring(lastIndex))

        val annotatedString = builder.toAnnotatedString()
        val offsetMapping =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    val originalSubstring = originalText.substring(0, offset)
                    val newlineCount = originalSubstring.count { it == '\n' }
                    return offset + newlineCount
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val transformedText = annotatedString.text.substring(0, offset)
                    val specialCharCount = transformedText.count { it == optionCharacter }
                    return offset - specialCharCount
                }
            }
        return TransformedText(annotatedString, offsetMapping)
    }
}
