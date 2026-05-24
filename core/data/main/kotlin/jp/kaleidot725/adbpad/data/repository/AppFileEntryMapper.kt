package jp.kaleidot725.adbpad.data.repository

import com.malinskiy.adam.Const
import com.malinskiy.adam.request.sync.AndroidFileType
import com.malinskiy.adam.request.sync.model.FileEntry
import jp.kaleidot725.adbpad.domain.model.app.AppFileEntry
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal object AppFileEntryMapper {
    fun toPrivateDataPath(path: String): PrivateDataPath? {
        if (!path.startsWith(DATA_DIRECTORY_PREFIX)) return null

        val withoutPrefix = path.removePrefix(DATA_DIRECTORY_PREFIX)
        val packageName = withoutPrefix.substringBefore('/').takeIf { it.isNotBlank() } ?: return null
        val relativePath =
            withoutPrefix
                .substringAfter('/', missingDelimiterValue = ".")
                .ifBlank { "." }

        return PrivateDataPath(
            packageName = packageName,
            relativePath = relativePath,
        )
    }

    fun fromRunAsLsOutput(
        directory: String,
        output: String,
    ): List<AppFileEntry> =
        output
            .lineSequence()
            .mapNotNull { it.toAppFileEntryFromLs(directory) }
            .filterNot { it.name == "." || it.name == ".." }
            .sortedWith(appFileEntryComparator())
            .toList()

    fun fromSyncEntries(
        directory: String,
        entries: List<FileEntry>,
    ): List<AppFileEntry> =
        entries
            .asSequence()
            .filter { it.exists() }
            .filterNot { it.name == "." || it.name == ".." || it.name == null }
            .map { it.toAppFileEntry(directory) }
            .sortedWith(appFileEntryComparator())
            .toList()

    fun shellQuote(value: String): String = "'${value.replace("'", "'\\''")}'"

    fun sanitizeRemoteFileName(name: String): String =
        name
            .replace(REMOTE_FILE_NAME_REGEX, "_")
            .ifBlank { "file" }

    private fun String.toAppFileEntryFromLs(directory: String): AppFileEntry? {
        val match = LS_LINE_REGEX.matchEntire(trim()) ?: return null
        val permissions = match.groupValues[1]
        val size = match.groupValues[4].filter { it.isDigit() }.toLongOrNull() ?: 0L
        val date = match.groupValues[5]
        val time = match.groupValues[6]
        var name = match.groupValues[7]
        val type = permissions.firstOrNull().toAndroidFileType()

        if (type == AndroidFileType.SYMBOLIC_LINK) {
            name = name.substringBefore(" -> ").trim()
        }

        return toAppFileEntry(
            type = type,
            name = name,
            path = directory.resolveChildPath(name),
            permissions = permissions,
            size = size,
            date = date,
            time = time,
        )
    }

    private fun FileEntry.toAppFileEntry(directory: String): AppFileEntry {
        val name = requireNotNull(name)
        val path = directory.resolveChildPath(name)
        val dateTime = mtime.atZone(FILE_TIME_ZONE)
        return toAppFileEntry(
            type = type,
            name = name,
            path = path,
            permissions = mode.toPermissionString(),
            size = size().toLong(),
            date = FILE_DATE_FORMATTER.format(dateTime),
            time = FILE_TIME_FORMATTER.format(dateTime),
        )
    }

    private fun toAppFileEntry(
        type: AndroidFileType,
        name: String,
        path: String,
        permissions: String,
        size: Long,
        date: String,
        time: String,
    ): AppFileEntry =
        when (type) {
            AndroidFileType.DIRECTORY -> {
                AppFileEntry.Directory(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            }

            AndroidFileType.REGULAR_FILE -> {
                AppFileEntry.File(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            }

            AndroidFileType.SYMBOLIC_LINK -> {
                AppFileEntry.Link(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            }

            else -> {
                AppFileEntry.Other(
                    name = name,
                    path = path,
                    permissions = permissions,
                    size = size,
                    date = date,
                    time = time,
                )
            }
        }

    private val FileEntry.type: AndroidFileType
        get() =
            when {
                isDirectory() -> AndroidFileType.DIRECTORY
                isRegularFile() -> AndroidFileType.REGULAR_FILE
                isBlockDevice() -> AndroidFileType.BLOCK_SPECIAL_FILE
                isCharDevice() -> AndroidFileType.CHARACTER_SPECIAL_FILE
                isLink() -> AndroidFileType.SYMBOLIC_LINK
                mode.hasFileType(Const.FileType.S_IFIFO) -> AndroidFileType.FIFO
                mode.hasFileType(Const.FileType.S_IFSOCK) -> AndroidFileType.SOCKET_LINK
                else -> AndroidFileType.OTHER
            }

    private fun UInt.hasFileType(fileType: UInt): Boolean = (this and Const.FileType.S_IFMT) == fileType

    private fun String.resolveChildPath(name: String): String =
        if (endsWith("/")) {
            "$this$name"
        } else {
            "$this/$name"
        }

    private fun Char?.toAndroidFileType(): AndroidFileType =
        when (this) {
            '-' -> AndroidFileType.REGULAR_FILE
            'b' -> AndroidFileType.BLOCK_SPECIAL_FILE
            'c' -> AndroidFileType.CHARACTER_SPECIAL_FILE
            'd' -> AndroidFileType.DIRECTORY
            'l' -> AndroidFileType.SYMBOLIC_LINK
            'p' -> AndroidFileType.FIFO
            's' -> AndroidFileType.SOCKET_LINK
            else -> AndroidFileType.OTHER
        }

    private fun UInt.toPermissionString(): String {
        val mode = toInt()
        return buildString {
            append(mode.toFileTypeChar())
            appendReadWriteExecute(mode, OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, SET_UID, 's', 'S')
            appendReadWriteExecute(mode, GROUP_READ, GROUP_WRITE, GROUP_EXECUTE, SET_GID, 's', 'S')
            appendReadWriteExecute(mode, OTHER_READ, OTHER_WRITE, OTHER_EXECUTE, STICKY, 't', 'T')
        }
    }

    private fun StringBuilder.appendReadWriteExecute(
        mode: Int,
        readMask: Int,
        writeMask: Int,
        executeMask: Int,
        specialMask: Int,
        specialExecuteChar: Char,
        specialOnlyChar: Char,
    ) {
        append(if (mode hasMode readMask) 'r' else '-')
        append(if (mode hasMode writeMask) 'w' else '-')
        append(
            when {
                mode hasMode specialMask -> if (mode hasMode executeMask) specialExecuteChar else specialOnlyChar
                mode hasMode executeMask -> 'x'
                else -> '-'
            },
        )
    }

    private fun Int.toFileTypeChar(): Char =
        when (toUInt() and Const.FileType.S_IFMT) {
            Const.FileType.S_IFDIR -> 'd'
            Const.FileType.S_IFREG -> '-'
            Const.FileType.S_IFBLK -> 'b'
            Const.FileType.S_IFCHR -> 'c'
            Const.FileType.S_IFLNK -> 'l'
            Const.FileType.S_IFIFO -> 'p'
            Const.FileType.S_IFSOCK -> 's'
            else -> '?'
        }

    private infix fun Int.hasMode(mask: Int): Boolean = this and mask != 0

    private fun appFileEntryComparator(): Comparator<AppFileEntry> =
        compareByDescending<AppFileEntry> { it.isDirectory }
            .thenBy { it.name.lowercase(Locale.getDefault()) }

    data class PrivateDataPath(
        val packageName: String,
        val relativePath: String,
    )

    private const val DATA_DIRECTORY_PREFIX = "/data/data/"
    private const val OWNER_READ = 0x100
    private const val OWNER_WRITE = 0x80
    private const val OWNER_EXECUTE = 0x40
    private const val GROUP_READ = 0x20
    private const val GROUP_WRITE = 0x10
    private const val GROUP_EXECUTE = 0x8
    private const val OTHER_READ = 0x4
    private const val OTHER_WRITE = 0x2
    private const val OTHER_EXECUTE = 0x1
    private const val SET_UID = 0x800
    private const val SET_GID = 0x400
    private const val STICKY = 0x200
    private val FILE_TIME_ZONE = ZoneId.systemDefault()
    private val FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val FILE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    private val REMOTE_FILE_NAME_REGEX = Regex("[^A-Za-z0-9._-]")
    private val LS_LINE_REGEX =
        Regex(
            "^([bcdlsp-][-r][-w][-xsS][-r][-w][-xsS][-r][-w][-xstST])\\s+" +
                "(?:\\d+\\s+)?(\\S+)\\s+(\\S+)\\s+([\\d\\s,]*)\\s+" +
                "(\\d{4}-\\d\\d-\\d\\d)\\s+(\\d\\d:\\d\\d)\\s+(.*)$",
        )
}
