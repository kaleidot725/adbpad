package jp.kaleidot725.adbpad.ui.screen.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.layout.ThreePaneLayout
import jp.kaleidot725.adbpad.ui.screen.app.component.AppDetailPane
import jp.kaleidot725.adbpad.ui.screen.app.component.AppHeader
import jp.kaleidot725.adbpad.ui.screen.app.component.AppList
import jp.kaleidot725.adbpad.ui.screen.app.state.AppAction
import jp.kaleidot725.adbpad.ui.screen.app.state.AppState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun AppScreen(
    state: AppState,
    onAction: (AppAction) -> Unit,
    splitterState: SplitPaneState,
    rightSplitterState: SplitPaneState,
) {
    ThreePaneLayout(
        splitterState = splitterState,
        rightSplitterState = rightSplitterState,
        left = {
            Column(modifier = Modifier.fillMaxSize()) {
                AppHeader(
                    searchText = state.searchText,
                    sortType = state.sortType,
                    isLoading = state.isLoading,
                    isInstalling = state.isInstalling,
                    canInstall = state.selectedDevice != null,
                    onUpdateSortType = { onAction(AppAction.UpdateSortType(it)) },
                    onUpdateSearchText = { onAction(AppAction.UpdateSearchText(it)) },
                    onRefresh = { onAction(AppAction.RefreshApps) },
                    onInstall = { onAction(AppAction.InstallPackage) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                )

                HorizontalDivider(color = UserColor.getSplitterColor())

                AppList(
                    selectedApp = state.selectedApp,
                    apps = state.filteredApps,
                    selectedDevice = state.selectedDevice,
                    isLoading = state.isLoading,
                    isProcessing = state.isUninstalling,
                    onSelectApp = { onAction(AppAction.SelectApp(it)) },
                    onUninstallApp = { onAction(AppAction.UninstallApp(it)) },
                    onNextApp = { onAction(AppAction.SelectNextApp) },
                    onPreviousApp = { onAction(AppAction.SelectPreviousApp) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        center = {
            AppDetailPane(
                app = state.selectedApp,
                isProcessing = state.isUninstalling,
                dataFileTree = state.dataFileTree,
                sdCardDataFileTree = state.sdCardDataFileTree,
                selectedDataFile = state.selectedDataFile,
                selectedSdCardDataFile = state.selectedSdCardDataFile,
                onSelectDataFileNode = { onAction(AppAction.SelectDataFileNode(it)) },
                onSelectSdCardDataFileNode = { onAction(AppAction.SelectSdCardDataFileNode(it)) },
                modifier = Modifier.fillMaxSize(),
            )
        },
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun AppScreenPreview() {
    val apps =
        listOf(
            InstalledApp(
                packageName = "com.example.notes",
            ),
            InstalledApp(
                packageName = "com.example.calendar",
            ),
        )

    AppScreen(
        state =
            AppState(
                apps = apps,
                selectedAppIndex = 0,
            ),
        onAction = {},
        splitterState = rememberSplitPaneState(initialPositionPercentage = 0.25f),
        rightSplitterState = rememberSplitPaneState(initialPositionPercentage = 0.7f),
    )
}
