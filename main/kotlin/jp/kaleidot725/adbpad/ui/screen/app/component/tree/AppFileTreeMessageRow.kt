package jp.kaleidot725.adbpad.ui.screen.app.component.tree

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.language.Language

@Composable
internal fun AppFileTreeMessageRow(
    message: String,
    depth: Int = 0,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.fillMaxWidth().padding(start = (depth * 16).dp, top = 6.dp, bottom = 6.dp),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview
@Composable
private fun AppFileTreeMessageRowPreview() {
    AppFileTreeMessageRow(
        message = Language.appFileTreeEmpty,
        modifier = Modifier.width(280.dp).padding(16.dp),
    )
}
