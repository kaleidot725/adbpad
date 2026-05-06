package jp.kaleidot725.adbpad.domain.model.app

data class AppFileListResult(
    val entries: List<AppFileEntry> = emptyList(),
    val errorMessage: String? = null,
) {
    val isSuccess: Boolean
        get() = errorMessage == null
}
