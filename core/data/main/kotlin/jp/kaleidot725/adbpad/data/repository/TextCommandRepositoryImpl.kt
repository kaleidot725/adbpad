package jp.kaleidot725.adbpad.data.repository

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import jp.kaleidot725.adbpad.data.local.TextCommandFileCreator
import jp.kaleidot725.adbpad.domain.model.command.KeyCommand
import jp.kaleidot725.adbpad.domain.model.command.TextCommand
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.TextCommandRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TextCommandRepositoryImpl : TextCommandRepository {
    private val runningCommands: MutableSet<TextCommand> = mutableSetOf()
    private val adbClient = AndroidDebugBridgeClientFactory().build()
    private val lock: Any = Any()

    override suspend fun getAllTextCommand(): List<TextCommand> {
        return withContext(Dispatchers.IO) {
            synchronized(lock) {
                val setting = TextCommandFileCreator.load()
                return@withContext setting.values.map { text ->
                    text.copy(isRunning = runningCommands.any { it.id == text.id })
                }
            }
        }
    }

    override suspend fun addTextCommand(command: TextCommand): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TextCommandFileCreator.load()
                val newInputTexts = oldSetting.values.toMutableList().apply { add(command) }
                val newSetting = oldSetting.copy(values = newInputTexts)
                return@withContext TextCommandFileCreator.save(newSetting)
            }
        }
    }

    override suspend fun removeTextCommand(command: TextCommand): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TextCommandFileCreator.load()
                val newInputTexts = oldSetting.values.toMutableList().apply { remove(command) }
                val newSetting = oldSetting.copy(values = newInputTexts)
                return@withContext TextCommandFileCreator.save(newSetting)
            }
        }
    }

    override suspend fun updateTextCommandTitle(
        id: String,
        title: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TextCommandFileCreator.load()
                val targetIndex = oldSetting.values.indexOfFirst { it.id == id }
                val target = oldSetting.values.getOrNull(targetIndex) ?: return@withContext false
                val newTarget = target.copy(title = title, lastModified = System.currentTimeMillis())
                val newCommands = oldSetting.values.toMutableList()
                newCommands.remove(target)
                newCommands.add(targetIndex, newTarget)

                val newSetting = oldSetting.copy(values = newCommands)
                return@withContext TextCommandFileCreator.save(newSetting)
            }
        }
    }

    override suspend fun updateTextCommandValue(
        id: String,
        value: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TextCommandFileCreator.load()
                val targetIndex = oldSetting.values.indexOfFirst { it.id == id }
                val target = oldSetting.values.getOrNull(targetIndex) ?: return@withContext false
                val newTarget = target.copy(text = value, lastModified = System.currentTimeMillis())
                val newCommands = oldSetting.values.toMutableList()
                newCommands.remove(target)
                newCommands.add(targetIndex, newTarget)

                val newSetting = oldSetting.copy(values = newCommands)
                return@withContext TextCommandFileCreator.save(newSetting)
            }
        }
    }

    override suspend fun updateTextCommandOption(
        id: String,
        option: TextCommand.Option,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TextCommandFileCreator.load()
                val targetIndex = oldSetting.values.indexOfFirst { it.id == id }
                val target = oldSetting.values.getOrNull(targetIndex) ?: return@withContext false
                val newTarget = target.copy(option = option, lastModified = System.currentTimeMillis())
                val newCommands = oldSetting.values.toMutableList()
                newCommands.remove(target)
                newCommands.add(targetIndex, newTarget)

                val newSetting = oldSetting.copy(values = newCommands)
                return@withContext TextCommandFileCreator.save(newSetting)
            }
        }
    }

    override suspend fun sendCommand(
        device: Device,
        command: TextCommand,
        onStart: suspend () -> Unit,
        onComplete: suspend () -> Unit,
        onFailed: suspend () -> Unit,
    ) {
        withContext(Dispatchers.IO) {
            runningCommands.add(command)
            onStart()

            delay(300)

            command.requests.forEachIndexed { index, request ->
                if (request.cmd.isNotEmpty()) {
                    val result = adbClient.execute(request, device.serial)
                    if (result.exitCode != 0) {
                        runningCommands.remove(command)
                        onFailed()
                        return@withContext
                    }
                }

                if (command.requests.lastIndex != index) {
                    val keyCode =
                        when (command.option) {
                            TextCommand.Option.SendWithTab -> 61
                            TextCommand.Option.SendWithNewLine -> 66
                        }

                    val keyCommand = KeyCommand(keyCode)
                    keyCommand.requests.forEach { keyRequest ->
                        val keyResult = adbClient.execute(keyRequest, device.serial)
                        if (keyResult.exitCode != 0) {
                            onFailed()
                            return@withContext
                        }
                    }
                }
            }

            runningCommands.remove(command)
            onComplete()
        }
    }

    override fun clear() {
        runningCommands.clear()
    }
}
