package jp.kaleidot725.adbpad.domain.model.command

import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TextCommand(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val text: String,
    val option: Option = Option.SendWithTab,
    val isRunning: Boolean = false,
    val lastModified: Long = System.currentTimeMillis(),
) {
    val requests: List<ShellCommandRequest> get() {
        return buildList {
            val texts = text.split('\n')
            texts.forEach { text ->
                if (text.isEmpty()) {
                    add(ShellCommandRequest(""))
                } else {
                    add(ShellCommandRequest("input text $text"))
                }
            }
        }
    }

    enum class Option {
        SendWithTab,
        SendWithNewLine,
    }

    companion object {
        fun createNew(
            title: String,
            text: String,
        ): TextCommand = TextCommand(title = title, text = text, isRunning = false)
    }
}
