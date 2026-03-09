package jp.kaleidot725.adbpad.ui.screen.screenshot

import jp.kaleidot725.adbpad.core.utils.ClipBoardUtils
import jp.kaleidot725.adbpad.domain.model.command.ScreenshotCommand
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.os.OSContext
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.repository.ScreenshotCommandRepository
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.screenshot.GetScreenshotCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.screenshot.RenameScreenshotUseCase
import jp.kaleidot725.adbpad.domain.usecase.screenshot.TakeScreenshotUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.screenshot.state.ScreenshotAction
import jp.kaleidot725.adbpad.ui.screen.screenshot.state.ScreenshotSideEffect
import jp.kaleidot725.adbpad.ui.screen.screenshot.state.ScreenshotState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.File

class ScreenshotStateHolder(
    private val takeScreenshotUseCase: TakeScreenshotUseCase,
    private val getScreenshotCommandUseCase: GetScreenshotCommandUseCase,
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val screenshotCommandRepository: ScreenshotCommandRepository,
    private val renameScreenshotUseCase: RenameScreenshotUseCase,
) : PulseStore<ScreenshotState, ScreenshotAction, ScreenshotSideEffect, AppBroadCast>(initialUiState = ScreenshotState()) {
    override fun onSetup() {
        coroutineScope.launch {
            val commands = getScreenshotCommandUseCase()
            update {
                copy(
                    selectedCommand = commands.first(),
                    commands = commands,
                )
            }

            initPreviews()
        }

        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collectLatest {
                update { copy(selectedDevice = it) }
            }
        }
    }

    override fun onAction(uiAction: ScreenshotAction) {
        coroutineScope.launch {
            when (uiAction) {
                is ScreenshotAction.TakeScreenshot -> takeScreenShot(uiAction.command)
                ScreenshotAction.OpenDirectory -> openDirectory()
                ScreenshotAction.CopyScreenshotToClipboard -> copyScreenShotToClipboard()
                ScreenshotAction.DeleteScreenshotToClipboard -> deleteScreenShotToClipboard()
                ScreenshotAction.EditScreenshot -> editScreenshot()
                is ScreenshotAction.RenameScreenshot -> renameScreenshot(uiAction.name, uiAction.isRealtime)
                is ScreenshotAction.DeleteScreenshot -> deleteSpecificScreenshot(uiAction.screenshot)
                is ScreenshotAction.SelectScreenshot -> selectScreenshot(uiAction.screenshot)
                ScreenshotAction.NextScreenshot -> nextScreenshot()
                ScreenshotAction.PreviousScreenshot -> previousScreenshot()
                is ScreenshotAction.UpdateSearchText -> updateSearchText(uiAction.text)
                is ScreenshotAction.SelectScreenshotCommand -> selectScreenshotCommand(uiAction.command)
                is ScreenshotAction.UpdateSortType -> updateSortType(uiAction.sortType)
                ScreenshotAction.DismissError -> update { copy(errorMessage = null) }
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {
                coroutineScope.launch {
                    val commands = getScreenshotCommandUseCase()
                    update {
                        copy(
                            selectedCommand = commands.first(),
                            commands = commands,
                        )
                    }

                    initPreviews()
                }
            }
        }
    }

    private suspend fun renameScreenshot(
        name: String,
        isRealtime: Boolean,
    ) {
        val currentScreenshot = currentState.preview
        if (currentScreenshot.file?.nameWithoutExtension == name) {
            update { copy(errorMessage = null) }
            return
        }

        val isLegal = name.isNotEmpty() && name.none { ILLEGAL_FILENAME_CHARS.contains(it) }
        if (!isLegal) {
            update {
                copy(
                    errorMessage = Language.invalidCharactersMessage,
                    renameResetKey = if (!isRealtime) renameResetKey + 1 else renameResetKey,
                )
            }
            return
        }

        val isDuplicate = currentState.previews.any { it.file?.nameWithoutExtension == name }
        if (isDuplicate) {
            update {
                copy(
                    errorMessage = Language.fileNameDuplicateMessage,
                    renameResetKey = if (!isRealtime) renameResetKey + 1 else renameResetKey,
                )
            }
            return
        }

        update { copy(errorMessage = null) }

        val isSuccess = renameScreenshotUseCase(currentScreenshot, name)
        if (!isSuccess) {
            update {
                copy(
                    renameResetKey = if (!isRealtime) renameResetKey + 1 else renameResetKey,
                )
            }
            return
        }

        val screenshots =
            screenshotCommandRepository.getScreenshots(
                currentState.searchText,
                currentState.sortType,
            )

        val newScreenshot =
            screenshots.firstOrNull {
                it.file?.nameWithoutExtension == name
            } ?: screenshots.firstOrNull() ?: Screenshot(null)

        update {
            copy(
                previews = screenshots,
                preview = newScreenshot,
            )
        }
    }

    private suspend fun updateSearchText(searchText: String) {
        val screenshots = screenshotCommandRepository.getScreenshots(searchText, currentState.sortType)
        update {
            copy(
                searchText = searchText,
                previews = screenshots.filter { it.file?.name?.startsWith(searchText) ?: false },
            )
        }
    }

    private suspend fun updateSortType(sortType: SortType) {
        val screenshots = screenshotCommandRepository.getScreenshots(currentState.searchText, sortType)
        update {
            copy(
                searchText = searchText,
                sortType = sortType,
                previews = screenshots.filter { it.file?.name?.startsWith(searchText) ?: false },
            )
        }
    }

    private suspend fun takeScreenShot(command: ScreenshotCommand) {
        val selectedDevice = state.value.selectedDevice ?: return
        takeScreenshotUseCase(
            device = selectedDevice,
            command = command,
            onStart = {
                val commands = getScreenshotCommandUseCase()
                update {
                    copy(
                        commands = commands,
                        preview = Screenshot.EMPTY,
                        isCapturing = true,
                    )
                }
            },
            onFailed = {
                val commands = getScreenshotCommandUseCase()
                update {
                    copy(
                        commands = commands,
                        preview = Screenshot.EMPTY,
                        isCapturing = false,
                    )
                }
            },
            onComplete = {
                val commands = getScreenshotCommandUseCase()
                val screenshots =
                    screenshotCommandRepository.getScreenshots(
                        currentState.searchText,
                        currentState.sortType,
                    )
                update {
                    copy(
                        commands = commands,
                        preview = it,
                        previews = screenshots,
                        isCapturing = false,
                    )
                }
            },
        )
    }

    private suspend fun openDirectory() {
        val file = File(OSContext.resolveOSContext().screenshotDirectory)
        withContext(Dispatchers.IO) { Desktop.getDesktop().open(file) }
    }

    private fun copyScreenShotToClipboard() {
        val file = currentState.preview.file ?: return
        ClipBoardUtils.copyFile(file)
    }

    private suspend fun deleteScreenShotToClipboard() {
        screenshotCommandRepository.delete(currentState.preview)
        initPreviews()
    }

    private suspend fun editScreenshot() {
        val file = currentState.preview.file ?: return
        val desktop = Desktop.getDesktop()
        when {
            desktop.isSupported(Desktop.Action.EDIT) -> desktop.edit(file)
            desktop.isSupported(Desktop.Action.OPEN) -> desktop.open(file)
        }
    }

    private suspend fun deleteSpecificScreenshot(screenshot: Screenshot) {
        screenshotCommandRepository.delete(screenshot)
        val screenshots =
            screenshotCommandRepository.getScreenshots(
                currentState.searchText,
                currentState.sortType,
            )

        if (screenshots.isEmpty()) {
            update {
                copy(
                    previews = screenshots,
                    preview = Screenshot(null),
                )
            }
        } else {
            val wasSelectedScreenshotDeleted = currentState.preview == screenshot
            if (wasSelectedScreenshotDeleted) {
                val newSelectedScreenshot = screenshots.firstOrNull() ?: Screenshot(null)
                update {
                    copy(
                        previews = screenshots,
                        preview = newSelectedScreenshot,
                    )
                }
            } else {
                update {
                    copy(
                        previews = screenshots,
                    )
                }
            }
        }
    }

    private fun selectScreenshot(screenshot: Screenshot) {
        update {
            this.copy(preview = screenshot)
        }
    }

    private fun nextScreenshot() {
        val nextIndex = currentState.previews.indexOf(currentState.preview) + 1
        val nextPreview = currentState.previews.getOrNull(nextIndex) ?: return
        update {
            this.copy(preview = nextPreview)
        }
    }

    private fun previousScreenshot() {
        val previousIndex = currentState.previews.indexOf(currentState.preview) - 1
        val previousPreview = currentState.previews.getOrNull(previousIndex) ?: return
        update {
            this.copy(preview = previousPreview)
        }
    }

    private suspend fun initPreviews() {
        val screenshots =
            screenshotCommandRepository.getScreenshots(
                currentState.searchText,
                currentState.sortType,
            )
        val screenshot = screenshots.firstOrNull() ?: Screenshot(null)
        update {
            this.copy(
                previews = screenshots,
                preview = screenshot,
            )
        }
    }

    private fun selectScreenshotCommand(command: ScreenshotCommand) {
        update {
            this.copy(
                selectedCommand = command,
            )
        }
    }

    companion object {
        private val ILLEGAL_FILENAME_CHARS = setOf('\\', '/', ':', '*', '?', '"', '<', '>', '|')
    }
}
