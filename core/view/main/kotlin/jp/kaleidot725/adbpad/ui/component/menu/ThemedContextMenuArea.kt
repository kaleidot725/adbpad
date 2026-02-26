package jp.kaleidot725.adbpad.ui.component.menu

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun ThemedContextMenuArea(
    items: () -> List<ContextMenuItem>,
    content: @Composable () -> Unit,
) {
    // Create custom representation with Material 3 theme colors matching DropdownMenu
    val customRepresentation =
        DefaultContextMenuRepresentation(
            backgroundColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            itemHoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
        )

    CompositionLocalProvider(
        LocalContextMenuRepresentation provides customRepresentation,
    ) {
        ContextMenuArea(
            items = items,
            content = content,
        )
    }
}
