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
) : PulseState {
    val filteredApps: List<InstalledApp>
        get() = filterInstalledApps(apps, searchText, sortType)

    val selectedApp: InstalledApp?
        get() = filteredApps.firstOrNull { it.packageName == selectedAppPackageName } ?: filteredApps.firstOrNull()
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
                listOfNotNull(
                    app.displayName,
                    app.packageName,
                    app.sourceDir,
                ).any { it.contains(normalized, ignoreCase = true) }
            }
        }

    return when (sortType) {
        SortType.SORT_BY_NAME_ASC -> filtered.sortedBy { it.packageName.lowercase(Locale.getDefault()) }
        SortType.SORT_BY_NAME_DESC -> filtered.sortedByDescending { it.packageName.lowercase(Locale.getDefault()) }
    }
}
