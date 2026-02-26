package jp.kaleidot725.adbpad.domain.usecase.sdkpath

import jp.kaleidot725.adbpad.domain.model.setting.SdkPath
import jp.kaleidot725.adbpad.domain.repository.SettingRepository

class GetSdkPathUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): SdkPath = settingRepository.getSdkPath()
}
