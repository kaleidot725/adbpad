package jp.kaleidot725.adbpad.ui.screen.timeedit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.card.CommonItemCard
import jp.kaleidot725.adbpad.ui.component.layout.ThreePaneLayout
import jp.kaleidot725.adbpad.ui.component.text.DefaultTextField
import jp.kaleidot725.adbpad.ui.screen.timeedit.component.TimeEditDetailPane
import jp.kaleidot725.adbpad.ui.screen.timeedit.component.TimeEditHeader
import jp.kaleidot725.adbpad.ui.screen.timeedit.component.TimeEditPreviewPane
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditAction
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditState
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.SplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun TimeEditScreen(
    state: TimeEditState,
    onAction: (TimeEditAction) -> Unit,
    splitterState: SplitPaneState,
    rightSplitterState: SplitPaneState,
) {
    ThreePaneLayout(
        splitterState = splitterState,
        rightSplitterState = rightSplitterState,
        left = {
            Column(modifier = Modifier.fillMaxSize()) {
                TimeEditHeader(
                    searchText = state.searchText,
                    sortType = state.sortType,
                    onUpdateSearchText = { onAction(TimeEditAction.UpdateSearchText(it)) },
                    onUpdateSortType = { onAction(TimeEditAction.UpdateSortType(it)) },
                    onAddItem = { onAction(TimeEditAction.AddNewItem) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                )

                HorizontalDivider(color = UserColor.getSplitterColor())

                if (state.filteredItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = Language.timeEditEmpty,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 24.dp),
                    ) {
                        items(state.filteredItems, key = { it.id }) { item ->
                            val dateTimeSummary = if (item.isAutoDateTime) Language.autoLabel else item.formattedDateTime
                            val timeZoneSummary = if (item.isAutoTimeZone) Language.autoLabel else item.timeZoneId
                            CommonItemCard(
                                title = item.title,
                                details = "$dateTimeSummary • $timeZoneSummary",
                                isSelected = state.selectedItem?.id == item.id,
                                onClick = { onAction(TimeEditAction.SelectItem(item.id)) },
                            )
                        }
                    }
                }
            }
        },
        center = {
            if (state.selectedItem != null) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                    ) {
                        DefaultTextField(
                            id = "time-edit-title-${state.selectedItemId}",
                            initialText = state.inputTitle,
                            onUpdateText = { onAction(TimeEditAction.UpdateTitle(it)) },
                            placeHolder = Language.timeEditTitleLabel,
                            errorMessage = if (state.isTitleValid) null else Language.timeEditInvalidTitle,
                            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                        )
                    }

                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = UserColor.getSplitterColor())

                    TimeEditPreviewPane(
                        state = state,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            } else {
                TimeEditPreviewPane(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        right = {
            TimeEditDetailPane(
                state = state,
                onAction = onAction,
                modifier = Modifier.fillMaxSize(),
            )
        },
    )
}
