package jp.kaleidot725.adbpad.ui.screen.setting.state

import jp.kaleidot725.pulse.mvi.PulseEvent

sealed class SettingSideEffect : PulseEvent {
    data object Saved : SettingSideEffect()
}
