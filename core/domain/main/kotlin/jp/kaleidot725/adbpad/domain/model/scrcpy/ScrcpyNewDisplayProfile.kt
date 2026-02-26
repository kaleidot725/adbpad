package jp.kaleidot725.adbpad.domain.model.scrcpy

import jp.kaleidot725.adbpad.domain.model.device.ScrcpyOptions
import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Predefined profile describing a virtual display created via `scrcpy --new-display`.
 */
@Serializable
data class ScrcpyNewDisplayProfile(
    val id: String,
    val displayName: String,
    val width: Int,
    val height: Int,
    val densityDpi: Int?,
    val refreshRateHz: Int?,
    val note: String? = null,
    val options: ScrcpyOptions = ScrcpyOptions(),
    val lastModified: Long = System.currentTimeMillis(),
) {
    val aspectRatio: String = calculateAspectRatio(width, height)
    val shortSpec: String = "${max(width, height)} x ${min(width, height)} ($aspectRatio)"

    companion object {
        private fun calculateAspectRatio(
            width: Int,
            height: Int,
        ): String {
            val gcdValue = gcd(width, height)
            val w = width / gcdValue
            val h = height / gcdValue
            return "${max(w, h)}:${min(w, h)}"
        }

        private tailrec fun gcd(
            a: Int,
            b: Int,
        ): Int {
            val absA = abs(a)
            val absB = abs(b)
            return if (absB == 0) absA else gcd(absB, absA % absB)
        }
    }
}
