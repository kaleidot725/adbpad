package jp.kaleidot725.adbpad.domain.model.language.resources

interface StringResources {
    val windowTitle: String

    val notFoundDevice: String
    val notFoundCommand: String
    val notFoundInputText: String
    val notFoundScreenshot: String
    val execute: String
    val save: String
    val delete: String
    val tab: String
    val send: String
    val cancel: String
    val open: String
    val edit: String
    val copy: String
    val properties: String
    val targetDevice: String
    val tool: String
    val setting: String
    val dark: String
    val light: String
    val system: String
    val search: String

    val textCommandUnTitle: String
    val screenshotTakeByCurrentTheme: String
    val screenshotTakeByDarkTheme: String
    val screenshotTakeByLightTheme: String
    val screenshotTakeByBothTheme: String

    val commandStartEventFormat: String
    val commandEndEventFormat: String
    val commandErrorEventFormat: String
    val commandPointerLocationOnTitle: String
    val commandPointerLocationOnDetails: String
    val commandPointerLocationOffTitle: String
    val commandPointerLocationOffDetails: String
    val commandLayoutBorderOnTitle: String
    val commandLayoutBorderOnDetails: String
    val commandLayoutBorderOffTitle: String
    val commandLayoutBorderOffDetails: String
    val commandTapEffectOnTitle: String
    val commandTapEffectOnDetails: String
    val commandTapEffectOffTitle: String
    val commandTapEffectOffDetails: String
    val commandSleepModeOnTitle: String
    val commandSleepModeOnDetails: String
    val commandSleepModeOffTitle: String
    val commandSleepModeOffDetails: String
    val commandDarkThemeOnTitle: String
    val commandDarkThemeOnDetails: String
    val commandDarkThemeOffTitle: String
    val commandDarkThemeOffDetails: String
    val commandWifiOnTitle: String
    val commandWifiOnDetails: String
    val commandWifiOffTitle: String
    val commandWifiOffDetails: String
    val commandDataOnTitle: String
    val commandDataOnDetails: String
    val commandDataOffTitle: String
    val commandDataOffDetails: String
    val commandWifiAndDataOnTitle: String
    val commandWifiAndDataOnDetails: String
    val commandWifiAndDataOffTitle: String
    val commandWifiAndDataOffDetails: String
    val commandScreenPinningOffTitle: String
    val commandScreenPinningOffDetails: String
    val commandEnableThreeButtonNavigationTitle: String
    val commandEnableThreeButtonNavigationDetails: String
    val commandEnableTwoButtonNavigationTitle: String
    val commandEnableTwoButtonNavigationDetails: String
    val commandEnableGestureNavigationTitle: String
    val commandEnableGestureNavigationDetails: String
    val commandAirplaneModeOnTitle: String
    val commandAirplaneModeOnDetails: String
    val commandAirplaneModeOffTitle: String
    val commandAirplaneModeOffDetails: String
    val commandBluetoothOnTitle: String
    val commandBluetoothOnDetails: String
    val commandBluetoothOffTitle: String
    val commandBluetoothOffDetails: String
    val commandLocationOnTitle: String
    val commandLocationOnDetails: String
    val commandLocationOffTitle: String
    val commandLocationOffDetails: String
    val commandAnimationsOnTitle: String
    val commandAnimationsOnDetails: String
    val commandAnimationsOffTitle: String
    val commandAnimationsOffDetails: String
    val commandAutoRotateOnTitle: String
    val commandAutoRotateOnDetails: String
    val commandAutoRotateOffTitle: String
    val commandAutoRotateOffDetails: String
    val commandFontScaleSmallTitle: String
    val commandFontScaleSmallDetails: String
    val commandFontScaleNormalTitle: String
    val commandFontScaleNormalDetails: String
    val commandFontScaleLargeTitle: String
    val commandFontScaleLargeDetails: String
    val commandFontScaleHugeTitle: String
    val commandFontScaleHugeDetails: String
    val commandFontScaleExtraLargeTitle: String
    val commandFontScaleExtraLargeDetails: String
    val commandFontScaleMaximumTitle: String
    val commandFontScaleMaximumDetails: String
    val commandRtlLayoutOnTitle: String
    val commandRtlLayoutOnDetails: String
    val commandRtlLayoutOffTitle: String
    val commandRtlLayoutOffDetails: String
    val commandBatterySaverOnTitle: String
    val commandBatterySaverOnDetails: String
    val commandBatterySaverOffTitle: String
    val commandBatterySaverOffDetails: String
    val commandDataSaverOnTitle: String
    val commandDataSaverOnDetails: String
    val commandDataSaverOffTitle: String
    val commandDataSaverOffDetails: String
    val commandDozeModeOnTitle: String
    val commandDozeModeOnDetails: String
    val commandDozeModeOffTitle: String
    val commandDozeModeOffDetails: String
    val commandScreenRotation0Title: String
    val commandScreenRotation0Details: String
    val commandScreenRotation90Title: String
    val commandScreenRotation90Details: String
    val commandScreenRotation180Title: String
    val commandScreenRotation180Details: String
    val commandScreenRotation270Title: String
    val commandScreenRotation270Details: String

    val categoryUI: String
    val categoryCommunication: String
    val categoryAll: String

    val outputTerminalTitle: String
    val outputTerminalPlaceholder: String

    val textCommandStartEventFormat: String
    val textCommandEndEventFormat: String
    val textCommandErrorEventFormat: String

    val keyCommandStartEventFormat: String
    val keyCommandEndEventFormat: String
    val keyCommandErrorEventFormat: String

    val screenshotCommandStartEventFormat: String
    val screenshotCommandEndEventFormat: String
    val screenshotCommandErrorEventFormat: String
    val screenshotCopyToClipbaordEventFormat: String
    val cantScreenshotCopyToClipbaordEventFormat: String
    val screenshotClearCache: String

    val menuCommandTitle: String
    val menuInputTextTitle: String
    val menuScreenshot: String

    val settingThemeHeader: String
    val settingAccentColorHeader: String
    val accentColorBlue: String
    val accentColorPurple: String
    val accentColorGreen: String
    val accentColorOrange: String
    val accentColorRed: String
    val accentColorTeal: String
    val accentColorIndigo: String
    val settingLanguageHeader: String
    val settingLanguageEnglish: String
    val settingLanguageJapanese: String
    val settingLanguageChinese: String
    val settingLanguageTurkish: String
    val settingAdbHeader: String
    val settingAdbDirectoryPathTitle: String
    val settingAdbPortNumberTitle: String
    val settingAndroidSdkHeader: String
    val settingAndroidSdkDirectoryPathTitle: String
    val settingAdbRestartTitle: String

    val settingScrcpyHeader: String
    val settingScrcpyBinaryPathTitle: String

    val adbErrorTitle: String
    val adbErrorMessage: String
    val adbErrorOpenSetting: String

    val textCommandOptionNewLine: String
    val textCommandOptionTab: String

    val textCommandLineBreakSection: String
    val textCommandLineBreakOptionEnter: String
    val textCommandLineBreakOptionTab: String
    val textCommandLineBreakOptionLabel: String

    val sortByNameAsc: String
    val sortByNameDesc: String

    // TopSection tooltips
    val tooltipRefresh: String
    val tooltipPower: String
    val tooltipVolumeUp: String
    val tooltipVolumeDown: String
    val tooltipVolumeMute: String
    val tooltipBack: String
    val tooltipHome: String
    val tooltipRecents: String

    // NavigationRail tooltips
    val tooltipCommand: String
    val tooltipText: String
    val tooltipScreenshot: String
    val tooltipFile: String
    val tooltipSetting: String
    val tooltipScrcpy: String
    val tooltipNewDisplay: String

    // MenuBar Window menu
    val menuWindow: String
    val menuWindowMaximize: String
    val menuWindowMinimize: String
    val menuWindowFullscreen: String
    val menuWindowAlwaysOnTop: String

    // Device Settings Screen
    val deviceSettingsTitle: String
    val deviceNameSection: String
    val customDeviceNameLabel: String
    val scrcpySettingsSection: String

    // Scrcpy Options Section
    val virtualDisplayOptionsSection: String
    val videoOptionsSection: String
    val audioOptionsSection: String
    val displayOptionsSection: String
    val controlOptionsSection: String
    val loggingOptionsSection: String

    val noVideoLabel: String
    val noAudioLabel: String
    val fullscreenLabel: String
    val alwaysOnTopLabel: String
    val stayAwakeLabel: String
    val turnScreenOffLabel: String
    val powerOffOnCloseLabel: String
    val showTouchesLabel: String
    val disableScreensaverLabel: String

    val maxSizeLabel: String
    val videoBitRateLabel: String
    val maxFpsLabel: String
    val videoCodecLabel: String
    val videoSourceLabel: String
    val audioBitRateLabel: String
    val audioBufferLabel: String
    val audioCodecLabel: String
    val audioSourceLabel: String
    val windowTitleLabel: String
    val displayIdLabel: String
    val windowXLabel: String
    val windowYLabel: String
    val windowWidthLabel: String
    val windowHeightLabel: String
    val virtualDisplayWidthLabel: String
    val virtualDisplayHeightLabel: String
    val virtualDisplayDpiLabel: String
    val logLevelLabel: String

    val autoLabel: String
    val defaultLabel: String
    val customTitlePlaceholder: String
    val screenshotNamePlaceholder: String
    val invalidCharactersMessage: String
    val fileNameDuplicateMessage: String

    // Setting Categories
    val categoryAppearance: String
    val categorySDK: String

    // Device Setting Categories
    val categoryDevice: String
    val categoryScrcpy: String

    // Scrcpy New Display Screen
    val scrcpyNewDisplayProfiles: String
    val scrcpyNewDisplayOverview: String
    val scrcpyNewDisplayResolution: String
    val scrcpyNewDisplayDensity: String
    val scrcpyNewDisplayDensityUnknown: String
    val scrcpyNewDisplayRefresh: String
    val scrcpyNewDisplayRefreshUnknown: String
    val scrcpyNewDisplayPanel: String
    val scrcpyNewDisplayNoDevice: String
    val scrcpyNewDisplayLaunchSuccess: String
    val scrcpyNewDisplayLaunchFailure: String
    val scrcpyNewDisplayEmpty: String
    val scrcpyNewDisplayTitleFormat: String
    val scrcpyNewDisplayTargetDeviceFormat: String
    val scrcpyNewDisplayLaunchOptions: String

    val commandCategoryAll: String
    val commandCategoryCommunication: String
    val commandCategoryNavigation: String
    val commandCategoryTheme: String
    val commandCategoryDisplay: String
    val commandCategoryDevice: String
    val commandCategoryFavorite: String
}
