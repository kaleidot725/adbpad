package jp.kaleidot725.adbpad.ui.screen.app.component.tree

import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry

internal val previewAppFileEntries =
    listOf(
        AppFileEntry.Directory(
            name = "files",
            path = "/data/data/com.example.notes/files",
            permissions = "drwxrwx--x",
            size = 4096,
            date = "2026-05-08",
            time = "12:34",
        ),
        AppFileEntry.File(
            name = "settings.toml",
            path = "/data/data/com.example.notes/files/settings.toml",
            permissions = "-rw-rw----",
            size = 128,
            date = "2026-05-08",
            time = "12:35",
        ),
    )

internal val previewChildAppFileEntries =
    listOf(
        AppFileEntry.File(
            name = "cache.db",
            path = "/data/data/com.example.notes/files/cache.db",
            permissions = "-rw-rw----",
            size = 4096,
            date = "2026-05-08",
            time = "12:36",
        ),
    )
