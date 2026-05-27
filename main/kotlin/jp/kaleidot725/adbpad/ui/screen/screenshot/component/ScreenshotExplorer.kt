package jp.kaleidot725.adbpad.ui.screen.screenshot.component

import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.screenshot.Screenshot
import jp.kaleidot725.adbpad.ui.component.card.CommonItemCard
import jp.kaleidot725.adbpad.ui.component.menu.ThemedContextMenuArea
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScreenshotExplorer(
    selectedScreenshot: Screenshot,
    screenshots: List<Screenshot>,
    onSelectScreenShot: (Screenshot) -> Unit,
    onDeleteScreenshot: (Screenshot) -> Unit,
    onNextScreenshot: () -> Unit,
    onPreviousScreenshot: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()) }

    Box(modifier) {
        if (screenshots.isNotEmpty()) {
            val lazyColumnState = rememberLazyListState()
            LazyColumn(
                state = lazyColumnState,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onKeyEvent { event ->
                            when {
                                event.key == Key.DirectionUp && event.type == KeyEventType.KeyDown -> {
                                    onPreviousScreenshot()
                                    true
                                }

                                event.key == Key.DirectionDown && event.type == KeyEventType.KeyDown -> {
                                    onNextScreenshot()
                                    true
                                }

                                else -> false
                            }
                        },
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding =
                    PaddingValues(
                        start = 8.dp,
                        top = 4.dp,
                        end = 8.dp,
                        bottom = 24.dp,
                    ),
            ) {
                items(
                    items = screenshots,
                    key = { screenshot -> screenshot.file?.absolutePath ?: "" },
                ) { screenshot ->
                    ThemedContextMenuArea(
                        items = {
                            listOf(
                                ContextMenuItem(
                                    label = Language.delete,
                                    onClick = { onDeleteScreenshot(screenshot) },
                                ),
                            )
                        },
                    ) {
                        CommonItemCard(
                            title = screenshot.file?.name ?: "",
                            details = screenshot.file?.lastModified()?.let { dateFormat.format(Date(it)) },
                            isSelected = selectedScreenshot == screenshot,
                            onClick = { onSelectScreenShot(screenshot) },
                        )
                    }
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).width(8.dp).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = lazyColumnState),
            )
        } else {
            Text(
                text = Language.notFoundScreenshot,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
