package jp.kaleidot725.adbpad.ui.container

import jp.kaleidot725.pulse.mvi.PulseContainer
import jp.kaleidot725.pulse.mvi.PulseStore

class AppContainer(
    stores: List<PulseStore<*, *, *, AppBroadCast>>,
) : PulseContainer<AppBroadCast>(stores = stores)
