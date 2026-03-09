package jp.kaleidot725.adbpad.ui.container

import jp.kaleidot725.pulse.mvi.PulseBroadcast

sealed interface AppBroadCast : PulseBroadcast {
    data object Refresh : AppBroadCast
}
