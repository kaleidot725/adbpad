package jp.kaleidot725.adbpad.domain.usecase.adb

import com.malinskiy.adam.interactor.StartAdbInteractor
import jp.kaleidot725.adbpad.domain.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class StartAdbUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): Boolean {
        return withContext(Dispatchers.IO) {
            val sdkPath = settingRepository.getSdkPath()
            return@withContext StartAdbInteractor().execute(
                adbBinary = File(sdkPath.adbDirectory),
                serverPort = sdkPath.adbServerPort,
            )
        }
    }
}
