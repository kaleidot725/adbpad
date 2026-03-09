package jp.kaleidot725.adbpad.ui.screen.newdisplay

import jp.kaleidot725.adbpad.domain.model.device.ScrcpyOptions
import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile
import jp.kaleidot725.adbpad.domain.model.sort.SortType
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.DeleteScrcpyNewDisplayProfileUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.GetScrcpyNewDisplayProfilesUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.LaunchScrcpyNewDisplayUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.SaveScrcpyNewDisplayProfileUseCase
import jp.kaleidot725.adbpad.ui.container.AppBroadCast
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplayAction
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplayFailureReason
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplayFeedback
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplaySideEffect
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.ScrcpyNewDisplayState
import jp.kaleidot725.adbpad.ui.screen.newdisplay.state.filterScrcpyNewDisplayProfiles
import jp.kaleidot725.pulse.mvi.PulseStore
import kotlinx.coroutines.launch
import java.util.UUID

class ScrcpyNewDisplayStateHolder(
    private val getSelectedDeviceFlowUseCase: GetSelectedDeviceFlowUseCase,
    private val getScrcpyNewDisplayProfilesUseCase: GetScrcpyNewDisplayProfilesUseCase,
    private val launchScrcpyNewDisplayUseCase: LaunchScrcpyNewDisplayUseCase,
    private val saveScrcpyNewDisplayProfileUseCase: SaveScrcpyNewDisplayProfileUseCase,
    private val deleteScrcpyNewDisplayProfileUseCase: DeleteScrcpyNewDisplayProfileUseCase,
) : PulseStore<ScrcpyNewDisplayState, ScrcpyNewDisplayAction, ScrcpyNewDisplaySideEffect, AppBroadCast>(
        initialUiState = ScrcpyNewDisplayState(),
    ) {
    override fun onSetup() {
        coroutineScope.launch {
            getSelectedDeviceFlowUseCase().collect { device ->
                update { copy(selectedDevice = device) }
            }
        }

        coroutineScope.launch {
            val profiles = getScrcpyNewDisplayProfilesUseCase()
            updateProfiles(profiles)
        }
    }

    override fun onAction(uiAction: ScrcpyNewDisplayAction) {
        when (uiAction) {
            is ScrcpyNewDisplayAction.SelectProfile -> {
                selectProfile(uiAction.profileId)
            }

            ScrcpyNewDisplayAction.LaunchSelectedProfile -> {
                launchSelectedProfile()
            }

            is ScrcpyNewDisplayAction.UpdateSearchText -> {
                updateSearchText(uiAction.text)
            }

            is ScrcpyNewDisplayAction.UpdateSortType -> {
                updateSortType(uiAction.sortType)
            }

            is ScrcpyNewDisplayAction.UpdateScrcpyOptions -> {
                updateScrcpyOptions(uiAction.options)
            }

            ScrcpyNewDisplayAction.AddNewProfile -> {
                addNewProfile()
            }

            ScrcpyNewDisplayAction.SaveProfile -> {
                saveProfile()
            }

            is ScrcpyNewDisplayAction.DeleteProfile -> {
                deleteProfile(uiAction.profile)
            }

            is ScrcpyNewDisplayAction.UpdateInputName -> {
                update { copy(inputName = uiAction.text) }
                saveProfile(inputName = uiAction.text)
            }

            is ScrcpyNewDisplayAction.UpdateInputWidth -> {
                update { copy(inputWidth = uiAction.text) }
                saveProfile(inputWidth = uiAction.text)
            }

            is ScrcpyNewDisplayAction.UpdateInputHeight -> {
                update { copy(inputHeight = uiAction.text) }
                saveProfile(inputHeight = uiAction.text)
            }

            is ScrcpyNewDisplayAction.UpdateInputDpi -> {
                update { copy(inputDpi = uiAction.text) }
                saveProfile(inputDpi = uiAction.text)
            }

            ScrcpyNewDisplayAction.SelectNextProfile -> {
                selectNextProfile()
            }

            ScrcpyNewDisplayAction.SelectPreviousProfile -> {
                selectPreviousProfile()
            }
        }
    }

    override fun onReceive(broadcast: AppBroadCast) {
        when (broadcast) {
            AppBroadCast.Refresh -> {
                coroutineScope.launch {
                    val profiles = getScrcpyNewDisplayProfilesUseCase()
                    updateProfiles(profiles)
                }
            }
        }
    }

    private fun updateScrcpyOptions(options: ScrcpyOptions) {
        val currentProfile = currentState.selectedProfile ?: return
        val updatedProfile = currentProfile.copy(options = options)

        coroutineScope.launch {
            saveScrcpyNewDisplayProfileUseCase(updatedProfile)
            updateProfiles(getScrcpyNewDisplayProfilesUseCase())
        }
    }

    private fun selectProfile(profileId: String) {
        update {
            val selected = profiles.firstOrNull { it.id == profileId }
            copy(
                selectedProfileId = profileId,
                inputName = selected?.displayName ?: "",
                inputWidth = selected?.width?.toString() ?: "",
                inputHeight = selected?.height?.toString() ?: "",
                inputDpi = selected?.densityDpi?.toString() ?: "",
                feedback = ScrcpyNewDisplayFeedback.None,
            )
        }
    }

    private fun addNewProfile() {
        val newProfileId = UUID.randomUUID().toString()
        val newProfile =
            ScrcpyNewDisplayProfile(
                id = newProfileId,
                displayName = "New Profile",
                width = 1080,
                height = 1920,
                densityDpi = 440,
                refreshRateHz = 60,
                options = ScrcpyOptions(noAudio = true),
            )

        coroutineScope.launch {
            saveScrcpyNewDisplayProfileUseCase(newProfile)
            val profiles = getScrcpyNewDisplayProfilesUseCase()
            updateProfiles(profiles)
            selectProfile(newProfileId)
        }
    }

    private fun saveProfile(
        inputName: String? = null,
        inputWidth: String? = null,
        inputHeight: String? = null,
        inputDpi: String? = null,
    ) {
        val currentProfile = currentState.selectedProfile ?: return

        val name = inputName ?: currentState.inputName
        val width = inputWidth?.toIntOrNull() ?: currentState.inputWidth.toIntOrNull() ?: currentProfile.width
        val height = inputHeight?.toIntOrNull() ?: currentState.inputHeight.toIntOrNull() ?: currentProfile.height
        val dpi = inputDpi?.toIntOrNull() ?: currentState.inputDpi.toIntOrNull()

        val updatedProfile =
            currentProfile.copy(
                displayName = name,
                width = width,
                height = height,
                densityDpi = dpi,
            )

        coroutineScope.launch {
            saveScrcpyNewDisplayProfileUseCase(updatedProfile)
            updateProfiles(getScrcpyNewDisplayProfilesUseCase())
        }
    }

    private fun deleteProfile(profile: ScrcpyNewDisplayProfile) {
        coroutineScope.launch {
            deleteScrcpyNewDisplayProfileUseCase(profile.id)
            val profiles = getScrcpyNewDisplayProfilesUseCase()
            updateProfiles(profiles)
            update { copy(selectedProfileId = profiles.firstOrNull()?.id) }
        }
    }

    private fun updateSearchText(text: String) {
        update {
            val filteredProfiles = filterScrcpyNewDisplayProfiles(profiles, text, sortType)
            val nextSelection =
                when {
                    filteredProfiles.any { it.id == selectedProfileId } -> selectedProfileId
                    else -> filteredProfiles.firstOrNull()?.id
                }

            val nextProfile = profiles.firstOrNull { it.id == nextSelection }
            copy(
                searchText = text,
                selectedProfileId = nextSelection,
                inputName = nextProfile?.displayName ?: "",
                inputWidth = nextProfile?.width?.toString() ?: "",
                inputHeight = nextProfile?.height?.toString() ?: "",
                inputDpi = nextProfile?.densityDpi?.toString() ?: "",
                feedback = ScrcpyNewDisplayFeedback.None,
            )
        }
    }

    private fun updateProfiles(profiles: List<ScrcpyNewDisplayProfile>) {
        update {
            val filteredProfiles = filterScrcpyNewDisplayProfiles(profiles, searchText, sortType)
            val nextSelection =
                when {
                    filteredProfiles.any { it.id == selectedProfileId } -> selectedProfileId
                    else -> filteredProfiles.firstOrNull()?.id
                }

            val nextProfile = profiles.firstOrNull { it.id == nextSelection }
            val isSelectionChanged = selectedProfileId != nextSelection

            copy(
                profiles = profiles,
                selectedProfileId = nextSelection,
                inputName = if (isSelectionChanged) nextProfile?.displayName ?: "" else inputName,
                inputWidth = if (isSelectionChanged) nextProfile?.width?.toString() ?: "" else inputWidth,
                inputHeight = if (isSelectionChanged) nextProfile?.height?.toString() ?: "" else inputHeight,
                inputDpi = if (isSelectionChanged) nextProfile?.densityDpi?.toString() ?: "" else inputDpi,
            )
        }
    }

    private fun updateSortType(sortType: SortType) {
        update {
            val filteredProfiles = filterScrcpyNewDisplayProfiles(profiles, searchText, sortType)
            val nextSelection =
                when {
                    filteredProfiles.any { it.id == selectedProfileId } -> selectedProfileId
                    else -> filteredProfiles.firstOrNull()?.id
                }

            val nextProfile = profiles.firstOrNull { it.id == nextSelection }
            copy(
                sortType = sortType,
                selectedProfileId = nextSelection,
                inputName = nextProfile?.displayName ?: "",
                inputWidth = nextProfile?.width?.toString() ?: "",
                inputHeight = nextProfile?.height?.toString() ?: "",
                inputDpi = nextProfile?.densityDpi?.toString() ?: "",
                feedback = ScrcpyNewDisplayFeedback.None,
            )
        }
    }

    private fun launchSelectedProfile() {
        val selectedProfile = state.value.selectedProfile
        if (selectedProfile == null) {
            update {
                copy(
                    feedback = ScrcpyNewDisplayFeedback.Failure(ScrcpyNewDisplayFailureReason.ProcessError),
                )
            }
            return
        }

        val device = state.value.selectedDevice
        if (device == null) {
            update {
                copy(
                    feedback = ScrcpyNewDisplayFeedback.Failure(ScrcpyNewDisplayFailureReason.NoDevice),
                )
            }
            return
        }

        if (state.value.isLaunching) return

        update {
            copy(
                isLaunching = true,
                feedback = ScrcpyNewDisplayFeedback.None,
            )
        }

        coroutineScope.launch {
            val success = launchScrcpyNewDisplayUseCase(device, selectedProfile, selectedProfile.options)
            update {
                copy(
                    isLaunching = false,
                    feedback =
                        if (success) {
                            ScrcpyNewDisplayFeedback.Success
                        } else {
                            ScrcpyNewDisplayFeedback.Failure(ScrcpyNewDisplayFailureReason.ProcessError)
                        },
                )
            }
        }
    }

    private fun selectNextProfile() {
        val filteredProfiles = state.value.filteredProfiles
        if (filteredProfiles.isEmpty()) return

        val currentProfile = state.value.selectedProfile
        val currentIndex = filteredProfiles.indexOfFirst { it.id == currentProfile?.id }
        val nextIndex =
            when {
                currentIndex == -1 -> 0
                currentIndex < filteredProfiles.lastIndex -> currentIndex + 1
                else -> null
            }

        nextIndex?.let { index ->
            val nextProfileId = filteredProfiles[index].id
            update {
                copy(
                    selectedProfileId = nextProfileId,
                    feedback = ScrcpyNewDisplayFeedback.None,
                )
            }
        }
    }

    private fun selectPreviousProfile() {
        val filteredProfiles = state.value.filteredProfiles
        if (filteredProfiles.isEmpty()) return

        val currentProfile = state.value.selectedProfile ?: return
        val currentIndex = filteredProfiles.indexOfFirst { it.id == currentProfile.id }
        if (currentIndex <= 0) return

        val previousProfileId = filteredProfiles[currentIndex - 1].id
        update {
            copy(
                selectedProfileId = previousProfileId,
                feedback = ScrcpyNewDisplayFeedback.None,
            )
        }
    }
}
