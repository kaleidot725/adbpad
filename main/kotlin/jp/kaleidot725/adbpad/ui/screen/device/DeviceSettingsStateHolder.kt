package jp.kaleidot725.adbpad.ui.screen.device

import jp.kaleidot725.adbpad.domain.model.device.DeviceSettings
import jp.kaleidot725.adbpad.domain.repository.DeviceSettingsRepository
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.device.model.DeviceSettingCategory
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsAction
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsSideEffect
import jp.kaleidot725.adbpad.ui.screen.device.state.DeviceSettingsState
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DeviceSettingsStateHolder(
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val deviceSettingsRepository: DeviceSettingsRepository,
) : PulseStore<DeviceSettingsState, DeviceSettingsAction, DeviceSettingsSideEffect, AppBroadCast>(
        initialUiState = DeviceSettingsState(),
    ) {
    override fun onSetup() {
        initialize()
    }

    override fun onAction(uiAction: DeviceSettingsAction) {
        when (uiAction) {
            is DeviceSettingsAction.SelectCategory -> selectCategory(uiAction.category)
            is DeviceSettingsAction.UpdateSettings -> updateSettings(uiAction.settings)
            is DeviceSettingsAction.Save -> saveSettings()
            is DeviceSettingsAction.Cancel -> dismiss()
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> initialize()
        }
    }

    private fun initialize() {
        coroutineScope.launch {
            val selected = getSelectedDeviceFlowUseCase().firstOrNull() ?: return@launch
            val deviceSettings = deviceSettingsRepository.getDeviceSettings(selected)
            update {
                copy(
                    device = selected,
                    deviceSettings = deviceSettings,
                    isLoaded = true,
                )
            }
        }
    }

    private fun updateSettings(settings: DeviceSettings) {
        update { copy(deviceSettings = settings) }
    }

    private fun saveSettings() {
        val currentState = state.value
        if (currentState.device == null || currentState.deviceSettings == null) return

        coroutineScope.launch {
            update { copy(isSaving = true) }

            val success = deviceSettingsRepository.saveDeviceSettings(currentState.device, currentState.deviceSettings)
            if (success) {
                event(DeviceSettingsSideEffect.Saved)
            }

            update { copy(isSaving = false) }
        }
    }

    private fun dismiss() {
        event(DeviceSettingsSideEffect.Cancelled)
    }

    private fun selectCategory(category: DeviceSettingCategory) {
        update { this.copy(selectedCategory = category) }
    }
}
