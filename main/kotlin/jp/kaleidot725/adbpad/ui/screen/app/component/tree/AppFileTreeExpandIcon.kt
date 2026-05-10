package jp.kaleidot725.adbpad.ui.screen.app.component.tree

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Lucide

@Composable
internal fun AppFileTreeExpandIcon(
    isDirectory: Boolean,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.size(14.dp), contentAlignment = Alignment.Center) {
        if (isDirectory) {
            Icon(
                imageVector = if (isExpanded) Lucide.ChevronDown else Lucide.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Preview
@Composable
private fun AppFileTreeExpandIconPreview() {
    AppFileTreeExpandIcon(
        isDirectory = true,
        isExpanded = true,
        modifier = Modifier.width(24.dp),
    )
}

