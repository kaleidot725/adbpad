package jp.kaleidot725.adbpad.ui.screen.app.state

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.pulse.mvi.PulseState
import java.util.Locale

data class AppState(
    val apps: List<InstalledApp> = emptyList(),
    val selectedAppPackageName: String? = null,
    val selectedDevice: Device? = null,
    val searchText: String = "",
    val sortType: SortType = SortType.SORT_BY_NAME_ASC,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val uninstallingPackageNames: Set<String> = emptySet(),
    val isInstalling: Boolean = false,
) : PulseState {
    val filteredApps: List<InstalledApp>
        get() = filterInstalledApps(apps, searchText, sortType)

    val selectedApp: InstalledApp?
        get() = filteredApps.firstOrNull { it.packageName == selectedAppPackageName } ?: filteredApps.firstOrNull()

    fun isUninstalling(app: InstalledApp): Boolean = uninstallingPackageNames.contains(app.packageName)

    fun isProcessing(app: InstalledApp): Boolean = isUninstalling(app)
}

internal fun filterInstalledApps(
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
