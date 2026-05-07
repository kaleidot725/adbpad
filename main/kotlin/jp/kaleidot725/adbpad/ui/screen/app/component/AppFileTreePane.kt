package jp.kaleidot725.adbpad.ui.screen.app.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.File
import com.composables.icons.lucide.Folder
import com.composables.icons.lucide.Lucide
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground
import jp.kaleidot725.adbpad.ui.component.indicator.RunningIndicator
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState

@Composable
fun AppFileTreeView(
    tree: AppFileTreeState,
    selectedFile: AppFileEntry?,
    onSelectNode: (AppFileEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        when {
            tree.isLoading && tree.entries.isEmpty() -> AppFileTreeLoadingRow()
            tree.errorMessage != null -> AppFileTreeMessageRow(tree.errorMessage.ifBlank { Language.appFileTreeEmpty })
            tree.entries.isEmpty() -> AppFileTreeMessageRow(Language.appFileTreeEmpty)
            else -> {
                tree.entries.forEach { entry ->
                    AppFileTreeNode(
                        entry = entry,
                        selectedFile = selectedFile,
                        onSelectNode = onSelectNode,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppFileTreeNode(
    entry: AppFileEntry,
    selectedFile: AppFileEntry?,
    onSelectNode: (AppFileEntry) -> Unit,
) {
    val isSelected = selectedFile?.path == entry.path

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickableBackground(
                    isSelected = isSelected,
                    shape = RoundedCornerShape(4.dp),
                ).clickable { onSelectNode(entry) }
                .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector =
                if (entry.isDirectory) {
                    Lucide.Folder
                } else {
                    Lucide.File
                },
            contentDescription = null,
            tint =
                if (entry.isDirectory) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = entry.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AppFileTreeLoadingRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RunningIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = Language.loadingAppFiles,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AppFileTreeMessageRow(
    message: String,
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}
