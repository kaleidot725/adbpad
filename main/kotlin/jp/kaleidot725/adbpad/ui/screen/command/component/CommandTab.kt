package jp.kaleidot725.adbpad.ui.screen.command.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Clock3
import com.composables.icons.lucide.Diamond
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.Navigation
import com.composables.icons.lucide.Palette
import com.composables.icons.lucide.Smartphone
import com.composables.icons.lucide.Wifi
import jp.kaleidot725.adbpad.domain.model.command.NormalCommandCategory
import jp.kaleidot725.adbpad.domain.model.language.Language

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommandTab(
    modifier: Modifier = Modifier,
    filtered: NormalCommandCategory,
    onClick: (NormalCommandCategory) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CommandTabItem(
            title = Language.commandCategoryAll,
            icon = Lucide.Diamond,
            isSelected = filtered == NormalCommandCategory.ALL,
            onClick = { onClick(NormalCommandCategory.ALL) },
        )

        CommandTabItem(
            title = Language.commandCategoryCommunication,
            icon = Lucide.Wifi,
            isSelected = filtered == NormalCommandCategory.COM,
            onClick = { onClick(NormalCommandCategory.COM) },
        )

        CommandTabItem(
            title = Language.commandCategoryNavigation,
            icon = Lucide.Navigation,
            isSelected = filtered == NormalCommandCategory.NAVIGATION,
            onClick = { onClick(NormalCommandCategory.NAVIGATION) },
        )

        CommandTabItem(
            title = Language.commandCategoryTheme,
            icon = Lucide.Palette,
            isSelected = filtered == NormalCommandCategory.THEME,
            onClick = { onClick(NormalCommandCategory.THEME) },
        )

        CommandTabItem(
            title = Language.commandCategoryDisplay,
            icon = Lucide.Monitor,
            isSelected = filtered == NormalCommandCategory.DISPLAY,
            onClick = { onClick(NormalCommandCategory.DISPLAY) },
        )

        CommandTabItem(
            title = Language.commandCategoryDevice,
            icon = Lucide.Smartphone,
            isSelected = filtered == NormalCommandCategory.DEVICE,
            onClick = { onClick(NormalCommandCategory.DEVICE) },
        )

        CommandTabItem(
            title = Language.commandCategoryTime,
            icon = Lucide.Clock3,
            isSelected = filtered == NormalCommandCategory.TIME,
            onClick = { onClick(NormalCommandCategory.TIME) },
        )

        CommandTabItem(
            title = Language.commandCategoryFavorite,
            icon = Icons.Default.Favorite,
            isSelected = filtered == NormalCommandCategory.FAVORITE,
            onClick = { onClick(NormalCommandCategory.FAVORITE) },
        )
    }
}

@Preview
@Composable
private fun CommandTabPreview() {
    CommandTab(filtered = NormalCommandCategory.ALL) {}
}
