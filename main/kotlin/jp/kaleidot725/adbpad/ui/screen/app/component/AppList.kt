package jp.kaleidot725.adbpad.ui.screen.app.component

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.component.menu.ThemedContextMenuArea

@Composable
fun AppList(
    selectedApp: InstalledApp?,
    apps: List<InstalledApp>,
    selectedDevice: Device?,
    isLoading: Boolean,
    isProcessing: Boolean,
    onSelectApp: (InstalledApp) -> Unit,
    onUninstallApp: (InstalledApp) -> Unit,
    onNextApp: () -> Unit,
    onPreviousApp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when {
            selectedDevice == null -> {
                AppListEmptyState(text = Language.notFoundDevice, modifier = Modifier.align(Alignment.Center))
            }

            apps.isNotEmpty() -> {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .onKeyEvent { event ->
                                when {
                                    event.key == Key.DirectionUp && event.type == KeyEventType.KeyDown -> {
                                        onPreviousApp()
                                        true
                                    }

                                    event.key == Key.DirectionDown && event.type == KeyEventType.KeyDown -> {
                                        onNextApp()
                                        true
                                    }

                                    else -> false
                                }
                            },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 24.dp),
                ) {
                    items(apps, key = { it.packageName }) { app ->
                        ThemedContextMenuArea(
                            items = {
                                listOf(
                                    ContextMenuItem(
                                        label = Language.uninstallApp,
                                        onClick = { onUninstallApp(app) },
                                    ),
                                )
                            },
                        ) {
                            AppListItem(
                                app = app,
                                isProcessing = isProcessing,
                                isSelected = selectedApp?.packageName == app.packageName,
                                onClick = { onSelectApp(app) },
                            )
                        }
                    }
                }
            }

            isLoading -> {
                AppListLoadingState(modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                AppListEmptyState(text = Language.notFoundApp, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
