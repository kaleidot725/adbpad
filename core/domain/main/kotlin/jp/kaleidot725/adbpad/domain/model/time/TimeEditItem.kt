package jp.kaleidot725.adbpad.domain.model.time

import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
data class TimeEditItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val date: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
    val time: String = LocalTime.now().withNano(0).format(DateTimeFormatter.ofPattern("HH:mm:ss")),
    val isAutoDateTime: Boolean = false,
    val timeZoneId: String = ZoneId.systemDefault().id,
    val isAutoTimeZone: Boolean = false,
    val lastModified: Long = System.currentTimeMillis(),
) {
    val formattedDateTime: String = "$date $time"
}
