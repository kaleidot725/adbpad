package jp.kaleidot725.adbpad.ui.screen.timeedit

import jp.kaleidot725.adbpad.domain.model.language.Language
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem
import jp.kaleidot725.adbpad.domain.repository.TimeEditItemRepository
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.time.ExecuteTimeEditUseCase
import jp.kaleidot725.adbpad.domain.usecase.time.GetTimeEditItemsUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditAction
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditSideEffect
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.TimeEditState
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.filterTimeEditItems
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.normalizeTimeInput
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.parseLocalDate
import jp.kaleidot725.adbpad.ui.screen.timeedit.state.parseLocalTime
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeEditStateHolder(
    private val timeEditItemRepository: TimeEditItemRepository,
    private val getTimeEditItemsUseCase: GetTimeEditItemsUseCase,
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val executeTimeEditUseCase: ExecuteTimeEditUseCase,
) : PulseStore<TimeEditState, TimeEditAction, TimeEditSideEffect, AppBroadCast>(initialUiState = TimeEditState()) {
    override fun onSetup() {
        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collect { device ->
                update { copy(selectedDevice = device) }
            }
        }

        coroutineScope.launch {
            refreshItems()
        }
    }

    override fun onAction(uiAction: TimeEditAction) {
        coroutineScope.launch {
            when (uiAction) {
                TimeEditAction.AddNewItem -> addNewItem()
                TimeEditAction.RunSelectedItem -> runSelectedItem()
                is TimeEditAction.SelectItem -> selectItem(uiAction.itemId)
                is TimeEditAction.UpdateSearchText -> updateSearchText(uiAction.text)
                is TimeEditAction.UpdateSortType -> updateSortType(uiAction.sortType)
                is TimeEditAction.UpdateTitle -> updateTitle(uiAction.text)
                is TimeEditAction.UpdateDate -> updateDate(uiAction.text)
                is TimeEditAction.UpdateTime -> updateTime(uiAction.text)
                is TimeEditAction.UpdateAutoDateTime -> updateAutoDateTime(uiAction.enabled)
                is TimeEditAction.UpdateTimeZone -> updateTimeZone(uiAction.text)
                is TimeEditAction.UpdateAutoTimeZone -> updateAutoTimeZone(uiAction.enabled)
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {
                coroutineScope.launch {
                    refreshItems()
                }
            }
        }
    }

    private suspend fun refreshItems(forceSelectedItemId: String? = null) {
        val items = getTimeEditItemsUseCase()
        update {
            val nextSearchText = if (forceSelectedItemId != null) "" else searchText
            val filteredItems = filterTimeEditItems(items, nextSearchText, sortType)
            val nextSelectedItemId =
                when {
                    forceSelectedItemId != null && filteredItems.any { it.id == forceSelectedItemId } -> forceSelectedItemId
                    filteredItems.any { it.id == selectedItemId } -> selectedItemId
                    else -> filteredItems.firstOrNull()?.id
                }
            val nextSelectedItem = items.firstOrNull { it.id == nextSelectedItemId }
            val shouldSyncInputs =
                forceSelectedItemId != null || nextSelectedItemId != selectedItemId || filteredItems.isEmpty()

            copy(
                items = items,
                searchText = nextSearchText,
                selectedItemId = nextSelectedItemId,
                inputTitle = if (shouldSyncInputs) nextSelectedItem?.title.orEmpty() else inputTitle,
                inputDate = if (shouldSyncInputs) nextSelectedItem?.date.orEmpty() else inputDate,
                inputTime = if (shouldSyncInputs) nextSelectedItem?.time.orEmpty() else inputTime,
                inputAutoDateTime = if (shouldSyncInputs) nextSelectedItem?.isAutoDateTime ?: false else inputAutoDateTime,
                inputTimeZone = if (shouldSyncInputs) nextSelectedItem?.timeZoneId.orEmpty() else inputTimeZone,
                inputAutoTimeZone = if (shouldSyncInputs) nextSelectedItem?.isAutoTimeZone ?: false else inputAutoTimeZone,
            )
        }
    }

    private fun applyFilter(
        searchText: String = currentState.searchText,
        sortType: SortType = currentState.sortType,
    ) {
        update {
            val filteredItems = filterTimeEditItems(items, searchText, sortType)
            val nextSelectedItemId =
                when {
                    filteredItems.any { it.id == selectedItemId } -> selectedItemId
                    else -> filteredItems.firstOrNull()?.id
                }
            val nextSelectedItem = items.firstOrNull { it.id == nextSelectedItemId }
            val shouldSyncInputs = nextSelectedItemId != selectedItemId || filteredItems.isEmpty()

            copy(
                searchText = searchText,
                sortType = sortType,
                selectedItemId = nextSelectedItemId,
                inputTitle = if (shouldSyncInputs) nextSelectedItem?.title.orEmpty() else inputTitle,
                inputDate = if (shouldSyncInputs) nextSelectedItem?.date.orEmpty() else inputDate,
                inputTime = if (shouldSyncInputs) nextSelectedItem?.time.orEmpty() else inputTime,
                inputAutoDateTime = if (shouldSyncInputs) nextSelectedItem?.isAutoDateTime ?: false else inputAutoDateTime,
                inputTimeZone = if (shouldSyncInputs) nextSelectedItem?.timeZoneId.orEmpty() else inputTimeZone,
                inputAutoTimeZone = if (shouldSyncInputs) nextSelectedItem?.isAutoTimeZone ?: false else inputAutoTimeZone,
            )
        }
    }

    private fun selectItem(itemId: String) {
        update {
            val selectedItem = items.firstOrNull { it.id == itemId }
            copy(
                selectedItemId = selectedItem?.id,
                inputTitle = selectedItem?.title.orEmpty(),
                inputDate = selectedItem?.date.orEmpty(),
                inputTime = selectedItem?.time.orEmpty(),
                inputAutoDateTime = selectedItem?.isAutoDateTime ?: false,
                inputTimeZone = selectedItem?.timeZoneId.orEmpty(),
                inputAutoTimeZone = selectedItem?.isAutoTimeZone ?: false,
            )
        }
    }

    private suspend fun addNewItem() {
        val defaultZoneId = ZoneId.systemDefault()
        val now = ZonedDateTime.now(defaultZoneId)
        val item =
            TimeEditItem(
                title = Language.timeEditDefaultTitle,
                date = now.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                time = now.toLocalTime().withNano(0).format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                timeZoneId = defaultZoneId.id,
            )

        timeEditItemRepository.addTimeEditItem(item)
        refreshItems(forceSelectedItemId = item.id)
    }

    private fun updateSearchText(text: String) {
        applyFilter(searchText = text)
    }

    private fun updateSortType(sortType: SortType) {
        applyFilter(sortType = sortType)
    }

    private suspend fun updateTitle(text: String) {
        update { copy(inputTitle = text) }
        saveCurrentItem(title = text)
    }

    private suspend fun updateDate(text: String) {
        update { copy(inputDate = text) }
        saveCurrentItem(date = text)
    }

    private suspend fun updateTime(text: String) {
        update { copy(inputTime = text) }
        saveCurrentItem(time = text)
    }

    private suspend fun updateAutoDateTime(enabled: Boolean) {
        update { copy(inputAutoDateTime = enabled) }
        saveCurrentItem(isAutoDateTime = enabled)
    }

    private suspend fun updateTimeZone(text: String) {
        update { copy(inputTimeZone = text) }
        saveCurrentItem(timeZoneId = text)
    }

    private suspend fun updateAutoTimeZone(enabled: Boolean) {
        update { copy(inputAutoTimeZone = enabled) }
        saveCurrentItem(isAutoTimeZone = enabled)
    }

    private suspend fun runSelectedItem() {
        val device = currentState.selectedDevice ?: return
        val item = buildCurrentItem() ?: return

        timeEditItemRepository.updateTimeEditItem(item)
        refreshItems(forceSelectedItemId = item.id)

        executeTimeEditUseCase(
            device = device,
            item = item,
            onStart = { update { copy(isRunning = true) } },
            onComplete = { update { copy(isRunning = false) } },
            onFailed = { update { copy(isRunning = false) } },
        )
    }

    private suspend fun saveCurrentItem(
        title: String = currentState.inputTitle,
        date: String = currentState.inputDate,
        time: String = currentState.inputTime,
        isAutoDateTime: Boolean = currentState.inputAutoDateTime,
        timeZoneId: String = currentState.inputTimeZone,
        isAutoTimeZone: Boolean = currentState.inputAutoTimeZone,
    ) {
        val updatedItem =
            buildCurrentItem(
                title = title,
                date = date,
                time = time,
                isAutoDateTime = isAutoDateTime,
                timeZoneId = timeZoneId,
                isAutoTimeZone = isAutoTimeZone,
            ) ?: return

        update {
            copy(
                inputTitle = updatedItem.title,
                inputDate = updatedItem.date,
                inputTime = updatedItem.time,
                inputAutoDateTime = updatedItem.isAutoDateTime,
                inputTimeZone = updatedItem.timeZoneId,
                inputAutoTimeZone = updatedItem.isAutoTimeZone,
            )
        }

        timeEditItemRepository.updateTimeEditItem(updatedItem)
        refreshItems(forceSelectedItemId = updatedItem.id)
    }

    private fun buildCurrentItem(
        title: String = currentState.inputTitle,
        date: String = currentState.inputDate,
        time: String = currentState.inputTime,
        isAutoDateTime: Boolean = currentState.inputAutoDateTime,
        timeZoneId: String = currentState.inputTimeZone,
        isAutoTimeZone: Boolean = currentState.inputAutoTimeZone,
    ): TimeEditItem? {
        val selectedItem = currentState.selectedItem ?: return null
        val normalizedTitle = title.trim()
        val normalizedDate = date.trim()
        val normalizedTime = time.trim()
        val normalizedTimeZoneId = timeZoneId.trim()

        if (normalizedTitle.isBlank()) return null

        val parsedDate = parseLocalDate(normalizedDate)
        val parsedTime = parseLocalTime(normalizedTime)
        val parsedZoneId = runCatching { ZoneId.of(normalizedTimeZoneId) }.getOrNull()

        if (!isAutoDateTime && (parsedDate == null || parsedTime == null)) return null
        if (!isAutoTimeZone && parsedZoneId == null) return null

        return selectedItem.copy(
            title = normalizedTitle,
            date = parsedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: selectedItem.date,
            time = parsedTime?.let(::normalizeTimeInput) ?: selectedItem.time,
            isAutoDateTime = isAutoDateTime,
            timeZoneId = parsedZoneId?.id ?: selectedItem.timeZoneId,
            isAutoTimeZone = isAutoTimeZone,
        )
    }
}
