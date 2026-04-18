package jp.kaleidot725.adbpad.ui.screen.timeedit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditState
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Composable
fun TimeEditPreviewPane(
    state: TimeEditState,
    modifier: Modifier = Modifier,
) {
    val currentTime by produceState<ZonedDateTime?>(initialValue = ZonedDateTime.now(state.previewZoneId), key1 = state.previewZoneId) {
        while (true) {
            value = ZonedDateTime.now(state.previewZoneId)
            delay(1_000)
        }
    }

    if (state.selectedItem == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = Language.timeEditNoSelection,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }

    val currentTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val configuredDateTimeText =
        if (state.inputAutoDateTime) {
            Language.autoLabel
        } else {
            listOfNotNull(
                state.inputDate.takeIf { it.isNotBlank() },
                state.inputTime.takeIf { it.isNotBlank() },
            ).joinToString(" ").ifBlank { "--" }
        }
    val configuredTimeZoneText =
        if (state.inputAutoTimeZone) {
            Language.autoLabel
        } else {
            state.inputTimeZone.ifBlank { "--" }
        }
    val configuredDateTime =
        if (state.inputAutoDateTime) {
            null
        } else {
            val date = state.inputDateValue
            val time = state.inputTimeValue
            if (date != null && time != null) {
                ZonedDateTime.of(date, time, state.previewZoneId)
            } else {
                null
            }
        }
    val timeDifferenceText =
        if (state.inputAutoDateTime) {
            Language.autoLabel
        } else {
            val previewCurrentTime = currentTime
            if (configuredDateTime != null && previewCurrentTime != null) {
                formatTimeDifference(Duration.between(previewCurrentTime, configuredDateTime))
            } else {
                "--"
            }
        }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = Language.timeEditConfiguredTime,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = configuredDateTimeText,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Text(
                text = Language.timeEditCurrentTime,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = currentTime?.format(currentTimeFormatter) ?: "--",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Text(
                text = Language.timeEditTimeDifference,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = timeDifferenceText,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            Text(
                text = Language.timeEditTimeZoneLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = configuredTimeZoneText,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun formatTimeDifference(duration: Duration): String {
    val totalSeconds = duration.seconds
    val sign = if (totalSeconds >= 0) "+" else "-"
    val absoluteSeconds = totalSeconds.absoluteValue
    val hours = absoluteSeconds / 3600
    val minutes = (absoluteSeconds % 3600) / 60
    val seconds = absoluteSeconds % 60

    return "%s%02d:%02d:%02d".format(sign, hours, minutes, seconds)
}
