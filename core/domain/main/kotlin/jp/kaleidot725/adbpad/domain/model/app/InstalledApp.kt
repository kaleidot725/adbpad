package jp.kaleidot725.adbpad.domain.model.app

data class InstalledApp(
    val packageName: String,
    val sourceDir: String? = null,
) {
    val displayName: String
        get() = packageName.substringAfterLast('.').ifBlank { packageName }

    val dataDir: String
        get() = "/data/data/$packageName"
}
