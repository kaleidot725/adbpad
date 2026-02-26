package jp.kaleidot725.adbpad.domain.model.os

sealed class OSContext(
    val directory: String = "",
) {
    val screenshotDirectory get() = "${directory}screenshot/"

    object Mac : OSContext(System.getProperty("user.home") + "/Library/Application Support/adbpad/")

    object Windows : OSContext(System.getProperty("user.home") + "/adbpad/")

    companion object {
        fun resolveOSContext(): OSContext =
            when (System.getProperty("os.name")) {
                "Mac OS X" -> Mac
                else -> Windows
            }
    }
}
