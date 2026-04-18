package jp.kaleidot725.adbpad.ui.screen.timeedit.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.component.dropbox.EnumDropDown
import jp.kaleidot725.adbpad.ui.component.indicator.RunningIndicator
import jp.kaleidot725.adbpad.ui.component.layout.ExpandableSection
import jp.kaleidot725.adbpad.ui.component.text.DefaultOutlineTextField
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditAction
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeEditDetailPane(
    state: TimeEditState,
    onAction: (TimeEditAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val timeZoneOptions =
        remember {
            val currentInstant = Instant.now()
            ZoneId.getAvailableZoneIds()
                .map { zoneId ->
                    val zone = ZoneId.of(zoneId)
                    val offset = zone.rules.getOffset(currentInstant)
                    TimeZoneOption(
                        id = zoneId,
                        offsetTotalSeconds = offset.totalSeconds,
                        displayName = "${formatUtcOffset(offset)} · $zoneId",
                    )
                }.sortedWith(compareBy<TimeZoneOption> { it.offsetTotalSeconds }.thenBy { it.id })
        }
    var showDatePicker by remember(state.selectedItemId) { mutableStateOf(false) }
    var showTimePicker by remember(state.selectedItemId) { mutableStateOf(false) }

    if (state.selectedItem == null) {
        Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = Language.timeEditNoSelection,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis = state.inputDateValue?.toPickerMillis(),
            )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis
                            ?.let(::pickerMillisToLocalDate)
                            ?.format(DateTimeFormatter.ISO_LOCAL_DATE)
                            ?.let { onAction(TimeEditAction.UpdateDate(it)) }
                        showDatePicker = false
                    },
                    enabled = datePickerState.selectedDateMillis != null,
                ) {
                    Text(Language.save)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(Language.cancel)
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val initialTime = state.inputTimeValue ?: LocalTime.now().withSecond(0).withNano(0)
        val timePickerState =
            rememberTimePickerState(
                initialHour = initialTime.hour,
                initialMinute = initialTime.minute,
                is24Hour = true,
            )
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text(Language.timeEditTimeLabel) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(
                            TimeEditAction.UpdateTime(
                                LocalTime.of(timePickerState.hour, timePickerState.minute)
                                    .withSecond(0)
                                    .format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                            ),
                        )
                        showTimePicker = false
                    },
                ) {
                    Text(Language.save)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(Language.cancel)
                }
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ExpandableSection(title = Language.timeEditDateTimeSection) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Checkbox(
                        checked = state.inputAutoDateTime,
                        onCheckedChange = { onAction(TimeEditAction.UpdateAutoDateTime(it)) },
                    )
                    Text(
                        text = Language.timeEditAutoDateTimeLabel,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                DefaultOutlineTextField(
                    id = "time-edit-date-${state.selectedItemId}",
                    label = Language.timeEditDateLabel,
                    initialText = state.inputDate,
                    placeHolder = "2026-04-18",
                    isError = !state.inputAutoDateTime && !state.isDateValid,
                    onUpdateText = { onAction(TimeEditAction.UpdateDate(it)) },
                    enabled = !state.inputAutoDateTime,
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true },
                            enabled = !state.inputAutoDateTime,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = Language.timeEditDateLabel,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                if (!state.inputAutoDateTime && !state.isDateValid) {
                    Text(
                        text = Language.timeEditInvalidDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                DefaultOutlineTextField(
                    id = "time-edit-time-${state.selectedItemId}",
                    label = Language.timeEditTimeLabel,
                    initialText = state.inputTime,
                    placeHolder = "09:30:00",
                    isError = !state.inputAutoDateTime && !state.isTimeValid,
                    onUpdateText = { onAction(TimeEditAction.UpdateTime(it)) },
                    enabled = !state.inputAutoDateTime,
                    trailingIcon = {
                        IconButton(
                            onClick = { showTimePicker = true },
                            enabled = !state.inputAutoDateTime,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccessTime,
                                contentDescription = Language.timeEditTimeLabel,
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                if (!state.inputAutoDateTime && !state.isTimeValid) {
                    Text(
                        text = Language.timeEditInvalidTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            ExpandableSection(title = Language.timeEditTimeZoneSection) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Checkbox(
                        checked = state.inputAutoTimeZone,
                        onCheckedChange = { onAction(TimeEditAction.UpdateAutoTimeZone(it)) },
                    )
                    Text(
                        text = Language.timeEditAutoTimeZoneLabel,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                EnumDropDown(
                    selectedValue = timeZoneOptions.firstOrNull { it.id == state.inputTimeZone },
                    values = timeZoneOptions,
                    onValueSelected = { selectedTimeZone ->
                        if (selectedTimeZone != null) {
                            onAction(TimeEditAction.UpdateTimeZone(selectedTimeZone.id))
                        }
                    },
                    displayName = { it?.displayName ?: "--" },
                    label = Language.timeEditTimeZoneLabel,
                    enabled = !state.inputAutoTimeZone,
                    showNullOption = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                if (!state.inputAutoTimeZone && !state.isTimeZoneValid) {
                    Text(
                        text = Language.timeEditInvalidTimeZone,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }

        TimeEditActionButton(
            text = Language.execute,
            icon = Lucide.Play,
            onClick = { onAction(TimeEditAction.RunSelectedItem) },
            enabled = state.canRun,
            isRunning = state.isRunning,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        )
    }
}

private fun LocalDate.toPickerMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun pickerMillisToLocalDate(value: Long): LocalDate = Instant.ofEpochMilli(value).atOffset(ZoneOffset.UTC).toLocalDate()

private fun formatUtcOffset(offset: ZoneOffset): String {
    val totalSeconds = offset.totalSeconds
    val sign = if (totalSeconds >= 0) "+" else "-"
    val absoluteSeconds = kotlin.math.abs(totalSeconds)
    val hours = absoluteSeconds / 3600
    val minutes = (absoluteSeconds % 3600) / 60

    return "UTC%s%02d:%02d".format(sign, hours, minutes)
}

private data class TimeZoneOption(
    val id: String,
    val offsetTotalSeconds: Int,
    val displayName: String,
)

@Composable
private fun TimeEditActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    isRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isRunning,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier,
    ) {
        if (isRunning) {
            RunningIndicator(modifier = Modifier.size(16.dp))
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Text(text = text)
            }
        }
    }
}
