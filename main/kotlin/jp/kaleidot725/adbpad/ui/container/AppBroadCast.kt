package jp.kaleidot725.adbpad.ui.container

import jp.kaleidot725.pulse.mvi.PulseBroadcast
import jp.kaleidot725.pulse.mvi.PulseUnicast

sealed interface AppBroadCast : PulseBroadcast {
    data object Refresh : AppBroadCast
}

sealed interface AppUnicast : PulseUnicast
