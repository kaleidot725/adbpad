package jp.kaleidot725.adbpad.domain.model.app

sealed interface AppFileEntry {
    val name: String
    val path: String
    val type: AppFileType
    val permissions: String
    val size: Long
    val date: String
    val time: String

    val isDirectory: Boolean
        get() = this is Directory

    data class File(
        override val name: String,
        override val path: String,
        override val permissions: String,
        override val size: Long,
        override val date: String,
        override val time: String,
    ) : AppFileEntry {
        override val type: AppFileType = AppFileType.File
    }

    data class Directory(
        override val name: String,
        override val path: String,
        override val permissions: String,
        override val size: Long,
        override val date: String,
        override val time: String,
    ) : AppFileEntry {
        override val type: AppFileType = AppFileType.Directory
    }

    data class Link(
        override val name: String,
        override val path: String,
        override val permissions: String,
        override val size: Long,
        override val date: String,
        override val time: String,
    ) : AppFileEntry {
        override val type: AppFileType = AppFileType.Link
    }

    data class Other(
        override val name: String,
        override val path: String,
        override val permissions: String,
        override val size: Long,
        override val date: String,
        override val time: String,
    ) : AppFileEntry {
        override val type: AppFileType = AppFileType.Other
    }
}

enum class AppFileType {
    File,
    Directory,
    Link,
    Other,
}
