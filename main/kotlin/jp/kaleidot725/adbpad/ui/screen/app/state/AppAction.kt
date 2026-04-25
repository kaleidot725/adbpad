package jp.kaleidot725.adbpad.ui.screen.app.state

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.pulse.mvi.PulseAction

sealed class AppAction : PulseAction {
    data object RefreshApps : AppAction()

    data class UpdateSearchText(
        val text: String,
    ) : AppAction()

    data class UpdateSortType(
        val sortType: SortType,
    ) : AppAction()

    data class SelectApp(
        val app: InstalledApp,
    ) : AppAction()

    data object SelectNextApp : AppAction()

    data object SelectPreviousApp : AppAction()
}
