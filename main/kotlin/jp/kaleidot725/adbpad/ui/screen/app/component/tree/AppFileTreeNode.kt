package jp.kaleidot725.adbpad.ui.screen.app.component.tree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.File
import com.composables.icons.lucide.Folder
import com.composables.icons.lucide.FolderOpen
import com.composables.icons.lucide.Lucide
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground
import jp.kaleidot725.adbpad.ui.component.indicator.RunningIndicator
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState

@Composable
internal fun AppFileTreeNode(
    entry: AppFileEntry,
    tree: AppFileTreeState,
    depth: Int,
    selectedFile: AppFileEntry?,
    onSelectNode: (AppFileEntry) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isExpanded = tree.expandedPaths.contains(entry.path)
    val isLoading = tree.loadingPaths.contains(entry.path)
    val childEntries = tree.childrenByPath[entry.path].orEmpty()
    val errorMessage = tree.errorMessages[entry.path]
    val isSelected = selectedFile?.path == entry.path

    Column(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickableBackground(
                        isSelected = isSelected,
                        shape = RoundedCornerShape(4.dp),
                    ).clickable { onSelectNode(entry) }
                    .padding(start = (depth * 16).dp, top = 4.dp, end = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            AppFileTreeExpandIcon(
                isDirectory = entry.isDirectory,
                isExpanded = isExpanded,
            )
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
                else -> {
                    childEntries.forEach { child ->
                        AppFileTreeNode(
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
}

@Preview
@Composable
private fun AppFileTreeNodePreview() {
    val directory = previewAppFileEntries.first()

    AppFileTreeNode(
        entry = directory,
        tree =
            AppFileTreeState(
                expandedPaths = setOf(directory.path),
                childrenByPath = mapOf(directory.path to previewChildAppFileEntries),
            ),
        depth = 0,
        selectedFile = directory,
        onSelectNode = {},
        modifier = Modifier.width(280.dp).padding(16.dp),
    )
}
