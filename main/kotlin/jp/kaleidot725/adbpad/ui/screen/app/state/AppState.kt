package jp.kaleidot725.adbpad.ui.screen.app.state

import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.pulse.mvi.PulseState
import java.util.Locale

data class AppState(
    val selectedDevice: Device? = null,
    val apps: List<InstalledApp> = emptyList(),
    val selectedAppIndex: Int? = null,
    val searchText: String = "",
    val sortType: SortType = SortType.SORT_BY_NAME_ASC,
    val processState: AppProcessState = AppProcessState.Idle,
    val dataFileTree: AppFileTreeState = AppFileTreeState(),
    val selectedDataFile: AppFileEntry? = null,
    val sdCardDataFileTree: AppFileTreeState = AppFileTreeState(),
    val selectedSdCardDataFile: AppFileEntry? = null,
) : PulseState {
    val isLoading: Boolean = processState == AppProcessState.Loading
    val isUninstalling: Boolean = processState == AppProcessState.Uninstalling
    val isInstalling: Boolean = processState == AppProcessState.Installing

    val selectedApp: InstalledApp? get() = selectedAppIndex?.let { apps.getOrNull(it) }

    val filteredApps: List<InstalledApp>
        get() {
            val normalized = searchText.trim().lowercase(Locale.getDefault())
            val filtered =
                if (normalized.isBlank()) {
                    apps
                } else {
                    apps.filter { app ->
                        app.displayName.contains(normalized, ignoreCase = true) ||
                            app.packageName.contains(normalized, ignoreCase = true)
                    }
                }

            return when (sortType) {
                SortType.SORT_BY_NAME_ASC -> {
                    filtered.sortedBy { it.packageName.lowercase(Locale.getDefault()) }
                }

                SortType.SORT_BY_NAME_DESC -> {
                    filtered.sortedByDescending {
                        it.packageName.lowercase(Locale.getDefault())
                    }
                }
            }
        }
}

enum class AppProcessState {
    Idle,
    Loading,
    Uninstalling,
    Installing,
}

data class AppFileTreeState(
    val entries: List<AppFileEntry> = emptyList(),
    val expandedPaths: Set<String> = emptySet(),
    val childrenByPath: Map<String, List<AppFileEntry>> = emptyMap(),
    val loadingPaths: Set<String> = emptySet(),
    val errorMessages: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
