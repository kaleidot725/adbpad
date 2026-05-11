package jp.kaleidot725.adbpad.ui.screen.app.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.component.layout.ExpandableSection
import jp.kaleidot725.adbpad.ui.screen.app.component.tree.AppFileTreeView
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState

@Composable
fun AppDetailPane(
    app: InstalledApp?,
    isProcessing: Boolean,
    dataFileTree: AppFileTreeState,
    sdCardDataFileTree: AppFileTreeState,
    selectedDataFile: AppFileEntry?,
    selectedSdCardDataFile: AppFileEntry?,
    onSelectDataFileNode: (AppFileEntry) -> Unit,
    onSelectSdCardDataFileNode: (AppFileEntry) -> Unit,
    onPreviewDataFileNode: (AppFileEntry) -> Unit,
    onPreviewSdCardDataFileNode: (AppFileEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (app == null) {
        AppDetailEmptyState(modifier = modifier)
    } else {
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AppInitialIcon(
                    name = app.displayName,
                    isLoading = isProcessing,
                    modifier = Modifier.size(48.dp),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = app.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = app.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            ExpandableSection(title = Language.appDetailsTitle) {
                AppDetailPropertyRow(
                    label = Language.appPackageName,
                    value = app.packageName,
                )
            }

            ExpandableSection(title = Language.appDataDirectory) {
                AppDetailPropertyRow(
                    label = Language.appPathInfo,
                    value = app.dataDir,
                )
                AppFileTreeView(
                    tree = dataFileTree,
                    selectedFile = selectedDataFile,
                    onSelectNode = onSelectDataFileNode,
                    onPreviewNode = onPreviewDataFileNode,
                )
            }

            ExpandableSection(title = Language.appSdCardDataDirectory) {
                AppDetailPropertyRow(
                    label = Language.appPathInfo,
                    value = app.sdCardDataDir,
                )
                AppFileTreeView(
                    tree = sdCardDataFileTree,
                    selectedFile = selectedSdCardDataFile,
                    onSelectNode = onSelectSdCardDataFileNode,
                    onPreviewNode = onPreviewSdCardDataFileNode,
                )
            }
        }
    }
}
