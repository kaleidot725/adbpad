package jp.kaleidot725.adbpad.ui.screen.timeedit.state

import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.pulse.mvi.PulseAction

sealed class TimeEditAction : PulseAction {
    data class SelectItem(
        val itemId: String,
    ) : TimeEditAction()

    data class UpdateSearchText(
        val text: String,
    ) : TimeEditAction()

    data class UpdateSortType(
        val sortType: SortType,
    ) : TimeEditAction()

    data class UpdateTitle(
        val text: String,
    ) : TimeEditAction()

    data class UpdateDate(
        val text: String,
    ) : TimeEditAction()

    data class UpdateTime(
        val text: String,
    ) : TimeEditAction()

    data class UpdateAutoDateTime(
        val enabled: Boolean,
    ) : TimeEditAction()

    data class UpdateTimeZone(
        val text: String,
    ) : TimeEditAction()

    data class UpdateAutoTimeZone(
        val enabled: Boolean,
    ) : TimeEditAction()

    data object AddNewItem : TimeEditAction()

    data object RunSelectedItem : TimeEditAction()
}
