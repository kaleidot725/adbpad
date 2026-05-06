package jp.kaleidot725.adbpad.domain.model.app

enum class AppDataDirectory {
    Data,
    SdCardData,
    ;

    fun getRootPath(app: InstalledApp): String =
        when (this) {
            Data -> app.dataDir
            SdCardData -> app.sdCardDataDir
        }
}
