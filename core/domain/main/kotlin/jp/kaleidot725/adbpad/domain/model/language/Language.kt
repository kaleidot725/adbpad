package jp.kaleidot725.adbpad.domain.model.language

import jp.kaleidot725.adbpad.domain.model.language.resources.ChineseResources
import jp.kaleidot725.adbpad.domain.model.language.resources.EnglishResources
import jp.kaleidot725.adbpad.domain.model.language.resources.JapaneseResources
import jp.kaleidot725.adbpad.domain.model.language.resources.StringResources
import jp.kaleidot725.adbpad.domain.model.language.resources.TurkishResources

object Language : StringResources {
    override val windowTitle: String
        get() = getCurrentResources().windowTitle
    override val notFoundDevice: String
        get() = getCurrentResources().notFoundDevice
    override val notFoundCommand: String
        get() = getCurrentResources().notFoundCommand
    override val notFoundInputText: String
        get() = getCurrentResources().notFoundInputText
    override val notFoundScreenshot: String
        get() = getCurrentResources().notFoundScreenshot
    override val execute: String
        get() = getCurrentResources().execute
    override val tab: String
        get() = getCurrentResources().tab
    override val save: String
        get() = getCurrentResources().save
    override val delete: String
        get() = getCurrentResources().delete
    override val send: String
        get() = getCurrentResources().send
    override val cancel: String
        get() = getCurrentResources().cancel
    override val open: String
        get() = getCurrentResources().open
    override val edit: String
        get() = getCurrentResources().edit
    override val copy: String
        get() = getCurrentResources().copy
    override val properties: String
        get() = getCurrentResources().properties
    override val targetDevice: String
        get() = getCurrentResources().targetDevice
    override val tool: String
        get() = getCurrentResources().tool
    override val setting: String
        get() = getCurrentResources().setting
    override val dark: String
        get() = getCurrentResources().dark
    override val light: String
        get() = getCurrentResources().light
    override val system: String
        get() = getCurrentResources().system
    override val search: String
        get() = getCurrentResources().search
    override val textCommandUnTitle: String
        get() = getCurrentResources().textCommandUnTitle
    override val screenshotTakeByCurrentTheme: String
        get() = getCurrentResources().screenshotTakeByCurrentTheme
    override val screenshotTakeByDarkTheme: String
        get() = getCurrentResources().screenshotTakeByDarkTheme
    override val screenshotTakeByLightTheme: String
        get() = getCurrentResources().screenshotTakeByLightTheme
    override val screenshotTakeByBothTheme: String
        get() = getCurrentResources().screenshotTakeByBothTheme
    override val commandStartEventFormat: String
        get() = getCurrentResources().commandStartEventFormat
    override val commandEndEventFormat: String
        get() = getCurrentResources().commandEndEventFormat
    override val commandErrorEventFormat: String
        get() = getCurrentResources().commandErrorEventFormat

    override val commandPointerLocationOnTitle: String
        get() = getCurrentResources().commandPointerLocationOnTitle
    override val commandPointerLocationOnDetails: String
        get() = getCurrentResources().commandPointerLocationOnDetails
    override val commandPointerLocationOffTitle: String
        get() = getCurrentResources().commandPointerLocationOffTitle
    override val commandPointerLocationOffDetails: String
        get() = getCurrentResources().commandPointerLocationOffDetails

    override val commandLayoutBorderOnTitle: String
        get() = getCurrentResources().commandLayoutBorderOnTitle
    override val commandLayoutBorderOnDetails: String
        get() = getCurrentResources().commandLayoutBorderOnDetails
    override val commandLayoutBorderOffTitle: String
        get() = getCurrentResources().commandLayoutBorderOffTitle
    override val commandLayoutBorderOffDetails: String
        get() = getCurrentResources().commandLayoutBorderOffDetails
    override val commandTapEffectOnTitle: String
        get() = getCurrentResources().commandTapEffectOnTitle
    override val commandTapEffectOnDetails: String
        get() = getCurrentResources().commandTapEffectOnDetails
    override val commandTapEffectOffTitle: String
        get() = getCurrentResources().commandTapEffectOffTitle
    override val commandTapEffectOffDetails: String
        get() = getCurrentResources().commandTapEffectOffDetails
    override val commandSleepModeOnTitle: String
        get() = getCurrentResources().commandSleepModeOnTitle
    override val commandSleepModeOnDetails: String
        get() = getCurrentResources().commandSleepModeOnDetails
    override val commandSleepModeOffTitle: String
        get() = getCurrentResources().commandSleepModeOffTitle
    override val commandSleepModeOffDetails: String
        get() = getCurrentResources().commandSleepModeOffDetails
    override val commandDarkThemeOnTitle: String
        get() = getCurrentResources().commandDarkThemeOnTitle
    override val commandDarkThemeOnDetails: String
        get() = getCurrentResources().commandDarkThemeOnDetails
    override val commandDarkThemeOffTitle: String
        get() = getCurrentResources().commandDarkThemeOffTitle
    override val commandDarkThemeOffDetails: String
        get() = getCurrentResources().commandDarkThemeOffDetails
    override val commandWifiOnTitle: String
        get() = getCurrentResources().commandWifiOnTitle
    override val commandWifiOnDetails: String
        get() = getCurrentResources().commandWifiOnDetails
    override val commandWifiOffTitle: String
        get() = getCurrentResources().commandWifiOffTitle
    override val commandWifiOffDetails: String
        get() = getCurrentResources().commandWifiOffDetails
    override val commandDataOnTitle: String
        get() = getCurrentResources().commandDataOnTitle
    override val commandDataOnDetails: String
        get() = getCurrentResources().commandDataOnDetails
    override val commandDataOffTitle: String
        get() = getCurrentResources().commandDataOffTitle
    override val commandDataOffDetails: String
        get() = getCurrentResources().commandDataOffDetails
    override val commandWifiAndDataOnTitle: String
        get() = getCurrentResources().commandWifiAndDataOnTitle
    override val commandWifiAndDataOnDetails: String
        get() = getCurrentResources().commandWifiAndDataOnDetails
    override val commandWifiAndDataOffTitle: String
        get() = getCurrentResources().commandWifiAndDataOffTitle
    override val commandWifiAndDataOffDetails: String
        get() = getCurrentResources().commandWifiAndDataOffDetails
    override val commandScreenPinningOffTitle: String
        get() = getCurrentResources().commandScreenPinningOffTitle
    override val commandScreenPinningOffDetails: String
        get() = getCurrentResources().commandScreenPinningOffDetails
    override val commandEnableThreeButtonNavigationTitle: String
        get() = getCurrentResources().commandEnableThreeButtonNavigationTitle
    override val commandEnableThreeButtonNavigationDetails: String
        get() = getCurrentResources().commandEnableThreeButtonNavigationDetails
    override val commandEnableTwoButtonNavigationTitle: String
        get() = getCurrentResources().commandEnableTwoButtonNavigationTitle
    override val commandEnableTwoButtonNavigationDetails: String
        get() = getCurrentResources().commandEnableTwoButtonNavigationDetails
    override val commandEnableGestureNavigationTitle: String
        get() = getCurrentResources().commandEnableGestureNavigationTitle
    override val commandEnableGestureNavigationDetails: String
        get() = getCurrentResources().commandEnableGestureNavigationDetails
    override val commandAirplaneModeOnTitle: String
        get() = getCurrentResources().commandAirplaneModeOnTitle
    override val commandAirplaneModeOnDetails: String
        get() = getCurrentResources().commandAirplaneModeOnDetails
    override val commandAirplaneModeOffTitle: String
        get() = getCurrentResources().commandAirplaneModeOffTitle
    override val commandAirplaneModeOffDetails: String
        get() = getCurrentResources().commandAirplaneModeOffDetails
    override val commandBluetoothOnTitle: String
        get() = getCurrentResources().commandBluetoothOnTitle
    override val commandBluetoothOnDetails: String
        get() = getCurrentResources().commandBluetoothOnDetails
    override val commandBluetoothOffTitle: String
        get() = getCurrentResources().commandBluetoothOffTitle
    override val commandBluetoothOffDetails: String
        get() = getCurrentResources().commandBluetoothOffDetails
    override val commandLocationOnTitle: String
        get() = getCurrentResources().commandLocationOnTitle
    override val commandLocationOnDetails: String
        get() = getCurrentResources().commandLocationOnDetails
    override val commandLocationOffTitle: String
        get() = getCurrentResources().commandLocationOffTitle
    override val commandLocationOffDetails: String
        get() = getCurrentResources().commandLocationOffDetails
    override val commandAnimationsOnTitle: String
        get() = getCurrentResources().commandAnimationsOnTitle
    override val commandAnimationsOnDetails: String
        get() = getCurrentResources().commandAnimationsOnDetails
    override val commandAnimationsOffTitle: String
        get() = getCurrentResources().commandAnimationsOffTitle
    override val commandAnimationsOffDetails: String
        get() = getCurrentResources().commandAnimationsOffDetails
    override val commandAutoRotateOnTitle: String
        get() = getCurrentResources().commandAutoRotateOnTitle
    override val commandAutoRotateOnDetails: String
        get() = getCurrentResources().commandAutoRotateOnDetails
    override val commandAutoRotateOffTitle: String
        get() = getCurrentResources().commandAutoRotateOffTitle
    override val commandAutoRotateOffDetails: String
        get() = getCurrentResources().commandAutoRotateOffDetails
    override val commandFontScaleSmallTitle: String
        get() = getCurrentResources().commandFontScaleSmallTitle
    override val commandFontScaleSmallDetails: String
        get() = getCurrentResources().commandFontScaleSmallDetails
    override val commandFontScaleNormalTitle: String
        get() = getCurrentResources().commandFontScaleNormalTitle
    override val commandFontScaleNormalDetails: String
        get() = getCurrentResources().commandFontScaleNormalDetails
    override val commandFontScaleLargeTitle: String
        get() = getCurrentResources().commandFontScaleLargeTitle
    override val commandFontScaleLargeDetails: String
        get() = getCurrentResources().commandFontScaleLargeDetails
    override val commandFontScaleHugeTitle: String
        get() = getCurrentResources().commandFontScaleHugeTitle
    override val commandFontScaleHugeDetails: String
        get() = getCurrentResources().commandFontScaleHugeDetails
    override val commandFontScaleExtraLargeTitle: String
        get() = getCurrentResources().commandFontScaleExtraLargeTitle
    override val commandFontScaleExtraLargeDetails: String
        get() = getCurrentResources().commandFontScaleExtraLargeDetails
    override val commandFontScaleMaximumTitle: String
        get() = getCurrentResources().commandFontScaleMaximumTitle
    override val commandFontScaleMaximumDetails: String
        get() = getCurrentResources().commandFontScaleMaximumDetails
    override val commandRtlLayoutOnTitle: String
        get() = getCurrentResources().commandRtlLayoutOnTitle
    override val commandRtlLayoutOnDetails: String
        get() = getCurrentResources().commandRtlLayoutOnDetails
    override val commandRtlLayoutOffTitle: String
        get() = getCurrentResources().commandRtlLayoutOffTitle
    override val commandRtlLayoutOffDetails: String
        get() = getCurrentResources().commandRtlLayoutOffDetails
    override val commandBatterySaverOnTitle: String
        get() = getCurrentResources().commandBatterySaverOnTitle
    override val commandBatterySaverOnDetails: String
        get() = getCurrentResources().commandBatterySaverOnDetails
    override val commandBatterySaverOffTitle: String
        get() = getCurrentResources().commandBatterySaverOffTitle
    override val commandBatterySaverOffDetails: String
        get() = getCurrentResources().commandBatterySaverOffDetails
    override val commandDataSaverOnTitle: String
        get() = getCurrentResources().commandDataSaverOnTitle
    override val commandDataSaverOnDetails: String
        get() = getCurrentResources().commandDataSaverOnDetails
    override val commandDataSaverOffTitle: String
        get() = getCurrentResources().commandDataSaverOffTitle
    override val commandDataSaverOffDetails: String
        get() = getCurrentResources().commandDataSaverOffDetails
    override val commandDozeModeOnTitle: String
        get() = getCurrentResources().commandDozeModeOnTitle
    override val commandDozeModeOnDetails: String
        get() = getCurrentResources().commandDozeModeOnDetails
    override val commandDozeModeOffTitle: String
        get() = getCurrentResources().commandDozeModeOffTitle
    override val commandDozeModeOffDetails: String
        get() = getCurrentResources().commandDozeModeOffDetails
    override val commandScreenRotation0Title: String
        get() = getCurrentResources().commandScreenRotation0Title
    override val commandScreenRotation0Details: String
        get() = getCurrentResources().commandScreenRotation0Details
    override val commandScreenRotation90Title: String
        get() = getCurrentResources().commandScreenRotation90Title
    override val commandScreenRotation90Details: String
        get() = getCurrentResources().commandScreenRotation90Details
    override val commandScreenRotation180Title: String
        get() = getCurrentResources().commandScreenRotation180Title
    override val commandScreenRotation180Details: String
        get() = getCurrentResources().commandScreenRotation180Details
    override val commandScreenRotation270Title: String
        get() = getCurrentResources().commandScreenRotation270Title
    override val commandScreenRotation270Details: String
        get() = getCurrentResources().commandScreenRotation270Details

    override val categoryUI: String
        get() = getCurrentResources().categoryUI
    override val categoryCommunication: String
        get() = getCurrentResources().categoryCommunication
    override val categoryAll: String
        get() = getCurrentResources().categoryAll

    override val outputTerminalTitle: String
        get() = getCurrentResources().outputTerminalTitle
    override val outputTerminalPlaceholder: String
        get() = getCurrentResources().outputTerminalPlaceholder

    override val textCommandStartEventFormat: String
        get() = getCurrentResources().textCommandStartEventFormat
    override val textCommandEndEventFormat: String
        get() = getCurrentResources().textCommandEndEventFormat
    override val textCommandErrorEventFormat: String
        get() = getCurrentResources().textCommandErrorEventFormat

    override val keyCommandStartEventFormat: String
        get() = getCurrentResources().keyCommandStartEventFormat
    override val keyCommandEndEventFormat: String
        get() = getCurrentResources().keyCommandEndEventFormat
    override val keyCommandErrorEventFormat: String
        get() = getCurrentResources().keyCommandErrorEventFormat

    override val screenshotCommandStartEventFormat: String
        get() = getCurrentResources().screenshotCommandStartEventFormat
    override val screenshotCommandEndEventFormat: String
        get() = getCurrentResources().screenshotCommandEndEventFormat
    override val screenshotCommandErrorEventFormat: String
        get() = getCurrentResources().screenshotCommandErrorEventFormat
    override val screenshotCopyToClipbaordEventFormat: String
        get() = getCurrentResources().screenshotCopyToClipbaordEventFormat
    override val cantScreenshotCopyToClipbaordEventFormat: String
        get() = getCurrentResources().cantScreenshotCopyToClipbaordEventFormat
    override val screenshotClearCache: String
        get() = getCurrentResources().screenshotClearCache

    override val menuCommandTitle: String
        get() = getCurrentResources().menuCommandTitle
    override val menuInputTextTitle: String
        get() = getCurrentResources().menuInputTextTitle
    override val menuScreenshot: String
        get() = getCurrentResources().menuScreenshot
    override val settingThemeHeader: String
        get() = getCurrentResources().settingThemeHeader
    override val settingAccentColorHeader: String
        get() = getCurrentResources().settingAccentColorHeader
    override val accentColorBlue: String
        get() = getCurrentResources().accentColorBlue
    override val accentColorPurple: String
        get() = getCurrentResources().accentColorPurple
    override val accentColorGreen: String
        get() = getCurrentResources().accentColorGreen
    override val accentColorOrange: String
        get() = getCurrentResources().accentColorOrange
    override val accentColorRed: String
        get() = getCurrentResources().accentColorRed
    override val accentColorTeal: String
        get() = getCurrentResources().accentColorTeal
    override val accentColorIndigo: String
        get() = getCurrentResources().accentColorIndigo
    override val settingLanguageHeader: String
        get() = getCurrentResources().settingLanguageHeader
    override val settingLanguageEnglish: String
        get() = getCurrentResources().settingLanguageEnglish
    override val settingLanguageJapanese: String
        get() = getCurrentResources().settingLanguageJapanese
    override val settingLanguageChinese: String
        get() = getCurrentResources().settingLanguageChinese
    override val settingLanguageTurkish: String
        get() = getCurrentResources().settingLanguageTurkish
    override val settingAdbHeader: String
        get() = getCurrentResources().settingAdbHeader
    override val settingAdbDirectoryPathTitle: String
        get() = getCurrentResources().settingAdbDirectoryPathTitle
    override val settingAdbPortNumberTitle: String
        get() = getCurrentResources().settingAdbPortNumberTitle
    override val settingAdbRestartTitle: String
        get() = getCurrentResources().settingAdbRestartTitle

    override val settingScrcpyHeader: String
        get() = getCurrentResources().settingScrcpyHeader
    override val settingScrcpyBinaryPathTitle: String
        get() = getCurrentResources().settingScrcpyBinaryPathTitle

    override val settingAndroidSdkHeader: String
        get() = getCurrentResources().settingAndroidSdkHeader
    override val settingAndroidSdkDirectoryPathTitle: String
        get() = getCurrentResources().settingAndroidSdkDirectoryPathTitle
    override val adbErrorTitle: String
        get() = getCurrentResources().adbErrorTitle
    override val adbErrorMessage: String
        get() = getCurrentResources().adbErrorMessage
    override val adbErrorOpenSetting: String
        get() = getCurrentResources().adbErrorOpenSetting

    override val textCommandOptionNewLine: String
        get() = getCurrentResources().textCommandOptionNewLine
    override val textCommandOptionTab: String
        get() = getCurrentResources().textCommandOptionTab
    override val textCommandLineBreakSection: String
        get() = getCurrentResources().textCommandLineBreakSection
    override val textCommandLineBreakOptionEnter: String
        get() = getCurrentResources().textCommandLineBreakOptionEnter
    override val textCommandLineBreakOptionTab: String
        get() = getCurrentResources().textCommandLineBreakOptionTab
    override val textCommandLineBreakOptionLabel: String
        get() = getCurrentResources().textCommandLineBreakOptionLabel

    override val sortByNameAsc: String
        get() = getCurrentResources().sortByNameAsc

    override val sortByNameDesc: String
        get() = getCurrentResources().sortByNameDesc

    // TopSection tooltips
    override val tooltipRefresh: String
        get() = getCurrentResources().tooltipRefresh
    override val tooltipPower: String
        get() = getCurrentResources().tooltipPower
    override val tooltipVolumeUp: String
        get() = getCurrentResources().tooltipVolumeUp
    override val tooltipVolumeDown: String
        get() = getCurrentResources().tooltipVolumeDown
    override val tooltipVolumeMute: String
        get() = getCurrentResources().tooltipVolumeMute
    override val tooltipBack: String
        get() = getCurrentResources().tooltipBack
    override val tooltipHome: String
        get() = getCurrentResources().tooltipHome
    override val tooltipRecents: String
        get() = getCurrentResources().tooltipRecents

    // NavigationRail tooltips
    override val tooltipCommand: String
        get() = getCurrentResources().tooltipCommand
    override val tooltipText: String
        get() = getCurrentResources().tooltipText
    override val tooltipScreenshot: String
        get() = getCurrentResources().tooltipScreenshot
    override val tooltipFile: String
        get() = getCurrentResources().tooltipFile
    override val tooltipSetting: String
        get() = getCurrentResources().tooltipSetting
    override val tooltipScrcpy: String
        get() = getCurrentResources().tooltipScrcpy
    override val tooltipNewDisplay: String
        get() = getCurrentResources().tooltipNewDisplay

    // MenuBar Window menu
    override val menuWindow: String
        get() = getCurrentResources().menuWindow
    override val menuWindowMaximize: String
        get() = getCurrentResources().menuWindowMaximize
    override val menuWindowMinimize: String
        get() = getCurrentResources().menuWindowMinimize
    override val menuWindowFullscreen: String
        get() = getCurrentResources().menuWindowFullscreen
    override val menuWindowAlwaysOnTop: String
        get() = getCurrentResources().menuWindowAlwaysOnTop

    // Device Settings Screen
    override val deviceSettingsTitle: String
        get() = getCurrentResources().deviceSettingsTitle
    override val deviceNameSection: String
        get() = getCurrentResources().deviceNameSection
    override val customDeviceNameLabel: String
        get() = getCurrentResources().customDeviceNameLabel
    override val scrcpySettingsSection: String
        get() = getCurrentResources().scrcpySettingsSection

    // Scrcpy Options Section
    override val virtualDisplayOptionsSection: String
        get() = getCurrentResources().virtualDisplayOptionsSection
    override val videoOptionsSection: String
        get() = getCurrentResources().videoOptionsSection
    override val audioOptionsSection: String
        get() = getCurrentResources().audioOptionsSection
    override val displayOptionsSection: String
        get() = getCurrentResources().displayOptionsSection
    override val controlOptionsSection: String
        get() = getCurrentResources().controlOptionsSection
    override val loggingOptionsSection: String
        get() = getCurrentResources().loggingOptionsSection

    override val noVideoLabel: String
        get() = getCurrentResources().noVideoLabel
    override val noAudioLabel: String
        get() = getCurrentResources().noAudioLabel
    override val fullscreenLabel: String
        get() = getCurrentResources().fullscreenLabel
    override val alwaysOnTopLabel: String
        get() = getCurrentResources().alwaysOnTopLabel
    override val stayAwakeLabel: String
        get() = getCurrentResources().stayAwakeLabel
    override val turnScreenOffLabel: String
        get() = getCurrentResources().turnScreenOffLabel
    override val powerOffOnCloseLabel: String
        get() = getCurrentResources().powerOffOnCloseLabel
    override val showTouchesLabel: String
        get() = getCurrentResources().showTouchesLabel
    override val disableScreensaverLabel: String
        get() = getCurrentResources().disableScreensaverLabel

    override val maxSizeLabel: String
        get() = getCurrentResources().maxSizeLabel
    override val videoBitRateLabel: String
        get() = getCurrentResources().videoBitRateLabel
    override val maxFpsLabel: String
        get() = getCurrentResources().maxFpsLabel
    override val videoCodecLabel: String
        get() = getCurrentResources().videoCodecLabel
    override val videoSourceLabel: String
        get() = getCurrentResources().videoSourceLabel
    override val audioBitRateLabel: String
        get() = getCurrentResources().audioBitRateLabel
    override val audioBufferLabel: String
        get() = getCurrentResources().audioBufferLabel
    override val audioCodecLabel: String
        get() = getCurrentResources().audioCodecLabel
    override val audioSourceLabel: String
        get() = getCurrentResources().audioSourceLabel
    override val windowTitleLabel: String
        get() = getCurrentResources().windowTitleLabel
    override val displayIdLabel: String
        get() = getCurrentResources().displayIdLabel
    override val windowXLabel: String
        get() = getCurrentResources().windowXLabel
    override val windowYLabel: String
        get() = getCurrentResources().windowYLabel
    override val windowWidthLabel: String
        get() = getCurrentResources().windowWidthLabel
    override val windowHeightLabel: String
        get() = getCurrentResources().windowHeightLabel
    override val virtualDisplayWidthLabel: String
        get() = getCurrentResources().virtualDisplayWidthLabel
    override val virtualDisplayHeightLabel: String
        get() = getCurrentResources().virtualDisplayHeightLabel
    override val virtualDisplayDpiLabel: String
        get() = getCurrentResources().virtualDisplayDpiLabel
    override val logLevelLabel: String
        get() = getCurrentResources().logLevelLabel

    override val autoLabel: String
        get() = getCurrentResources().autoLabel
    override val defaultLabel: String
        get() = getCurrentResources().defaultLabel
    override val customTitlePlaceholder: String
        get() = getCurrentResources().customTitlePlaceholder
    override val screenshotNamePlaceholder: String
        get() = getCurrentResources().screenshotNamePlaceholder
    override val invalidCharactersMessage: String
        get() = getCurrentResources().invalidCharactersMessage
    override val fileNameDuplicateMessage: String
        get() = getCurrentResources().fileNameDuplicateMessage

    // Setting Categories
    override val categoryAppearance: String
        get() = getCurrentResources().categoryAppearance
    override val categorySDK: String
        get() = getCurrentResources().categorySDK

    // Device Setting Categories
    override val categoryDevice: String
        get() = getCurrentResources().categoryDevice
    override val categoryScrcpy: String
        get() = getCurrentResources().categoryScrcpy

    override val scrcpyNewDisplayProfiles: String
        get() = getCurrentResources().scrcpyNewDisplayProfiles
    override val scrcpyNewDisplayOverview: String
        get() = getCurrentResources().scrcpyNewDisplayOverview
    override val scrcpyNewDisplayResolution: String
        get() = getCurrentResources().scrcpyNewDisplayResolution
    override val scrcpyNewDisplayDensity: String
        get() = getCurrentResources().scrcpyNewDisplayDensity
    override val scrcpyNewDisplayDensityUnknown: String
        get() = getCurrentResources().scrcpyNewDisplayDensityUnknown
    override val scrcpyNewDisplayRefresh: String
        get() = getCurrentResources().scrcpyNewDisplayRefresh
    override val scrcpyNewDisplayRefreshUnknown: String
        get() = getCurrentResources().scrcpyNewDisplayRefreshUnknown
    override val scrcpyNewDisplayPanel: String
        get() = getCurrentResources().scrcpyNewDisplayPanel
    override val scrcpyNewDisplayNoDevice: String
        get() = getCurrentResources().scrcpyNewDisplayNoDevice
    override val scrcpyNewDisplayLaunchSuccess: String
        get() = getCurrentResources().scrcpyNewDisplayLaunchSuccess
    override val scrcpyNewDisplayLaunchFailure: String
        get() = getCurrentResources().scrcpyNewDisplayLaunchFailure
    override val scrcpyNewDisplayEmpty: String
        get() = getCurrentResources().scrcpyNewDisplayEmpty
    override val scrcpyNewDisplayTitleFormat: String
        get() = getCurrentResources().scrcpyNewDisplayTitleFormat
    override val scrcpyNewDisplayTargetDeviceFormat: String
        get() = getCurrentResources().scrcpyNewDisplayTargetDeviceFormat
    override val scrcpyNewDisplayLaunchOptions: String
        get() = getCurrentResources().scrcpyNewDisplayLaunchOptions

    override val commandCategoryAll: String
        get() = getCurrentResources().commandCategoryAll
    override val commandCategoryCommunication: String
        get() = getCurrentResources().commandCategoryCommunication
    override val commandCategoryNavigation: String
        get() = getCurrentResources().commandCategoryNavigation
    override val commandCategoryTheme: String
        get() = getCurrentResources().commandCategoryTheme
    override val commandCategoryDisplay: String
        get() = getCurrentResources().commandCategoryDisplay
    override val commandCategoryDevice: String
        get() = getCurrentResources().commandCategoryDevice
    override val commandCategoryFavorite: String
        get() = getCurrentResources().commandCategoryFavorite

    fun scrcpyNewDisplayTitle(name: String): String = String.format(scrcpyNewDisplayTitleFormat, name)

    fun scrcpyNewDisplayTargetDevice(name: String): String = String.format(scrcpyNewDisplayTargetDeviceFormat, name)

    private var currentType: Type = Type.ENGLISH

    fun switch(type: Type) {
        currentType = type
    }

    private fun getCurrentResources(): StringResources =
        when (currentType) {
            Type.ENGLISH -> EnglishResources
            Type.JAPANESE -> JapaneseResources
            Type.CHINESE -> ChineseResources
            Type.TURKISH -> TurkishResources
        }

    enum class Type {
        ENGLISH,
        JAPANESE,
        CHINESE,
        TURKISH,
    }
}
