package jp.kaleidot725.adbpad.ui.screen.app

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.app.state.AppAction
import jp.kaleidot725.adbpad.ui.screen.app.state.AppSideEffect
import jp.kaleidot725.adbpad.ui.screen.app.state.AppState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.CancellationException
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
        when (uiAction) {
            AppAction.RefreshApps -> {
                coroutineScope.launch { loadApps(currentState.selectedDevice) }
            }

            is AppAction.UpdateSearchText -> {
                updateSearchText(uiAction.text)
            }

            is AppAction.UpdateSortType -> {
                updateSortType(uiAction.sortType)
            }

            is AppAction.SelectApp -> {
                selectApp(uiAction.app)
            }

            AppAction.InstallPackage -> {
                installPackage()
            }

            is AppAction.UninstallApp -> {
                uninstallApp(uiAction.app)
            }

            AppAction.SelectNextApp -> {
                selectNextApp()
            }

            AppAction.SelectPreviousApp -> {
                selectPreviousApp()
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
                    errorMessage = null,
                )
            }
            return
        }

        update { copy(isLoading = true, errorMessage = null) }
        runCatching { installedAppRepository.getInstalledApps(device) }
            .onSuccess { apps -> updateApps(apps) }
            .onFailure { throwable ->
                if (throwable is CancellationException) throw throwable
                update {
                    copy(
                        apps = emptyList(),
                        filteredApps = emptyList(),
                        selectedAppPackageName = null,
                        isLoading = false,
                        errorMessage = throwable.message,
                    )
                }
            }
    }

    private fun updateApps(apps: List<InstalledApp>) {
        update {
            val filteredApps = filterInstalledApps(apps, searchText, sortType)
            val nextSelection =
                when {
                    filteredApps.any { it.packageName == selectedAppPackageName } -> selectedAppPackageName
                    else -> filteredApps.firstOrNull()?.packageName
                }

            copy(
                apps = apps,
                filteredApps = filteredApps,
                selectedAppPackageName = nextSelection,
                isLoading = false,
                errorMessage = null,
            )
        }
    }

    private fun updateSearchText(text: String) {
        update {
            val filteredApps = filterInstalledApps(apps, text, sortType)
            val nextSelection =
                when {
                    filteredApps.any { it.packageName == selectedAppPackageName } -> selectedAppPackageName
                    else -> filteredApps.firstOrNull()?.packageName
                }

            copy(
                searchText = text,
                filteredApps = filteredApps,
                selectedAppPackageName = nextSelection,
            )
        }
    }

    private fun updateSortType(sortType: SortType) {
        update {
            val filteredApps = filterInstalledApps(apps, searchText, sortType)
            val nextSelection =
                when {
                    filteredApps.any { it.packageName == selectedAppPackageName } -> selectedAppPackageName
                    else -> filteredApps.firstOrNull()?.packageName
                }

            copy(
                sortType = sortType,
                filteredApps = filteredApps,
                selectedAppPackageName = nextSelection,
            )
        }
    }

    private fun selectApp(app: InstalledApp) {
        update { copy(selectedAppPackageName = app.packageName) }
    }

    private fun installPackage() {
        val device = currentState.selectedDevice ?: return
        if (currentState.isInstalling) return

        coroutineScope.launch {
            val packageFile = selectInstallPackageFile() ?: return@launch
            if (currentState.isInstalling) return@launch

            update { copy(isInstalling = true) }
            runCatching { installedAppRepository.installPackage(device, packageFile) }
                .onSuccess {
                    update { copy(isInstalling = false) }
                    loadApps(device)
                }.onFailure { throwable ->
                    if (throwable is CancellationException) throw throwable
                    update { copy(isInstalling = false) }
                }
        }
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

    private fun uninstallApp(app: InstalledApp) {
        val device = currentState.selectedDevice ?: return
        if (currentState.isProcessing(app)) return

        update {
            copy(
                uninstallingPackageNames = uninstallingPackageNames + app.packageName,
            )
        }

        coroutineScope.launch {
            runCatching { installedAppRepository.uninstallInstalledApp(device, app) }
                .onSuccess {
                    update {
                        copy(
                            uninstallingPackageNames = uninstallingPackageNames - app.packageName,
                        )
                    }
                    loadApps(device)
                }.onFailure { throwable ->
                    if (throwable is CancellationException) throw throwable
                    update {
                        copy(
                            uninstallingPackageNames = uninstallingPackageNames - app.packageName,
                        )
                    }
                }
        }
    }

    private fun selectNextApp() {
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

        update { copy(selectedAppPackageName = filteredApps[nextIndex].packageName) }
    }

    private fun selectPreviousApp() {
        val filteredApps = currentState.filteredApps
        if (filteredApps.isEmpty()) return

        val currentPackageName = currentState.selectedApp?.packageName ?: return
        val currentIndex = filteredApps.indexOfFirst { it.packageName == currentPackageName }
        if (currentIndex <= 0) return

        update { copy(selectedAppPackageName = filteredApps[currentIndex - 1].packageName) }
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
