package jp.kaleidot725.adbpad.data.repository

import jp.kaleidot725.adbpad.data.local.ScrcpyNewDisplayProfileFileCreator
import jp.kaleidot725.adbpad.domain.model.scrcpy.ScrcpyNewDisplayProfile
import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScrcpyNewDisplayProfileRepositoryImpl(
    private val fileCreator: ScrcpyNewDisplayProfileFileCreator,
) : ScrcpyNewDisplayProfileRepository {
    private val lock: Any = Any()

    override suspend fun getUserProfiles(): List<ScrcpyNewDisplayProfile> =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                fileCreator.load()
            }
        }

    override suspend fun addUserProfile(profile: ScrcpyNewDisplayProfile) =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val updatedProfile = profile.copy(lastModified = System.currentTimeMillis())
                val currentProfiles = fileCreator.load().toMutableList()
                currentProfiles.removeAll { it.id == updatedProfile.id }
                currentProfiles.add(updatedProfile)
                fileCreator.save(currentProfiles)
            }
        }

    override suspend fun removeUserProfile(profileId: String) =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val currentProfiles = fileCreator.load().toMutableList()
                currentProfiles.removeAll { it.id == profileId }
                fileCreator.save(currentProfiles)
            }
        }
}
