package jp.kaleidot725.adbpad.ui.screen.app.component.tree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.screen.app.state.AppFileTreeState

@Composable
fun AppFileTreeView(
    tree: AppFileTreeState,
    selectedFile: AppFileEntry?,
    onSelectNode: (AppFileEntry) -> Unit,
    onPreviewNode: (AppFileEntry) -> Unit,
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
                        tree = tree,
                        depth = 0,
                        selectedFile = selectedFile,
                        onSelectNode = onSelectNode,
                        onPreviewNode = onPreviewNode,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppFileTreeViewPreview() {
    val directory = previewAppFileEntries.first()

    AppFileTreeView(
        tree =
            AppFileTreeState(
                entries = previewAppFileEntries,
                expandedPaths = setOf(directory.path),
                childrenByPath = mapOf(directory.path to previewChildAppFileEntries),
            ),
        selectedFile = directory,
        onSelectNode = {},
        onPreviewNode = {},
        modifier = Modifier.width(280.dp).padding(16.dp),
    )
}
