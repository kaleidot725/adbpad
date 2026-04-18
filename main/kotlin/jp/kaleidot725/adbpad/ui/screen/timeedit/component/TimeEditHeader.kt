package jp.kaleidot725.adbpad.ui.screen.timeedit.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground
import jp.kaleidot725.adbpad.ui.component.dropbox.SearchSortDropBox
import jp.kaleidot725.adbpad.ui.component.text.DefaultTextField

@Composable
fun TimeEditHeader(
    searchText: String,
    sortType: SortType,
    onUpdateSearchText: (String) -> Unit,
    onUpdateSortType: (SortType) -> Unit,
    onAddItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SearchSortDropBox(
            selectedSortType = sortType,
            onSelectType = onUpdateSortType,
        )

        DefaultTextField(
            initialText = searchText,
            onUpdateText = onUpdateSearchText,
            placeHolder = Language.search,
            modifier = Modifier.weight(1f),
        )

        Box(
            modifier =
                Modifier
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickableBackground(isDarker = MaterialTheme.colorScheme.surface.luminance() <= 0.5f)
                    .clickable(onClick = onAddItem),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Lucide.Plus,
                contentDescription = "Add time setting",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
