package jp.kaleidot725.adbpad.ui.component.rail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Camera
import com.composables.icons.lucide.ChevronsRight
import com.composables.icons.lucide.File
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MonitorSmartphone
import com.composables.icons.lucide.Settings
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.MainCategory

@Composable
fun NavigationRail(
    category: MainCategory,
    isCollapsed: Boolean,
    onSelectCategory: (MainCategory) -> Unit,
    onOpenSetting: () -> Unit,
) {
    val width by animateDpAsState(if (isCollapsed) 40.dp else 220.dp)

    Column(
        modifier = Modifier.fillMaxHeight().padding(8.dp).width(width),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        NavigationRailItem(
            label = Language.tooltipCommand,
            icon = Lucide.ChevronsRight,
            contentDescription = "device menu",
            isSelected = category == MainCategory.Command,
            isCollapsed = isCollapsed,
            onClick = { onSelectCategory(MainCategory.Command) },
        )

        NavigationRailItem(
            label = Language.tooltipText,
            icon = Lucide.File,
            contentDescription = "text menu",
            isSelected = category == MainCategory.Text,
            isCollapsed = isCollapsed,
            onClick = { onSelectCategory(MainCategory.Text) },
        )

        NavigationRailItem(
            label = Language.tooltipScreenshot,
            icon = Lucide.Camera,
            contentDescription = "screenshot menu",
            isSelected = category == MainCategory.Screenshot,
            isCollapsed = isCollapsed,
            onClick = { onSelectCategory(MainCategory.Screenshot) },
        )

        NavigationRailItem(
            label = Language.tooltipNewDisplay,
            icon = Lucide.MonitorSmartphone,
            contentDescription = "virtual display menu",
            isSelected = category == MainCategory.ScrcpyNewDisplay,
            isCollapsed = isCollapsed,
            onClick = { onSelectCategory(MainCategory.ScrcpyNewDisplay) },
        )

        Spacer(modifier = Modifier.weight(1f, fill = true))

        NavigationRailItem(
            label = Language.tooltipSetting,
            icon = Lucide.Settings,
            contentDescription = "app setting",
            isSelected = false,
            isCollapsed = isCollapsed,
            onClick = onOpenSetting,
        )
    }
}

@Preview
@Composable
private fun NavigationRail_Preview() {
    NavigationRail(
        category = MainCategory.Text,
        isCollapsed = false,
        onSelectCategory = {},
        onOpenSetting = {},
    )
}
