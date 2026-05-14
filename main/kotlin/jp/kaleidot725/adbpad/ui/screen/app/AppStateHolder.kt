package jp.kaleidot725.adbpad.ui.screen.app

import jp.kaleidot725.adbpad.domain.model.app.AppDataDirectory
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.app.state.AppAction
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFilePreviewState
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState
import jp.kaleidot725.adbpad.ui.screen.app.state.AppProcessState
import jp.kaleidot725.adbpad.ui.screen.app.state.AppSideEffect
import jp.kaleidot725.adbpad.ui.screen.app.state.AppState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.KeyboardFocusManager
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

class AppStateHolder(
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val installedAppRepository: InstalledAppRepository,
) : PulseStore<AppState, AppAction, AppSideEffect, AppBroadCast>(
        initialUiState = AppState(),
    ) {
    override fun onSetup() {
        collectSelectedDevice()
        collectAppFileTreeRoots()
    }

    override fun onAction(uiAction: AppAction) {
        coroutineScope.launch {
            when (uiAction) {
                AppAction.RefreshApps -> reduceRefreshApps()
                is AppAction.UpdateSearchText -> reduceUpdateSearchText(uiAction.text)
                is AppAction.UpdateSortType -> reduceUpdateSortType(uiAction.sortType)
                is AppAction.SelectApp -> reduceSelectApp(uiAction.app)
                AppAction.InstallPackage -> reduceInstallPackage()
                is AppAction.UninstallApp -> reduceUninstallApp(uiAction.app)
                AppAction.SelectNextApp -> reduceSelectNextApp()
                AppAction.SelectPreviousApp -> reduceSelectPreviousApp()
                is AppAction.SelectDataFileNode -> reduceSelectDataFileNode(uiAction.entry)
                is AppAction.SelectSdCardDataFileNode -> reduceSelectSdCardDataFileNode(uiAction.entry)
                is AppAction.PreviewDataFileNode -> reducePreviewDataFileNode(uiAction.entry)
                is AppAction.PreviewSdCardDataFileNode -> reducePreviewSdCardDataFileNode(uiAction.entry)
                AppAction.SavePreviewFile -> reduceSavePreviewFile()
                AppAction.OverwritePreviewFile -> reduceOverwritePreviewFile()
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        coroutineScope.launch {
            when (broadcast) {
                AppBroadCast.Refresh -> reduceRefreshApps()
            }
        }
    }

    private suspend fun reduceOverwritePreviewFile() {
        val device = currentState.selectedDevice ?: return
        val entry = currentState.filePreview.entry as? AppFileEntry.File ?: return
        if (currentState.filePreview.isOverwriting) return

        val source = selectConfirmedOverwriteAppFile(entry) ?: return

        update {
            copy(
                filePreview =
                    filePreview.copy(
                        isOverwriting = true,
                        errorMessage = null,
                    ),
            )
        }

        val result = installedAppRepository.overwriteAppFile(device, source, entry)
        if (result.isOk) {
            val updatedEntry = entry.copy(size = source.length())
            reloadCurrentAppFileTrees()
            update {
                copy(
                    selectedDataFile = selectedDataFile.replaceIfSamePath(updatedEntry),
                    selectedSdCardDataFile = selectedSdCardDataFile.replaceIfSamePath(updatedEntry),
                )
            }
            previewAppFile(updatedEntry)
        } else {
            update {
                copy(
                    filePreview =
                        filePreview.copy(
                            isOverwriting = false,
                            errorMessage = result.error.message ?: "Failed to overwrite file",
                        ),
                )
            }
        }
    }

    private fun AppFileEntry?.replaceIfSamePath(entry: AppFileEntry): AppFileEntry? =
        if (this?.path == entry.path) entry else this

    private suspend fun reduceRefreshApps() {
        if (currentState.processState != AppProcessState.Idle) return

        update { copy(processState = AppProcessState.Loading) }
        refreshApps(device = currentState.selectedDevice)
        update { copy(processState = AppProcessState.Idle) }
    }

    private suspend fun reduceUpdateSearchText(text: String) {
        update { copy(searchText = text) }
    }

    private suspend fun reduceUpdateSortType(sortType: SortType) {
        update { copy(sortType = sortType) }
    }

    private suspend fun reduceSelectApp(app: InstalledApp) {
        update {
            copy(
                selectedAppIndex = apps.indexOf(app).takeIf { it >= 0 },
            )
        }
    }

    private suspend fun reduceInstallPackage() {
        if (currentState.processState != AppProcessState.Idle) return

        val device = currentState.selectedDevice ?: return
        val packageFile = selectInstallApplication() ?: return

        update { copy(processState = AppProcessState.Installing) }
        val isInstalled = installedAppRepository.installPackage(device, packageFile)
        if (isInstalled) refreshApps(device)
        update { copy(processState = AppProcessState.Idle) }
    }

    private suspend fun reduceUninstallApp(app: InstalledApp) {
        if (currentState.processState != AppProcessState.Idle) return
        val device = currentState.selectedDevice ?: return

        update { copy(processState = AppProcessState.Uninstalling) }
        val isUninstalled = installedAppRepository.uninstallInstalledApp(device, app)
        if (isUninstalled) refreshApps(device)
        update { copy(processState = AppProcessState.Idle) }
    }

    private suspend fun reduceSelectNextApp() {
        val targetIndex = selectNextOrPreviousApp(offset = 1)
        update {
            copy(selectedAppIndex = targetIndex)
        }
    }

    private suspend fun reduceSelectPreviousApp() {
        val targetIndex = selectNextOrPreviousApp(offset = -1)
        update {
            copy(selectedAppIndex = targetIndex)
        }
    }

    private suspend fun reduceSelectDataFileNode(entry: AppFileEntry) {
        update { copy(selectedDataFile = entry) }
        if (entry is AppFileEntry.Directory) {
            toggleDataAppDirectory(entry)
        }
    }

    private suspend fun reduceSelectSdCardDataFileNode(entry: AppFileEntry) {
        update { copy(selectedSdCardDataFile = entry) }
        if (entry is AppFileEntry.Directory) {
            toggleSdCardDataAppDirectory(entry)
        }
    }

    private suspend fun reducePreviewDataFileNode(entry: AppFileEntry) {
        update { copy(selectedDataFile = entry) }
        previewAppFile(entry)
    }

    private suspend fun reducePreviewSdCardDataFileNode(entry: AppFileEntry) {
        update { copy(selectedSdCardDataFile = entry) }
        previewAppFile(entry)
    }

    private suspend fun reduceSavePreviewFile() {
        val device = currentState.selectedDevice ?: return
        val entry = currentState.filePreview.entry as? AppFileEntry.File ?: return
        if (currentState.filePreview.isSaving) return

        val destination = selectSaveAppFile(entry) ?: return

        update {
            copy(
                filePreview =
                    filePreview.copy(
                        isSaving = true,
                        errorMessage = null,
                    ),
            )
        }

        val result = installedAppRepository.saveAppFile(device, entry, destination)
        update {
            copy(
                filePreview =
                    filePreview.copy(
                        isSaving = false,
                        errorMessage =
                            if (result.isErr) {
                                result.error.message ?: "Failed to save file"
                            } else {
                                null
                            },
                    ),
            )
        }
    }

    private fun collectSelectedDevice() {
        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collectLatest { device ->
                if (device == null) {
                    update { AppState() }
                    return@collectLatest
                }

                update { copy(processState = AppProcessState.Loading) }
                update { copy(selectedDevice = device) }
                refreshApps(device)
                update { copy(processState = AppProcessState.Idle) }
            }
        }
    }

    private fun collectAppFileTreeRoots() {
        coroutineScope.launch {
            state
                .map { it.selectedDevice to it.selectedApp }
                .distinctUntilChanged()
                .collectLatest { (device, app) -> refreshAppFileTreeRoots(device, app) }
        }
    }

    private suspend fun selectInstallApplication(): File? =
        withContext(Dispatchers.Swing) {
            val chooser =
                JFileChooser().apply {
                    dialogTitle = Language.selectInstallPackage
                    fileSelectionMode = JFileChooser.FILES_ONLY
                    isAcceptAllFileFilterUsed = false
                    fileFilter = FileNameExtensionFilter("APK", "apk")
                }
            val parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().activeWindow
            val result = chooser.showOpenDialog(parent)
            if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
        }

    private suspend fun selectSaveAppFile(entry: AppFileEntry.File): File? =
        withContext(Dispatchers.Swing) {
            val chooser =
                JFileChooser().apply {
                    dialogTitle = Language.download
                    fileSelectionMode = JFileChooser.FILES_ONLY
                    selectedFile = File(entry.name)
                }
            val parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().activeWindow
            val result = chooser.showSaveDialog(parent)
            if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
        }

    private suspend fun selectOverwriteAppFile(entry: AppFileEntry.File): File? =
        withContext(Dispatchers.Swing) {
            val chooser =
                JFileChooser().apply {
                    dialogTitle = "Overwrite ${entry.name}"
                    fileSelectionMode = JFileChooser.FILES_ONLY
                }
            val parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().activeWindow
            val result = chooser.showOpenDialog(parent)
            if (result == JFileChooser.APPROVE_OPTION) chooser.selectedFile else null
        }

    private suspend fun selectConfirmedOverwriteAppFile(entry: AppFileEntry.File): File? {
        val source = selectOverwriteAppFile(entry) ?: return null
        return if (confirmOverwriteAppFile(entry, source)) source else null
    }

    private suspend fun confirmOverwriteAppFile(
        entry: AppFileEntry.File,
        source: File,
    ): Boolean =
        withContext(Dispatchers.Swing) {
            val parent = KeyboardFocusManager.getCurrentKeyboardFocusManager().activeWindow
            val result =
                JOptionPane.showConfirmDialog(
                    parent,
                    "Overwrite ${entry.name} with ${source.name}?",
                    "Overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                )
            result == JOptionPane.YES_OPTION
        }

    private fun selectNextOrPreviousApp(offset: Int): Int {
        val filteredApps = currentState.filteredApps
        if (filteredApps.isEmpty()) return 0

        val currentIndex = filteredApps.indexOf(currentState.selectedApp)
        val targetIndex = (currentIndex + offset).coerceIn(0, filteredApps.lastIndex)
        return currentState.apps.indexOf(filteredApps[targetIndex]).coerceAtLeast(0)
    }

    private suspend fun refreshApps(device: Device? = null) {
        val device = device ?: return
        val selectedApp = currentState.selectedApp
        val apps = installedAppRepository.getInstalledApps(device)
        val selectedAppIndex =
            selectedApp
                ?.let { apps.indexOf(it) }
                ?.takeIf { it >= 0 }
                ?: apps.indices.firstOrNull()
        update {
            copy(
                apps = apps,
                selectedAppIndex = selectedAppIndex,
            )
        }
    }

    private suspend fun refreshAppFileTreeRoots(
        device: Device?,
        app: InstalledApp?,
    ) {
        if (device == null || app == null) {
            update {
                copy(
                    dataFileTree = AppFileTreeState(),
                    sdCardDataFileTree = AppFileTreeState(),
                    selectedDataFile = null,
                    selectedSdCardDataFile = null,
                    filePreview = AppFilePreviewState(),
                )
            }
        } else {
            refreshDataAppFileTree(device, app)
            refreshSdCardDataAppFileTree(device, app)
        }
    }

    private suspend fun reloadCurrentAppFileTrees() {
        val device = currentState.selectedDevice ?: return
        val app = currentState.selectedApp ?: return

        update {
            copy(
                dataFileTree = AppFileTreeState(),
                sdCardDataFileTree = AppFileTreeState(),
            )
        }

        refreshDataAppFileTree(device, app)
        refreshSdCardDataAppFileTree(device, app)
    }

    private suspend fun refreshDataAppFileTree(
        device: Device,
        app: InstalledApp,
    ) = refreshAppFileTree(
        device = device,
        app = app,
        directory = AppDataDirectory.Data,
        selectTree = { dataFileTree },
        updateTree = { copy(dataFileTree = it) },
    )

    private suspend fun refreshSdCardDataAppFileTree(
        device: Device,
        app: InstalledApp,
    ) = refreshAppFileTree(
        device = device,
        app = app,
        directory = AppDataDirectory.SdCardData,
        selectTree = { sdCardDataFileTree },
        updateTree = { copy(sdCardDataFileTree = it) },
    )

    private suspend fun refreshAppFileTree(
        device: Device,
        app: InstalledApp,
        directory: AppDataDirectory,
        selectTree: AppState.() -> AppFileTreeState,
        updateTree: AppState.(AppFileTreeState) -> AppState,
    ) {
        if (currentState.selectedDevice != device || currentState.selectedApp != app) return

        update {
            updateTree(
                selectTree().copy(
                    isLoading = true,
                    errorMessage = null,
                ),
            )
        }

        val result = installedAppRepository.getAppFiles(device, app, directory)
        update {
            val nextTree =
                if (result.isOk) {
                    selectTree().copy(
                        entries = result.value,
                        isLoading = false,
                        errorMessage = null,
                    )
                } else {
                    selectTree().copy(
                        isLoading = false,
                        errorMessage = result.error.message ?: "Failed to load files",
                    )
                }

            updateTree(nextTree)
        }
    }

    private suspend fun toggleDataAppDirectory(entry: AppFileEntry.Directory) =
        toggleAppDirectory(
            entry = entry,
            selectTree = { dataFileTree },
            updateTree = { copy(dataFileTree = it) },
        )

    private suspend fun toggleSdCardDataAppDirectory(entry: AppFileEntry.Directory) =
        toggleAppDirectory(
            entry = entry,
            selectTree = { sdCardDataFileTree },
            updateTree = { copy(sdCardDataFileTree = it) },
        )

    private suspend fun toggleAppDirectory(
        entry: AppFileEntry.Directory,
        selectTree: AppState.() -> AppFileTreeState,
        updateTree: AppState.(AppFileTreeState) -> AppState,
    ) {
        val tree = currentState.selectTree()
        val isExpanded = tree.expandedPaths.contains(entry.path)
        val isLoaded = tree.childrenByPath.containsKey(entry.path)

        update {
            val currentTree = selectTree()
            val nextTree =
                if (isExpanded) {
                    currentTree.clearDirectoryCache(entry.path)
                } else {
                    currentTree.copy(expandedPaths = currentTree.expandedPaths + entry.path)
                }
            updateTree(nextTree)
        }

        if (!isExpanded && !isLoaded) {
            loadAppFileTreeChildren(entry, selectTree, updateTree)
        }
    }

    private fun AppFileTreeState.clearDirectoryCache(path: String): AppFileTreeState =
        copy(
            expandedPaths = expandedPaths.filterNot { it.isSameOrChildPath(path) }.toSet(),
            childrenByPath = childrenByPath.filterKeys { !it.isSameOrChildPath(path) },
            loadingPaths = loadingPaths.filterNot { it.isSameOrChildPath(path) }.toSet(),
            errorMessages = errorMessages.filterKeys { !it.isSameOrChildPath(path) },
        )

    private fun String.isSameOrChildPath(parentPath: String): Boolean {
        val parent = parentPath.trimEnd('/')
        val current = trimEnd('/')
        return current == parent || current.startsWith("$parent/")
    }

    private suspend fun loadAppFileTreeChildren(
        entry: AppFileEntry.Directory,
        selectTree: AppState.() -> AppFileTreeState,
        updateTree: AppState.(AppFileTreeState) -> AppState,
    ) {
        val device = currentState.selectedDevice ?: return
        val packageName = currentState.selectedApp?.packageName ?: return

        update {
            val tree = selectTree()
            updateTree(
                tree.copy(
                    loadingPaths = tree.loadingPaths + entry.path,
                    errorMessages = tree.errorMessages - entry.path,
                ),
            )
        }

        val result = installedAppRepository.getAppFileChildren(device, entry)
        if (currentState.selectedApp?.packageName != packageName) return
        if (!currentState.selectTree().loadingPaths.contains(entry.path)) return

        update {
            val tree = selectTree()
            val nextTree =
                if (result.isOk) {
                    tree.copy(
                        childrenByPath = tree.childrenByPath + (entry.path to result.value),
                        loadingPaths = tree.loadingPaths - entry.path,
                        errorMessages = tree.errorMessages - entry.path,
                    )
                } else {
                    tree.copy(
                        loadingPaths = tree.loadingPaths - entry.path,
                        errorMessages =
                            tree.errorMessages + (entry.path to (result.error.message ?: "Failed to load files")),
                    )
                }

            updateTree(nextTree)
        }
    }

    private suspend fun previewAppFile(entry: AppFileEntry) {
        val device = currentState.selectedDevice ?: return
        update {
            copy(
                filePreview =
                    AppFilePreviewState(
                        entry = entry,
                        isLoading = true,
                    ),
            )
        }

        val result = installedAppRepository.getAppFilePreview(device, entry)
        update {
            if (result.isOk) {
                copy(
                    filePreview =
                        AppFilePreviewState(
                            entry = entry,
                            preview = result.value,
                        ),
                )
            } else {
                copy(
                    filePreview =
                        AppFilePreviewState(
                            entry = entry,
                            errorMessage = result.error.message ?: "Failed to load preview",
                        ),
                )
            }
        }
    }
}
