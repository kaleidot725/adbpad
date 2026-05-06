package jp.kaleidot725.adbpad.domain.model.app

data class AppFileEntry(
    val name: String,
    val path: String,
    val type: AppFileType,
    val permissions: String,
    val size: Long?,
    val modifiedAt: String?,
) {
    val isDirectory: Boolean
        get() = type == AppFileType.Directory
}

enum class AppFileType {
    File,
    Directory,
    Link,
    Other,
}
