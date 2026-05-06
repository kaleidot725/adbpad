package jp.kaleidot725.adbpad.ui.screen.app.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.File
import com.composables.icons.lucide.Folder
import com.composables.icons.lucide.FolderOpen
import com.composables.icons.lucide.Lucide
import jp.kaleidot725.adbpad.domain.model.app.AppDataDirectory
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground
import jp.kaleidot725.adbpad.ui.component.indicator.RunningIndicator
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileSelection
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState

@Composable
fun AppFileTreeView(
    rootPath: String,
    directory: AppDataDirectory,
    tree: AppFileTreeState,
    selectedFile: AppFileSelection?,
    onSelectNode: (AppDataDirectory, AppFileEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        AppFileTreeRoot(
            rootPath = rootPath,
            directory = directory,
            tree = tree,
            selectedFile = selectedFile,
            onSelectNode = onSelectNode,
        )
    }
}

@Composable
private fun AppFileTreeRoot(
    rootPath: String,
    directory: AppDataDirectory,
    tree: AppFileTreeState,
    selectedFile: AppFileSelection?,
    onSelectNode: (AppDataDirectory, AppFileEntry) -> Unit,
) {
    val entries = tree.childrenByPath[rootPath]
    val isLoading = tree.loadingPaths.contains(rootPath)
    val errorMessage = tree.errorMessages[rootPath]

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        when {
            isLoading && entries == null -> AppFileTreeLoadingRow()
            errorMessage != null -> AppFileTreeMessageRow(errorMessage.ifBlank { Language.appFileTreeEmpty })
            entries.isNullOrEmpty() -> AppFileTreeMessageRow(Language.appFileTreeEmpty)
            else -> {
                entries.forEach { entry ->
                    AppFileTreeNode(
                        directory = directory,
                        entry = entry,
                        tree = tree,
                        depth = 0,
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
    directory: AppDataDirectory,
    entry: AppFileEntry,
    tree: AppFileTreeState,
    depth: Int,
    selectedFile: AppFileSelection?,
    onSelectNode: (AppDataDirectory, AppFileEntry) -> Unit,
) {
    val isExpanded = tree.expandedPaths.contains(entry.path)
    val isLoading = tree.loadingPaths.contains(entry.path)
    val childEntries = tree.childrenByPath[entry.path].orEmpty()
    val errorMessage = tree.errorMessages[entry.path]
    val isSelected = selectedFile?.directory == directory && selectedFile.entry.path == entry.path

    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickableBackground(
                        isSelected = isSelected,
                        shape = RoundedCornerShape(4.dp),
                    ).clickable { onSelectNode(directory, entry) }
                    .padding(start = (depth * 16).dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            AppFileTreeExpandIcon(entry = entry, isExpanded = isExpanded)
            Icon(
                imageVector =
                    when {
                        entry.isDirectory && isExpanded -> Lucide.FolderOpen
                        entry.isDirectory -> Lucide.Folder
                        else -> Lucide.File
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
            if (isLoading) {
                RunningIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        if (isExpanded) {
            when {
                errorMessage != null -> AppFileTreeMessageRow(
                    message = errorMessage.ifBlank { Language.appFileTreeEmpty },
                    depth = depth + 1,
                )
                childEntries.isEmpty() && !isLoading -> AppFileTreeMessageRow(
                    message = Language.appFileTreeEmpty,
                    depth = depth + 1,
                )
                else -> childEntries.forEach { child ->
                    AppFileTreeNode(
                        directory = directory,
                        entry = child,
                        tree = tree,
                        depth = depth + 1,
                        selectedFile = selectedFile,
                        onSelectNode = onSelectNode,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppFileTreeExpandIcon(
    entry: AppFileEntry,
    isExpanded: Boolean,
) {
    Box(modifier = Modifier.size(14.dp), contentAlignment = Alignment.Center) {
        if (entry.isDirectory) {
            Icon(
                imageVector = if (isExpanded) Lucide.ChevronDown else Lucide.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
        }
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
    depth: Int = 0,
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(start = (depth * 16).dp, top = 6.dp, bottom = 6.dp),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}
