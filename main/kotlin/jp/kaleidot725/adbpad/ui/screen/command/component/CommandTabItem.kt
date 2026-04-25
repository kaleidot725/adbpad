package jp.kaleidot725.adbpad.ui.screen.command.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Presentation
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground

@Composable
fun CommandTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .widthIn(min = 144.dp)
            .heightIn(min = 40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickableBackground(isSelected = isSelected, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.Center),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )

            val textStyle =
                if (isSelected) {
                    MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                } else {
                    MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal)
                }

            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = textStyle,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Composable
private fun CommandTabItemPreview() {
    Row {
        CommandTabItem("one", Lucide.Presentation, false, {})
        CommandTabItem("two", Lucide.Presentation, true, {})
    }
}
