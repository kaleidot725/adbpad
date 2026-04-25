package jp.kaleidot725.adbpad.ui.screen.app

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.usecase.app.GetInstalledAppsUseCase
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.app.state.AppAction
import jp.kaleidot725.adbpad.ui.screen.app.state.AppSideEffect
import jp.kaleidot725.adbpad.ui.screen.app.state.AppState
import jp.kaleidot725.adbpad.ui.screen.app.state.filterInstalledApps
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AppStateHolder(
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val getInstalledAppsUseCase: GetInstalledAppsUseCase,
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
                    selectedAppPackageName = null,
                    isLoading = false,
                    errorMessage = null,
                )
            }
            return
        }

        update { copy(isLoading = true, errorMessage = null) }
        runCatching { getInstalledAppsUseCase(device) }
            .onSuccess { apps -> updateApps(apps) }
            .onFailure { throwable ->
                if (throwable is CancellationException) throw throwable
                update {
                    copy(
                        apps = emptyList(),
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
                selectedAppPackageName = nextSelection,
            )
        }
    }

    private fun selectApp(app: InstalledApp) {
        update { copy(selectedAppPackageName = app.packageName) }
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
}
