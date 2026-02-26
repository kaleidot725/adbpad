package jp.kaleidot725.adbpad.domain.model.command

import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import jp.kaleidot725.adbpad.domain.model.language.Language

interface NormalCommand {
    val title: String
    val details: String
    val isRunning: Boolean
    val isFavorite: Boolean
    val requests: List<ShellCommandRequest>
    val category: NormalCommandCategory

    val commandStrings: List<String>
        get() = requests.map { it.cmd }

    fun updateFavorite(isFavorite: Boolean): NormalCommand

    data class PointerLocationOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandPointerLocationOnTitle
        override val details: String get() = Language.commandPointerLocationOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system pointer_location 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class PointerLocationOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandPointerLocationOffTitle
        override val details: String get() = Language.commandPointerLocationOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system pointer_location 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class LayoutBorderOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandLayoutBorderOnTitle
        override val details: String get() = Language.commandLayoutBorderOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("setprop debug.layout true"),
                ShellCommandRequest("service call activity 1599295570"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class LayoutBorderOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandLayoutBorderOffTitle
        override val details: String get() = Language.commandLayoutBorderOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("setprop debug.layout false"),
                ShellCommandRequest("service call activity 1599295570"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class TapEffectOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandTapEffectOnTitle
        override val details: String get() = Language.commandTapEffectOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system show_touches 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class TapEffectOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandTapEffectOffTitle
        override val details: String get() = Language.commandTapEffectOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system show_touches 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class SleepModeOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandSleepModeOffTitle
        override val details: String get() = Language.commandSleepModeOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global stay_on_while_plugged_in 7"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DEVICE

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class SleepModeOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandSleepModeOnTitle
        override val details: String get() = Language.commandSleepModeOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global stay_on_while_plugged_in 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DEVICE

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DarkThemeOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDarkThemeOnTitle
        override val details: String get() = Language.commandDarkThemeOnDetails
        override val requests: List<ShellCommandRequest> = listOf(ShellCommandRequest("cmd uimode night yes"))
        override val category: NormalCommandCategory = NormalCommandCategory.THEME

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DarkThemeOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDarkThemeOffTitle
        override val details: String get() = Language.commandDarkThemeOffDetails
        override val requests: List<ShellCommandRequest> = listOf(ShellCommandRequest("cmd uimode night no"))
        override val category: NormalCommandCategory = NormalCommandCategory.THEME

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class WifiOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandWifiOnTitle
        override val details: String get() = Language.commandWifiOnDetails
        override val requests: List<ShellCommandRequest> = listOf(ShellCommandRequest("svc wifi enable"))
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class WifiOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandWifiOffTitle
        override val details: String get() = Language.commandWifiOffDetails
        override val requests: List<ShellCommandRequest> = listOf(ShellCommandRequest("svc wifi disable"))
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DataOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDataOnTitle
        override val details: String get() = Language.commandDataOnDetails
        override val requests: List<ShellCommandRequest> = listOf(ShellCommandRequest("svc data enable"))
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DataOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDataOffTitle
        override val details: String get() = Language.commandDataOffDetails
        override val requests: List<ShellCommandRequest> = listOf(ShellCommandRequest("svc data disable"))
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class WifiAndDataOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandWifiAndDataOnTitle
        override val details: String get() = Language.commandWifiAndDataOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("svc wifi enable"),
                ShellCommandRequest("svc data enable"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class WifiAndDataOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandWifiAndDataOffTitle
        override val details: String get() = Language.commandWifiAndDataOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("svc wifi disable"),
                ShellCommandRequest("svc data disable"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class ScreenPinningOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandScreenPinningOffTitle
        override val details: String get() = Language.commandScreenPinningOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("am task lock stop"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class EnableGestureNavigation(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandEnableGestureNavigationTitle
        override val details: String get() = Language.commandEnableGestureNavigationDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("cmd overlay enable com.android.internal.systemui.navbar.gestural"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.NAVIGATION

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class EnableTwoButtonNavigation(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandEnableTwoButtonNavigationTitle
        override val details: String get() = Language.commandEnableTwoButtonNavigationDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("cmd overlay enable com.android.internal.systemui.navbar.twobutton"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.NAVIGATION

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class EnableThreeButtonNavigation(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandEnableThreeButtonNavigationTitle
        override val details: String get() = Language.commandEnableThreeButtonNavigationDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("cmd overlay enable com.android.internal.systemui.navbar.threebutton"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.NAVIGATION

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class AirplaneModeOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandAirplaneModeOnTitle
        override val details: String get() = Language.commandAirplaneModeOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global airplane_mode_on 1"),
                ShellCommandRequest("am broadcast -a android.intent.action.AIRPLANE_MODE"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class AirplaneModeOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandAirplaneModeOffTitle
        override val details: String get() = Language.commandAirplaneModeOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global airplane_mode_on 0"),
                ShellCommandRequest("am broadcast -a android.intent.action.AIRPLANE_MODE"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class BluetoothOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandBluetoothOnTitle
        override val details: String get() = Language.commandBluetoothOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("svc bluetooth enable"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class BluetoothOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandBluetoothOffTitle
        override val details: String get() = Language.commandBluetoothOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("svc bluetooth disable"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class LocationOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandLocationOnTitle
        override val details: String get() = Language.commandLocationOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put secure location_mode 3"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class LocationOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandLocationOffTitle
        override val details: String get() = Language.commandLocationOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put secure location_mode 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class AnimationsOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandAnimationsOnTitle
        override val details: String get() = Language.commandAnimationsOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global window_animation_scale 1"),
                ShellCommandRequest("settings put global transition_animation_scale 1"),
                ShellCommandRequest("settings put global animator_duration_scale 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class AnimationsOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandAnimationsOffTitle
        override val details: String get() = Language.commandAnimationsOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global window_animation_scale 0"),
                ShellCommandRequest("settings put global transition_animation_scale 0"),
                ShellCommandRequest("settings put global animator_duration_scale 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class AutoRotateOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandAutoRotateOnTitle
        override val details: String get() = Language.commandAutoRotateOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system accelerometer_rotation 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class AutoRotateOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandAutoRotateOffTitle
        override val details: String get() = Language.commandAutoRotateOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system accelerometer_rotation 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class FontScaleSmall(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandFontScaleSmallTitle
        override val details: String get() = Language.commandFontScaleSmallDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system font_scale 0.85"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class FontScaleNormal(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandFontScaleNormalTitle
        override val details: String get() = Language.commandFontScaleNormalDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system font_scale 1.0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class FontScaleLarge(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandFontScaleLargeTitle
        override val details: String get() = Language.commandFontScaleLargeDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system font_scale 1.15"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class FontScaleHuge(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandFontScaleHugeTitle
        override val details: String get() = Language.commandFontScaleHugeDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system font_scale 1.3"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class FontScaleExtraLarge(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandFontScaleExtraLargeTitle
        override val details: String get() = Language.commandFontScaleExtraLargeDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system font_scale 1.5"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class FontScaleMaximum(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandFontScaleMaximumTitle
        override val details: String get() = Language.commandFontScaleMaximumDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system font_scale 2.0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class RtlLayoutOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandRtlLayoutOnTitle
        override val details: String get() = Language.commandRtlLayoutOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global debug.force_rtl 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class RtlLayoutOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandRtlLayoutOffTitle
        override val details: String get() = Language.commandRtlLayoutOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global debug.force_rtl 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class BatterySaverOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandBatterySaverOnTitle
        override val details: String get() = Language.commandBatterySaverOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global low_power 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DEVICE

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class BatterySaverOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandBatterySaverOffTitle
        override val details: String get() = Language.commandBatterySaverOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put global low_power 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DEVICE

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DataSaverOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDataSaverOnTitle
        override val details: String get() = Language.commandDataSaverOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("cmd netpolicy set restrict-background true"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DataSaverOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDataSaverOffTitle
        override val details: String get() = Language.commandDataSaverOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("cmd netpolicy set restrict-background false"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.COM

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DozeModeOn(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDozeModeOnTitle
        override val details: String get() = Language.commandDozeModeOnDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("dumpsys deviceidle enable"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DEVICE

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class DozeModeOff(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandDozeModeOffTitle
        override val details: String get() = Language.commandDozeModeOffDetails
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("dumpsys deviceidle disable"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DEVICE

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class ScreenRotation0(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandScreenRotation0Title
        override val details: String get() = Language.commandScreenRotation0Details
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system user_rotation 0"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class ScreenRotation90(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandScreenRotation90Title
        override val details: String get() = Language.commandScreenRotation90Details
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system user_rotation 1"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class ScreenRotation180(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandScreenRotation180Title
        override val details: String get() = Language.commandScreenRotation180Details
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system user_rotation 2"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }

    data class ScreenRotation270(
        override val isRunning: Boolean = false,
        override val isFavorite: Boolean = false,
    ) : NormalCommand {
        override val title: String get() = Language.commandScreenRotation270Title
        override val details: String get() = Language.commandScreenRotation270Details
        override val requests: List<ShellCommandRequest> =
            listOf(
                ShellCommandRequest("settings put system user_rotation 3"),
            )
        override val category: NormalCommandCategory = NormalCommandCategory.DISPLAY

        override fun updateFavorite(isFavorite: Boolean): NormalCommand = copy(isFavorite = isFavorite)
    }
}
