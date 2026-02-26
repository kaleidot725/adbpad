package jp.kaleidot725.adbpad.ui.component.dropbox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.common.resource.clickableBackground

@Composable
fun SearchSortDropBox(
    selectedSortType: SortType,
    onSelectType: (SortType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        Box(
            modifier =
                Modifier
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickableBackground(isDarker = MaterialTheme.colorScheme.surface.luminance() <= 0.5)
                    .clickable {
                        expanded = true
                    }.width(40.dp),
        ) {
            Icon(
                imageVector = Lucide.Search,
                contentDescription = null,
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = (-6).dp)
                        .padding(vertical = 8.dp)
                        .size(24.dp)
                        .padding(2.dp),
            )

            Icon(
                imageVector = Lucide.ChevronDown,
                contentDescription = "Device DropDown Icon",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = 12.dp)
                        .size(24.dp)
                        .padding(4.dp),
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier =
            Modifier
                .background(
                    color = UserColor.getDropdownBackgroundColor(),
                    shape = RoundedCornerShape(4.dp),
                ).border(
                    width = 1.dp,
                    color = UserColor.getSplitterColor(),
                    shape = RoundedCornerShape(4.dp),
                ),
    ) {
        SortType.entries.forEach { sortType ->
            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterVertically),
                        ) {
                            if (selectedSortType == sortType) {
                                Icon(
                                    imageVector = Lucide.Check,
                                    contentDescription = "Check",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.align(Alignment.Center).size(20.dp),
                                )
                            }
                        }

                        Text(
                            text =
                                when (sortType) {
                                    SortType.SORT_BY_NAME_ASC -> Language.sortByNameAsc
                                    SortType.SORT_BY_NAME_DESC -> Language.sortByNameDesc
                                },
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.align(Alignment.CenterVertically).padding(bottom = 4.dp),
                        )
                    }
                },
                onClick = {
                    expanded = false
                    onSelectType(sortType)
                },
            )
        }
    }
}
