package jp.kaleidot725.adbpad.data.repository

import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class AppFileEntryMapperTest {
    @Test
    fun `toPrivateDataPath converts data root to package and dot relative path`() {
        val result = AppFileEntryMapper.toPrivateDataPath("/data/data/com.example.app")

        assertEquals("com.example.app", result?.packageName)
        assertEquals(".", result?.relativePath)
    }

    @Test
    fun `toPrivateDataPath converts data child path to package and relative path`() {
        val result = AppFileEntryMapper.toPrivateDataPath("/data/data/com.example.app/shared_prefs/settings.xml")

        assertEquals("com.example.app", result?.packageName)
        assertEquals("shared_prefs/settings.xml", result?.relativePath)
    }

    @Test
    fun `toPrivateDataPath ignores sdcard path`() {
        val result = AppFileEntryMapper.toPrivateDataPath("/sdcard/Android/data/com.example.app/files")

        assertNull(result)
    }

    @Test
    fun `fromRunAsLsOutput converts directory output to app file entries`() {
        val result =
            AppFileEntryMapper.fromRunAsLsOutput(
                directory = "/data/data/com.example.app",
                output =
                    """
                    total 12
                    drwx------ 3 u0_a123 u0_a123 4096 2026-05-29 10:00 .
                    drwx------ 10 u0_a123 u0_a123 4096 2026-05-29 09:59 ..
                    drwxrwx--x 2 u0_a123 u0_a123 4096 2026-05-29 10:01 files
                    -rw-rw---- 1 u0_a123 u0_a123 128 2026-05-29 10:02 settings.xml
                    lrwxrwxrwx 1 u0_a123 u0_a123 11 2026-05-29 10:03 current -> files/cache
                    -rw-rw---- 1 u0_a123 u0_a123 256 2026-05-29 10:04 file with spaces.txt
                    """.trimIndent(),
            )

        assertEquals(
            listOf(
                AppFileEntry.Directory(
                    name = "files",
                    path = "/data/data/com.example.app/files",
                    permissions = "drwxrwx--x",
                    size = 4096,
                    date = "2026-05-29",
                    time = "10:01",
                ),
                AppFileEntry.Link(
                    name = "current",
                    path = "/data/data/com.example.app/current",
                    permissions = "lrwxrwxrwx",
                    size = 11,
                    date = "2026-05-29",
                    time = "10:03",
                ),
                AppFileEntry.File(
                    name = "file with spaces.txt",
                    path = "/data/data/com.example.app/file with spaces.txt",
                    permissions = "-rw-rw----",
                    size = 256,
                    date = "2026-05-29",
                    time = "10:04",
                ),
                AppFileEntry.File(
                    name = "settings.xml",
                    path = "/data/data/com.example.app/settings.xml",
                    permissions = "-rw-rw----",
                    size = 128,
                    date = "2026-05-29",
                    time = "10:02",
                ),
            ),
            result,
        )
    }

    @Test
    fun `fromRunAsLsOutput resolves nested directory child paths`() {
        val result =
            AppFileEntryMapper.fromRunAsLsOutput(
                directory = "/data/data/com.example.app/files",
                output = "-rw-rw---- 1 u0_a123 u0_a123 64 2026-05-29 10:05 cache.db",
            )

        val entry = assertInstanceOf(AppFileEntry.File::class.java, result.single())
        assertEquals("cache.db", entry.name)
        assertEquals("/data/data/com.example.app/files/cache.db", entry.path)
    }

    @Test
    fun `shellQuote escapes single quotes`() {
        val result = AppFileEntryMapper.shellQuote("shared_prefs/user's settings.xml")

        assertEquals("'shared_prefs/user'\\''s settings.xml'", result)
    }

    @Test
    fun `sanitizeRemoteFileName keeps shell temp file names safe`() {
        val result = AppFileEntryMapper.sanitizeRemoteFileName("user settings #1.xml")

        assertEquals("user_settings__1.xml", result)
    }
}
