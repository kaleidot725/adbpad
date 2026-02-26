package jp.kaleidot725.adbpad.domain.model.device

import jp.kaleidot725.scrcpykt.option.AudioCodec
import jp.kaleidot725.scrcpykt.option.AudioSource
import jp.kaleidot725.scrcpykt.option.CameraFacing
import jp.kaleidot725.scrcpykt.option.CaptureOrientation
import jp.kaleidot725.scrcpykt.option.GamepadMode
import jp.kaleidot725.scrcpykt.option.KeyboardMode
import jp.kaleidot725.scrcpykt.option.MouseMode
import jp.kaleidot725.scrcpykt.option.RecordFormat
import jp.kaleidot725.scrcpykt.option.VideoCodec
import jp.kaleidot725.scrcpykt.option.VideoSource
import kotlinx.serialization.Serializable

@Serializable
data class ScrcpyOptions(
    // Video options (matching ScrcpyCommand)
    val videoBitRate: Int? = null,
    val maxFps: Int? = null,
    val maxSize: Int? = null,
    val videoCodec: VideoCodec? = null,
    val videoSource: VideoSource? = null,
    val noVideo: Boolean = false,
    // Audio options (matching ScrcpyCommand)
    val audioBitRate: Int? = null,
    val audioCodec: AudioCodec? = null,
    val audioSource: AudioSource? = null,
    val audioBuffer: Int? = null,
    val noAudio: Boolean = false,
    // Display options (matching ScrcpyCommand)
    val displayId: Int? = null,
    val windowTitle: String? = null,
    val windowX: Int? = null,
    val windowY: Int? = null,
    val windowWidth: Int? = null,
    val windowHeight: Int? = null,
    val fullscreen: Boolean = false,
    val alwaysOnTop: Boolean = false,
    // Input options (matching ScrcpyCommand)
    val keyboard: KeyboardMode? = null,
    val mouse: MouseMode? = null,
    val gamepad: GamepadMode? = null,
    val otg: Boolean = false,
    // Camera options (matching ScrcpyCommand)
    val cameraSize: String? = null,
    val cameraFacing: CameraFacing? = null,
    val cameraId: String? = null,
    val cameraFps: Int? = null,
    // Recording options (matching ScrcpyCommand)
    val record: String? = null,
    val recordFormat: RecordFormat? = null,
    val captureOrientation: CaptureOrientation? = null,
    val noPlayback: Boolean = false,
    val v4l2Sink: String? = null,
    // Connection options (matching ScrcpyCommand)
    val serial: String? = null,
    val selectUsb: Boolean = false,
    val selectTcpip: Boolean = false,
    val tcpip: String? = null,
    // Control options (matching ScrcpyCommand)
    val turnScreenOff: Boolean = false,
    val stayAwake: Boolean = false,
    val powerOffOnClose: Boolean = false,
    val showTouches: Boolean = false,
    val disableScreensaver: Boolean = false,
    // Other options (matching ScrcpyCommand)
    val renderDriver: String? = null,
    val pushTarget: String? = null,
    val startApp: String? = null,
    // Output options (matching ScrcpyCommand)
    val stdoutFile: String? = null,
    val stderrFile: String? = null,
)
