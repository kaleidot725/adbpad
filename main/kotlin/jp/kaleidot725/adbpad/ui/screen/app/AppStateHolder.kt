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
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileSelection
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState
import jp.kaleidot725.adbpad.ui.screen.app.state.AppSideEffect
import jp.kaleidot725.adbpad.ui.screen.app.state.AppState
import jp.kaleidot725.adbpad.ui.screen.app.state.createDefaultFileTrees
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.KeyboardFocusManager
import java.io.File
import java.util.Locale
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class AppStateHolder(
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val installedAppRepository: InstalledAppRepository,
) : PulseStore<AppState, AppAction, AppSideEffect, AppBroadCast>(
        initialUiState = AppState(),
    ) {
    override fun onSetup() {
        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collectLatest { device ->
                update { copy(selectedDevice = device) }
                loadApps(device)
            }
        }
    }

    override fun onAction(uiAction: AppAction) {
        coroutineScope.launch {
            when (uiAction) {
                AppAction.RefreshApps -> loadApps(currentState.selectedDevice)
                is AppAction.UpdateSearchText -> updateSearchText(uiAction.text)
                is AppAction.UpdateSortType -> updateSortType(uiAction.sortType)
                is AppAction.SelectApp -> selectApp(uiAction.app)
                AppAction.InstallPackage -> installPackage()
                is AppAction.UninstallApp -> uninstallApp(uiAction.app)
                AppAction.SelectNextApp -> selectNextApp()
                AppAction.SelectPreviousApp -> selectPreviousApp()
                is AppAction.RefreshAppFileTree -> refreshAppFileTree(uiAction.directory)
                is AppAction.SelectAppFileNode -> selectAppFileNode(uiAction.directory, uiAction.entry)
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {
                coroutineScope.launch { loadApps(currentState.selectedDevice) }
            }
        }
    }

    private suspend fun loadApps(device: Device?) {
        if (device == null) {
            update {
                copy(
                    apps = emptyList(),
                    filteredApps = emptyList(),
                    selectedAppPackageName = null,
                    isLoading = false,
                    fileTrees = createDefaultFileTrees(),
                    selectedFile = null,
                )
            }
            return
        }

        update { copy(isLoading = true) }

        val apps = installedAppRepository.getInstalledApps(device)
        val filteredApps = filterInstalledApps(apps, currentState.searchText, currentState.sortType)
        val nextSelection =
            when {
                filteredApps.any { it.packageName == currentState.selectedAppPackageName } -> currentState.selectedAppPackageName
                else -> filteredApps.firstOrNull()?.packageName
            }
        val selectedApp = filteredApps.firstOrNull { it.packageName == nextSelection }
        update {
            copy(
                apps = apps,
                filteredApps = filteredApps,
                selectedAppPackageName = nextSelection,
                isLoading = false,
                fileTrees = createDefaultFileTrees(),
                selectedFile = null,
            )
        }
        if (selectedApp != null) {
            loadAppFileTreeRoots(device, selectedApp)
        }
    }

    private suspend fun updateSearchText(text: String) {
        var nextSelectedApp: InstalledApp? = null
        var shouldLoadFileTrees = false
        update {
            val filteredApps = filterInstalledApps(apps, text, sortType)
            val nextSelection =
                when {
                    filteredApps.any { it.packageName == selectedAppPackageName } -> selectedAppPackageName
                    else -> filteredApps.firstOrNull()?.packageName
                }
            nextSelectedApp = filteredApps.firstOrNull { it.packageName == nextSelection }
            shouldLoadFileTrees = nextSelection != selectedAppPackageName

            copy(
                searchText = text,
                filteredApps = filteredApps,
                selectedAppPackageName = nextSelection,
                fileTrees = if (shouldLoadFileTrees) createDefaultFileTrees() else fileTrees,
                selectedFile = if (shouldLoadFileTrees) null else selectedFile,
            )
        }
        val device = currentState.selectedDevice
        val app = nextSelectedApp
        if (shouldLoadFileTrees && device != null && app != null) {
            loadAppFileTreeRoots(device, app)
        }
    }

    private suspend fun updateSortType(sortType: SortType) {
        var nextSelectedApp: InstalledApp? = null
        var shouldLoadFileTrees = false
        update {
            val filteredApps = filterInstalledApps(apps, searchText, sortType)
            val nextSelection =
                when {
                    filteredApps.any { it.packageName == selectedAppPackageName } -> selectedAppPackageName
                    else -> filteredApps.firstOrNull()?.packageName
                }
            nextSelectedApp = filteredApps.firstOrNull { it.packageName == nextSelection }
            shouldLoadFileTrees = nextSelection != selectedAppPackageName

            copy(
                sortType = sortType,
                filteredApps = filteredApps,
                selectedAppPackageName = nextSelection,
                fileTrees = if (shouldLoadFileTrees) createDefaultFileTrees() else fileTrees,
                selectedFile = if (shouldLoadFileTrees) null else selectedFile,
            )
        }
        val device = currentState.selectedDevice
        val app = nextSelectedApp
        if (shouldLoadFileTrees && device != null && app != null) {
            loadAppFileTreeRoots(device, app)
        }
    }

    private suspend fun selectApp(app: InstalledApp) {
        val shouldLoadFileTrees = currentState.selectedAppPackageName != app.packageName
        update {
            copy(
                selectedAppPackageName = app.packageName,
                fileTrees = if (shouldLoadFileTrees) createDefaultFileTrees() else fileTrees,
                selectedFile = if (shouldLoadFileTrees) null else selectedFile,
            )
        }
        val device = currentState.selectedDevice
        if (shouldLoadFileTrees && device != null) {
            loadAppFileTreeRoots(device, app)
        }
    }

    private suspend fun installPackage() {
        val device = currentState.selectedDevice ?: return
        if (currentState.isInstalling) return

        val packageFile = selectInstallPackageFile() ?: return
        if (currentState.isInstalling) return

        update { copy(isInstalling = true) }
        val isInstalled = installedAppRepository.installPackage(device, packageFile)
        update { copy(isInstalling = false) }
        if (isInstalled) loadApps(device)
    }

    private suspend fun selectInstallPackageFile(): File? =
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

    private suspend fun uninstallApp(app: InstalledApp) {
        val device = currentState.selectedDevice ?: return
        if (currentState.isProcessing(app)) return

        update {
            copy(
                uninstallingPackageNames = uninstallingPackageNames + app.packageName,
            )
        }

        val isUninstalled = installedAppRepository.uninstallInstalledApp(device, app)
        update {
            copy(
                uninstallingPackageNames = uninstallingPackageNames - app.packageName,
            )
        }
        if (isUninstalled) loadApps(device)
    }

    private suspend fun selectNextApp() {
        val filteredApps = currentState.filteredApps
        if (filteredApps.isEmpty()) return

        val currentPackageName = currentState.selectedApp?.packageName
        val currentIndex = filteredApps.indexOfFirst { it.packageName == currentPackageName }
        val nextIndex =
            when {
                currentIndex == -1 -> 0
                currentIndex < filteredApps.lastIndex -> currentIndex + 1
                else -> return
            }

        selectApp(filteredApps[nextIndex])
    }

    private suspend fun selectPreviousApp() {
        val filteredApps = currentState.filteredApps
        if (filteredApps.isEmpty()) return

        val currentPackageName = currentState.selectedApp?.packageName ?: return
        val currentIndex = filteredApps.indexOfFirst { it.packageName == currentPackageName }
        if (currentIndex <= 0) return

        selectApp(filteredApps[currentIndex - 1])
    }

    private suspend fun refreshAppFileTree(directory: AppDataDirectory) {
        val device = currentState.selectedDevice ?: return
        val app = currentState.selectedApp ?: return
        update {
            copy(
                fileTrees = fileTrees + (directory to AppFileTreeState(directory = directory)),
                selectedFile = selectedFile?.takeUnless { it.directory == directory },
            )
        }
        loadAppFileTreeNode(device, app, directory, directory.getRootPath(app))
    }

    private suspend fun selectAppFileNode(
        directory: AppDataDirectory,
        entry: AppFileEntry,
    ) {
        update { copy(selectedFile = AppFileSelection(directory, entry)) }
        if (!entry.isDirectory) return

        val tree = currentState.getFileTree(directory)
        if (tree.expandedPaths.contains(entry.path)) {
            updateFileTree(directory) { copy(expandedPaths = expandedPaths - entry.path) }
            return
        }

        updateFileTree(directory) { copy(expandedPaths = expandedPaths + entry.path) }
        if (!tree.childrenByPath.containsKey(entry.path)) {
            val device = currentState.selectedDevice ?: return
            val app = currentState.selectedApp ?: return
            loadAppFileTreeNode(device, app, directory, entry.path)
        }
    }

    private suspend fun loadAppFileTreeRoots(
        device: Device,
        app: InstalledApp,
    ) {
        AppDataDirectory.values().forEach { directory ->
            loadAppFileTreeNode(device, app, directory, directory.getRootPath(app))
        }
    }

    private suspend fun loadAppFileTreeNode(
        device: Device,
        app: InstalledApp,
        directory: AppDataDirectory,
        path: String,
    ) {
        updateFileTree(directory) {
            copy(
                expandedPaths = expandedPaths + path,
                loadingPaths = loadingPaths + path,
                errorMessages = errorMessages - path,
            )
        }

        val result = installedAppRepository.getAppFiles(device, app, directory, path)
        if (currentState.selectedAppPackageName != app.packageName) return

        updateFileTree(directory) {
            if (result.isOk) {
                copy(
                    childrenByPath = childrenByPath + (path to result.value),
                    loadingPaths = loadingPaths - path,
                    errorMessages = errorMessages - path,
                )
            } else {
                copy(
                    loadingPaths = loadingPaths - path,
                    errorMessages = errorMessages + (path to result.error),
                )
            }
        }
    }

    private fun updateFileTree(
        directory: AppDataDirectory,
        transform: AppFileTreeState.() -> AppFileTreeState,
    ) {
        update {
            copy(
                fileTrees = fileTrees + (directory to getFileTree(directory).transform()),
            )
        }
    }

    private fun filterInstalledApps(
        apps: List<InstalledApp>,
        query: String,
        sortType: SortType,
    ): List<InstalledApp> {
        val normalized = query.trim().lowercase(Locale.getDefault())
        val filtered =
            if (normalized.isBlank()) {
                apps
            } else {
                apps.filter { app ->
                    listOf(
                        app.displayName,
                        app.packageName,
                    ).any { it.contains(normalized, ignoreCase = true) }
                }
            }

        return when (sortType) {
            SortType.SORT_BY_NAME_ASC -> filtered.sortedBy { it.packageName.lowercase(Locale.getDefault()) }
            SortType.SORT_BY_NAME_DESC -> filtered.sortedByDescending { it.packageName.lowercase(Locale.getDefault()) }
        }
    }
}
