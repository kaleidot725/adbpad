package jp.kaleidot725.adbpad.ui.screen.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                    onUpdateSortType = { onAction(AppAction.UpdateSortType(it)) },
                    onUpdateSearchText = { onAction(AppAction.UpdateSearchText(it)) },
                    onRefresh = { onAction(AppAction.RefreshApps) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                )

                HorizontalDivider(color = UserColor.getSplitterColor())

                AppList(
                    selectedApp = state.selectedApp,
                    apps = state.filteredApps,
                    selectedDevice = state.selectedDevice,
                    isLoading = state.isLoading,
                    errorMessage = state.errorMessage,
                    onSelectApp = { onAction(AppAction.SelectApp(it)) },
                    onNextApp = { onAction(AppAction.SelectNextApp) },
                    onPreviousApp = { onAction(AppAction.SelectPreviousApp) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        center = {
            AppDetailPane(
                app = state.selectedApp,
                modifier = Modifier.fillMaxSize(),
            )
        },
    )
}

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun AppScreenPreview() {
    AppScreen(
        state =
            AppState(
                apps =
                    listOf(
                        InstalledApp(
                            packageName = "com.example.notes",
                            sourceDir = "/data/app/com.example.notes/base.apk",
                        ),
                        InstalledApp(
                            packageName = "com.example.calendar",
                            sourceDir = "/data/app/com.example.calendar/base.apk",
                        ),
                    ),
                selectedAppPackageName = "com.example.notes",
            ),
        onAction = {},
        splitterState = rememberSplitPaneState(initialPositionPercentage = 0.25f),
        rightSplitterState = rememberSplitPaneState(initialPositionPercentage = 0.7f),
    )
}
