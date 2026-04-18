package jp.kaleidot725.adbpad.ui.screen.timeedit.state

import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem
import jp.kaleidot725.pulse.mvi.PulseEvent
import jp.kaleidot725.pulse.mvi.PulseState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class TimeEditState(
    val items: List<TimeEditItem> = emptyList(),
    val selectedItemId: String? = null,
    val selectedDevice: Device? = null,
    val searchText: String = "",
    val sortType: SortType = SortType.SORT_BY_NAME_ASC,
    val inputTitle: String = "",
    val inputDate: String = "",
    val inputTime: String = "",
    val inputAutoDateTime: Boolean = false,
    val inputTimeZone: String = "",
    val inputAutoTimeZone: Boolean = false,
    val isRunning: Boolean = false,
) : PulseState {
    val filteredItems: List<TimeEditItem>
        get() = filterTimeEditItems(items, searchText, sortType)

    val selectedItem: TimeEditItem?
        get() = filteredItems.firstOrNull { it.id == selectedItemId } ?: filteredItems.firstOrNull()

    val inputDateValue: LocalDate?
        get() = parseLocalDate(inputDate)

    val inputTimeValue: LocalTime?
        get() = parseLocalTime(inputTime)

    val manualInputZoneId: ZoneId?
        get() = runCatching { ZoneId.of(inputTimeZone.trim()) }.getOrNull()

    val previewZoneId: ZoneId
        get() = if (inputAutoTimeZone) ZoneId.systemDefault() else (manualInputZoneId ?: ZoneId.systemDefault())

    val isTitleValid: Boolean
        get() = inputTitle.trim().isNotEmpty()

    val isDateValid: Boolean
        get() = inputAutoDateTime || inputDateValue != null

    val isTimeValid: Boolean
        get() = inputAutoDateTime || inputTimeValue != null

    val isTimeZoneValid: Boolean
        get() = inputAutoTimeZone || manualInputZoneId != null

    val canRun: Boolean
        get() = selectedItem != null && selectedDevice != null && isTitleValid && isDateValid && isTimeValid && isTimeZoneValid && !isRunning
}

class TimeEditSideEffect : PulseEvent

internal fun filterTimeEditItems(
    items: List<TimeEditItem>,
    query: String,
    sortType: SortType,
): List<TimeEditItem> {
    val normalized = query.trim().lowercase(Locale.getDefault())
    if (normalized.isBlank()) {
        return items.sortedWith(sortType)
    }

    return items
        .filter {
            it.title.contains(normalized, ignoreCase = true) ||
                it.timeZoneId.contains(normalized, ignoreCase = true) ||
                it.date.contains(normalized, ignoreCase = true) ||
                it.time.contains(normalized, ignoreCase = true)
        }.sortedWith(sortType)
}

internal fun parseLocalDate(value: String): LocalDate? =
    runCatching { LocalDate.parse(value.trim(), DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull()

internal fun parseLocalTime(value: String): LocalTime? =
    listOf(
        DateTimeFormatter.ofPattern("HH:mm:ss"),
        DateTimeFormatter.ofPattern("HH:mm"),
    ).firstNotNullOfOrNull { formatter ->
        runCatching { LocalTime.parse(value.trim(), formatter) }.getOrNull()
    }?.withNano(0)

internal fun normalizeTimeInput(value: LocalTime): String = value.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

private fun List<TimeEditItem>.sortedWith(sortType: SortType): List<TimeEditItem> =
    when (sortType) {
        SortType.SORT_BY_NAME_ASC -> sortedBy { it.title.lowercase(Locale.getDefault()) }
        SortType.SORT_BY_NAME_DESC -> sortedByDescending { it.title.lowercase(Locale.getDefault()) }
    }
