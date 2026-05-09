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
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState
import jp.kaleidot725.adbpad.ui.screen.app.state.AppProcessState
import jp.kaleidot725.adbpad.ui.screen.app.state.AppSideEffect
import jp.kaleidot725.adbpad.ui.screen.app.state.AppState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.KeyboardFocusManager
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class AppStateHolder(
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val installedAppRepository: InstalledAppRepository,
) : PulseStore<AppState, AppAction, AppSideEffect, AppBroadCast>(
        initialUiState = AppState(),
    ) {
    private var appFileTreeTarget: AppFileTreeTarget? = null

    override fun onSetup() {
        collectSelectedDevice()
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

    private suspend fun reduceRefreshApps() {
        if (currentState.processState != AppProcessState.Idle) return

        update { copy(processState = AppProcessState.Loading) }
        try {
            refreshApps()
            refreshAppFileTreeRoots()
        } finally {
            if (currentState.processState == AppProcessState.Loading) {
                update { copy(processState = AppProcessState.Idle) }
            }
        }
    }

    private suspend fun reduceUpdateSearchText(text: String) {
        update { copy(searchText = text) }
        refreshAppFileTreeRoots()
    }

    private suspend fun reduceUpdateSortType(sortType: SortType) {
        update { copy(sortType = sortType) }
        refreshAppFileTreeRoots()
    }

    private suspend fun reduceSelectApp(app: InstalledApp) {
        update { copy(selectedApp = app) }
        refreshAppFileTreeRoots()
    }

    private suspend fun reduceInstallPackage() {
        if (currentState.processState != AppProcessState.Idle) return

        val device = currentState.selectedDevice ?: return
        val packageFile = selectInstallApplication() ?: return

        update { copy(processState = AppProcessState.Installing) }
        val isInstalled = installedAppRepository.installPackage(device, packageFile)
        update { copy(processState = AppProcessState.Idle) }
        if (isInstalled) reduceRefreshApps()
    }

    private suspend fun reduceUninstallApp(app: InstalledApp) {
        if (currentState.processState != AppProcessState.Idle) return
        val device = currentState.selectedDevice ?: return

        update { copy(processState = AppProcessState.Uninstalling) }
        val isUninstalled = installedAppRepository.uninstallInstalledApp(device, app)
        update { copy(processState = AppProcessState.Idle) }
        if (isUninstalled) reduceRefreshApps()
    }

    private suspend fun reduceSelectNextApp() {
        selectNextOrPreviousApp(offset = 1)
        refreshAppFileTreeRoots()
    }

    private suspend fun reduceSelectPreviousApp() {
        selectNextOrPreviousApp(offset = -1)
        refreshAppFileTreeRoots()
    }

    private suspend fun reduceSelectDataFileNode(entry: AppFileEntry) {
        update { copy(selectedDataFile = entry) }
        selectAppFileNode(AppDataDirectory.Data, currentState.dataFileTree, entry)
    }

    private suspend fun reduceSelectSdCardDataFileNode(entry: AppFileEntry) {
        update { copy(selectedSdCardDataFile = entry) }
        selectAppFileNode(AppDataDirectory.SdCardData, currentState.sdCardDataFileTree, entry)
    }

    private suspend fun refreshApps() {
        val device = currentState.selectedDevice ?: return
        val apps = installedAppRepository.getInstalledApps(device)
        update {
            copy(
                apps = apps,
                selectedApp = selectedApp?.takeIf { apps.contains(it) } ?: apps.firstOrNull(),
            )
        }
    }

    private suspend fun refreshAppFileTreeRoots() {
        val device = currentState.selectedDevice
        val app = currentState.selectedApp
        val nextTarget =
            if (device != null && app != null) {
                AppFileTreeTarget(
                    deviceSerial = device.serial,
                    packageName = app.packageName,
                )
            } else {
                null
            }

        if (nextTarget == null) {
            appFileTreeTarget = null
            update {
                copy(
                    dataFileTree = AppFileTreeState(),
                    sdCardDataFileTree = AppFileTreeState(),
                    selectedDataFile = null,
                    selectedSdCardDataFile = null,
                )
            }
            return
        }

        if (appFileTreeTarget == nextTarget) return

        appFileTreeTarget = null
        update {
            copy(
                dataFileTree = AppFileTreeState(),
                sdCardDataFileTree = AppFileTreeState(),
                selectedDataFile = null,
                selectedSdCardDataFile = null,
            )
        }

        val isDataLoaded = refreshAppFileTree(AppDataDirectory.Data)
        val selectedDevice = currentState.selectedDevice
        val selectedApp = currentState.selectedApp
        val selectedTarget =
            if (selectedDevice != null && selectedApp != null) {
                AppFileTreeTarget(
                    deviceSerial = selectedDevice.serial,
                    packageName = selectedApp.packageName,
                )
            } else {
                null
            }
        if (selectedTarget != nextTarget) return

        val isSdCardDataLoaded = refreshAppFileTree(AppDataDirectory.SdCardData)
        val currentDevice = currentState.selectedDevice
        val currentApp = currentState.selectedApp
        val currentTarget =
            if (currentDevice != null && currentApp != null) {
                AppFileTreeTarget(
                    deviceSerial = currentDevice.serial,
                    packageName = currentApp.packageName,
                )
            } else {
                null
            }

        if (isDataLoaded && isSdCardDataLoaded && currentTarget == nextTarget) {
            appFileTreeTarget = nextTarget
        }
    }

    private suspend fun refreshAppFileTree(directory: AppDataDirectory): Boolean {
        val device = currentState.selectedDevice ?: return false
        val app = currentState.selectedApp ?: return false
        val deviceSerial = device.serial
        val packageName = app.packageName
        var isLoadingFinished = false

        update {
            when (directory) {
                AppDataDirectory.Data -> {
                    copy(
                        dataFileTree =
                            dataFileTree.copy(
                                isLoading = true,
                                errorMessage = null,
                            ),
                    )
                }

                AppDataDirectory.SdCardData -> {
                    copy(
                        sdCardDataFileTree =
                            sdCardDataFileTree.copy(
                                isLoading = true,
                                errorMessage = null,
                            ),
                    )
                }
            }
        }

        try {
            val result = installedAppRepository.getAppFiles(device, app, directory)
            if (currentState.selectedDevice?.serial != deviceSerial || currentState.selectedApp?.packageName != packageName) {
                return false
            }

            update {
                val tree =
                    when (directory) {
                        AppDataDirectory.Data -> dataFileTree
                        AppDataDirectory.SdCardData -> sdCardDataFileTree
                    }
                val nextTree =
                    if (result.isOk) {
                        tree.copy(
                            entries = result.value,
                            isLoading = false,
                            errorMessage = null,
                        )
                    } else {
                        tree.copy(
                            isLoading = false,
                            errorMessage = result.error.message ?: "Failed to load files",
                        )
                    }

                when (directory) {
                    AppDataDirectory.Data -> copy(dataFileTree = nextTree)
                    AppDataDirectory.SdCardData -> copy(sdCardDataFileTree = nextTree)
                }
            }
            isLoadingFinished = true
            return result.isOk
        } finally {
            if (
                !isLoadingFinished &&
                currentState.selectedDevice?.serial == deviceSerial &&
                currentState.selectedApp?.packageName == packageName
            ) {
                update {
                    when (directory) {
                        AppDataDirectory.Data -> {
                            copy(dataFileTree = dataFileTree.copy(isLoading = false))
                        }

                        AppDataDirectory.SdCardData -> {
                            copy(sdCardDataFileTree = sdCardDataFileTree.copy(isLoading = false))
                        }
                    }
                }
            }
        }
    }

    private fun selectNextOrPreviousApp(offset: Int) {
        val filteredApps = currentState.filteredApps
        if (filteredApps.isEmpty()) return

        val selectedApp = currentState.selectedApp
        val currentIndex = if (selectedApp == null) -1 else filteredApps.indexOf(selectedApp)
        val targetIndex =
            when {
                currentIndex == -1 && offset > 0 -> 0
                currentIndex == -1 -> return
                else -> currentIndex + offset
            }

        if (targetIndex !in filteredApps.indices) return

        update {
            copy(
                selectedApp = filteredApps[targetIndex],
            )
        }
    }

    private suspend fun selectAppFileNode(
        directory: AppDataDirectory,
        tree: AppFileTreeState,
        entry: AppFileEntry,
    ) {
        if (!entry.isDirectory) return

        if (tree.expandedPaths.contains(entry.path)) {
            update {
                when (directory) {
                    AppDataDirectory.Data -> {
                        copy(dataFileTree = dataFileTree.copy(expandedPaths = dataFileTree.expandedPaths - entry.path))
                    }

                    AppDataDirectory.SdCardData -> {
                        copy(
                            sdCardDataFileTree =
                                sdCardDataFileTree.copy(
                                    expandedPaths = sdCardDataFileTree.expandedPaths - entry.path,
                                ),
                        )
                    }
                }
            }
            return
        }

        update {
            when (directory) {
                AppDataDirectory.Data -> {
                    copy(dataFileTree = dataFileTree.copy(expandedPaths = dataFileTree.expandedPaths + entry.path))
                }

                AppDataDirectory.SdCardData -> {
                    copy(
                        sdCardDataFileTree =
                            sdCardDataFileTree.copy(
                                expandedPaths = sdCardDataFileTree.expandedPaths + entry.path,
                            ),
                    )
                }
            }
        }
        if (!tree.childrenByPath.containsKey(entry.path)) {
            loadAppFileTreeChildren(directory, entry)
        }
    }

    private suspend fun loadAppFileTreeChildren(
        directory: AppDataDirectory,
        entry: AppFileEntry,
    ) {
        val device = currentState.selectedDevice ?: return
        val packageName = currentState.selectedApp?.packageName ?: return

        update {
            when (directory) {
                AppDataDirectory.Data -> {
                    copy(
                        dataFileTree =
                            dataFileTree.copy(
                                loadingPaths = dataFileTree.loadingPaths + entry.path,
                                errorMessages = dataFileTree.errorMessages - entry.path,
                            ),
                    )
                }

                AppDataDirectory.SdCardData -> {
                    copy(
                        sdCardDataFileTree =
                            sdCardDataFileTree.copy(
                                loadingPaths = sdCardDataFileTree.loadingPaths + entry.path,
                                errorMessages = sdCardDataFileTree.errorMessages - entry.path,
                            ),
                    )
                }
            }
        }

        val result = installedAppRepository.getAppFileChildren(device, entry)
        if (currentState.selectedApp?.packageName != packageName) return

        update {
            val tree =
                when (directory) {
                    AppDataDirectory.Data -> dataFileTree
                    AppDataDirectory.SdCardData -> sdCardDataFileTree
                }
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

            when (directory) {
                AppDataDirectory.Data -> copy(dataFileTree = nextTree)
                AppDataDirectory.SdCardData -> copy(sdCardDataFileTree = nextTree)
            }
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

    private fun collectSelectedDevice() {
        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collectLatest { device ->
                if (device == null) {
                    clearState()
                    return@collectLatest
                }

                update {
                    copy(
                        selectedDevice = device,
                    )
                }
                reduceRefreshApps()
            }
        }
    }

    private fun clearState() {
        appFileTreeTarget = null
        update { AppState() }
    }

    private data class AppFileTreeTarget(
        val deviceSerial: String,
        val packageName: String,
    )
}
