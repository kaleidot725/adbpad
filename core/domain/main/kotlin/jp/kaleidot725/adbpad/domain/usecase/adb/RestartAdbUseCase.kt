package jp.kaleidot725.adbpad.domain.usecase.adb

import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.interactor.StopAdbInteractor
import jp.kaleidot725.adbpad.domain.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class RestartAdbUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(
        oldServerPort: Int,
        oldAdbDirectory: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                StopAdbInteractor().execute(
                    adbBinary = File(oldAdbDirectory),
                    serverPort = oldServerPort,
                )

                val sdkPath = settingRepository.getSdkPath()
                return@withContext StartAdbInteractor().execute(
                    adbBinary = File(sdkPath.adbDirectory),
                    serverPort = sdkPath.adbServerPort,
                )
            } catch (e: Exception) {
                println(e)
                return@withContext false
            }
        }
    }
}
