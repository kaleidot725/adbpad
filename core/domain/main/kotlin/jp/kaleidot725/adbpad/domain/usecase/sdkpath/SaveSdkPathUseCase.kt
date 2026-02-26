package jp.kaleidot725.adbpad.domain.usecase.sdkpath

import jp.kaleidot725.adbpad.domain.model.setting.SdkPath
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class SaveSdkPathUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(
        adbDirectory: String,
        adbServerPort: Int?,
    ): Boolean {
        val sdkPath = SdkPath(adbDirectory, adbServerPort ?: 30000)
        return settingRepository.updateSdkPath(sdkPath)
    }
}
