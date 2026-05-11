package jp.kaleidot725.adbpad.domain.model.app

import java.io.File

sealed interface AppFilePreview {
    val entry: AppFileEntry

    data class Image(
        override val entry: AppFileEntry.File,
        val localFile: File,
    ) : AppFilePreview

    data class Text(
        override val entry: AppFileEntry.File,
        val text: String,
    ) : AppFilePreview

    data class Unsupported(
        override val entry: AppFileEntry,
    ) : AppFilePreview
}
