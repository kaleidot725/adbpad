package jp.kaleidot725.adbpad.ui.component.rail

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Power
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavigationRailItem(
    label: String,
    icon: ImageVector,
    contentDescription: String?,
    isSelected: Boolean,
    isCollapsed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val content =
        @Composable {
            Box(
                modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickableBackground(isSelected = isSelected, shape = RoundedCornerShape(8.dp))
                    .clickable(onClick = onClick)
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (isCollapsed) Arrangement.Center else Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        modifier = Modifier.size(18.dp),
                    )
                    if (!isCollapsed) {
                        val textStyle =
                            if (isSelected) {
                                MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            } else {
                                MaterialTheme.typography.bodySmall
                            }

                        Text(
                            text = label,
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                        )
                    }
                }
            }
        }

    if (isCollapsed) {
        TooltipArea(
            delayMillis = 300,
            tooltip = {
                Card(
                    modifier = Modifier.border(1.dp, UserColor.getSplitterColor()),
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    )
                }
            },
            tooltipPlacement =
                TooltipPlacement.ComponentRect(
                    anchor = Alignment.CenterEnd,
                    alignment = Alignment.CenterEnd,
                    offset = DpOffset(4.dp, 0.dp),
                ),
        ) {
            content()
        }
    } else {
        content()
    }
}

@Preview
@Composable
private fun NavigationRailItemPreview() {
    NavigationRailItem(
        label = "Power",
        icon = Lucide.Power,
        contentDescription = null,
        isSelected = true,
        isCollapsed = false,
        onClick = {},
    )
}
