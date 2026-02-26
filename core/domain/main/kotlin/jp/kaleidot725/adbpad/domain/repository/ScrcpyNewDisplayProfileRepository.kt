package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile

interface ScrcpyNewDisplayProfileRepository {
    suspend fun getUserProfiles(): List<ScrcpyNewDisplayProfile>

    suspend fun addUserProfile(profile: ScrcpyNewDisplayProfile)

    suspend fun removeUserProfile(profileId: String)
}
