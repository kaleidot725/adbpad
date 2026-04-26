package jp.kaleidot725.adbpad.domain.usecase.app

import jp.kaleidot725.adbpad.domain.model.app.InstalledApp
import jp.kaleidot725.adbpad.domain.model.device.Device
import jp.kaleidot725.adbpad.domain.repository.InstalledAppRepository
import java.io.File

class GetInstalledAppIconUseCase(
    private val installedAppRepository: InstalledAppRepository,
) {
    suspend operator fun invoke(
        device: Device,
        app: InstalledApp,
    ): File? = installedAppRepository.getInstalledAppIcon(device, app)
}
