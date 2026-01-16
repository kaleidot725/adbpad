package jp.kaleidot725.adbpad.ui.screen.newdisplay

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.device.DeviceState
import jp.kaleidot725.adbpad.domain.model.device.ScrcpyOptions
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile
import jp.kaleidot725.adbpad.ui.common.resource.UserColor
import jp.kaleidot725.adbpad.ui.component.menu.ThemedContextMenuArea
import jp.kaleidot725.adbpad.ui.screen.newdisplay.component.ProfileListItem
import jp.kaleidot725.adbpad.ui.screen.newdisplay.component.ScrcpyNewDisplayDetailPanel
import jp.kaleidot725.adbpad.ui.screen.newdisplay.component.ScrcpyNewDisplayHeader
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplayAction
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplayState
import jp.kaleidot725.adbpad.ui.screen.screenshot.cursorForHorizontalResize
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import kotlin.math.max

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun ScrcpyNewDisplayScreen(
    state: ScrcpyNewDisplayState,
    onAction: (ScrcpyNewDisplayAction) -> Unit,
    splitterState: SplitPaneState,
) {
    val filteredProfiles = state.filteredProfiles
    val maxProfileDimension =
        remember(state.profiles) {
            state.profiles.maxOfOrNull { max(it.width, it.height) }?.toFloat() ?: 1f
        }

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    HorizontalSplitPane(
        splitPaneState = splitterState,
        modifier = Modifier.fillMaxSize(),
    ) {
        first(minSize = 200.dp) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    ScrcpyNewDisplayHeader(
                        searchText = state.searchText,
                        sortType = state.sortType,
                        onUpdateSearchText = { onAction(ScrcpyNewDisplayAction.UpdateSearchText(it)) },
                        onUpdateSortType = { onAction(ScrcpyNewDisplayAction.UpdateSortType(it)) },
                        onAddProfile = { onAction(ScrcpyNewDisplayAction.AddNewProfile) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                    )

                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = UserColor.getSplitterColor())

                    if (filteredProfiles.isEmpty()) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                        ) {
                            Text(
                                text = Language.scrcpyNewDisplayEmpty,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .onKeyEvent { event ->
                                        when {
                                            event.key == Key.DirectionUp && event.type == KeyEventType.KeyDown -> {
                                                onAction(ScrcpyNewDisplayAction.SelectPreviousProfile)
                                                true
                                            }
                                            event.key == Key.DirectionDown && event.type == KeyEventType.KeyDown -> {
                                                onAction(ScrcpyNewDisplayAction.SelectNextProfile)
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
                            items(filteredProfiles, key = { it.id }) { profile ->
                                val selected = state.selectedProfile?.id == profile.id
                                ThemedContextMenuArea(
                                    items = {
                                        listOf(
                                            ContextMenuItem(
                                                label = Language.delete,
                                                onClick = { onAction(ScrcpyNewDisplayAction.DeleteProfile(profile)) },
                                            ),
                                        )
                                    },
                                ) {
                                    ProfileListItem(
                                        name = profile.displayName,
                                        lastModified = profile.lastModified,
                                        selected = selected,
                                        onClick = { onAction(ScrcpyNewDisplayAction.SelectProfile(profile.id)) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        second {
            ScrcpyNewDisplayDetailPanel(
                selectedProfile = state.selectedProfile,
                isEditable = true,
                selectedProfileId = state.selectedProfileId,
                inputName = state.inputName,
                inputWidth = state.inputWidth,
                inputHeight = state.inputHeight,
                inputDpi = state.inputDpi,
                scrcpyOptions = state.selectedProfile?.options ?: ScrcpyOptions(),
                isLaunching = state.isLaunching,
                canLaunch = state.canLaunch,
                maxProfileDimension = maxProfileDimension,
                onUpdateInputName = { onAction(ScrcpyNewDisplayAction.UpdateInputName(it)) },
                onUpdateInputWidth = { onAction(ScrcpyNewDisplayAction.UpdateInputWidth(it)) },
                onUpdateInputHeight = { onAction(ScrcpyNewDisplayAction.UpdateInputHeight(it)) },
                onUpdateInputDpi = { onAction(ScrcpyNewDisplayAction.UpdateInputDpi(it)) },
                onUpdateScrcpyOptions = { onAction(ScrcpyNewDisplayAction.UpdateScrcpyOptions(it)) },
                onLaunch = { onAction(ScrcpyNewDisplayAction.LaunchSelectedProfile) },
            )
        }

        splitter {
            visiblePart {
                Box(
                    Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(
                            if (isHovered) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            } else {
                                UserColor.getSplitterColor()
                            },
                        ),
                )
            }

            handle {
                Box(
                    Modifier
                        .markAsHandle()
                        .cursorForHorizontalResize()
                        .hoverable(interactionSource)
                        .width(10.dp)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@OptIn(ExperimentalSplitPaneApi::class)
@Preview
@Composable
private fun ScrcpyNewDisplayScreenPreview() {
    val sampleProfiles =
        remember {
            listOf(
                ScrcpyNewDisplayProfile(
                    id = "sample",
                    displayName = "Sample Profile",
                    width = 1080,
                    height = 1920,
                    densityDpi = 440,
                    refreshRateHz = 60,
                ),
            )
        }
    val state by remember {
        mutableStateOf(
            ScrcpyNewDisplayState(
                profiles = sampleProfiles,
                selectedProfileId = sampleProfiles.first().id,
                selectedDevice = Device(serial = "X1", name = "Pixel 8", state = DeviceState.UNKNOWN),
            ),
        )
    }
    ScrcpyNewDisplayScreen(
        state = state,
        onAction = {},
        splitterState = rememberSplitPaneState(initialPositionPercentage = 0.3f),
    )
}
