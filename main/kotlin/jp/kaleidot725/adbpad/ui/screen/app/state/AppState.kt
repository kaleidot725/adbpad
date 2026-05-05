package jp.kaleidot725.adbpad.ui.screen.app.state

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.pulse.mvi.PulseState

data class AppState(
    val apps: List<InstalledApp> = emptyList(),
    val filteredApps: List<InstalledApp> = emptyList(),
    val selectedAppPackageName: String? = null,
    val selectedDevice: Device? = null,
    val searchText: String = "",
    val sortType: SortType = SortType.SORT_BY_NAME_ASC,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val uninstallingPackageNames: Set<String> = emptySet(),
    val isInstalling: Boolean = false,
) : PulseState {
    val selectedApp: InstalledApp?
        get() = filteredApps.firstOrNull { it.packageName == selectedAppPackageName } ?: filteredApps.firstOrNull()

    fun isUninstalling(app: InstalledApp): Boolean = uninstallingPackageNames.contains(app.packageName)

    fun isProcessing(app: InstalledApp): Boolean = isUninstalling(app)
}
