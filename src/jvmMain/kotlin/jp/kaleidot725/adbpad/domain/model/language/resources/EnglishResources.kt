package jp.kaleidot725.adbpad.domain.model.language.resources

object EnglishResources : StringResources {
    override val version = "v1.1.0"
    override val windowTitle = "AdbPad($version)"

    override val notFoundDevice = "Not found device"
    override val notFoundCommand = "Not found command"
    override val notFoundInputText = "Not found input text"
    override val notFoundScreenshot = "Not found screenshot"

    override val execute = "Run"
    override val save = "Save"
    override val delete = "Delete"
    override val tab = "Tab"
    override val send = "Send"
    override val cancel = "Cancel"
    override val targetDevice = "Devices"
    override val tool = "Tools"
    override val setting = "Setting"
    override val dark = "Dark"
    override val light = "Light"
    override val system = "System"

    override val screenshotTakeByCurrentTheme = "Take by current theme"
    override val screenshotTakeByDarkTheme = "Take by dark theme"
    override val screenshotTakeByLightTheme = "Take by light theme"
    override val screenshotTakeByBothTheme = "Take by both theme"

    override val commandStartEventFormat = "Start sending command 「%s」"
    override val commandEndEventFormat = "End sending command 「%s」"
    override val commandErrorEventFormat = "Error sending command 「%s」"

    override val commandLayoutBorderOnTitle = "Show layout bounds: ON"
    override val commandLayoutBorderOnDetails = "Enable showing clip bounds, margins, etc."
    override val commandLayoutBorderOffTitle = "Show layout bonds: OFF"
    override val commandLayoutBorderOffDetails = "Disable showing clip bounds, margins, etc."
    override val commandTapEffectOnTitle = "Show taps: ON"
    override val commandTapEffectOnDetails = "Enable showing visual feedback for taps."
    override val commandTapEffectOffTitle = "Show taps: OFF"
    override val commandTapEffectOffDetails = "Disable showing visual feedback for taps."
    override val commandSleepModeOnTitle = "Sleep mode: ON"
    override val commandSleepModeOnDetails = "Enable sleep mode and device go into sleep."
    override val commandSleepModeOffTitle = "Sleep mode: OFF"
    override val commandSleepModeOffDetails = "Disable sleep mode and device doesn't go into sleep."
    override val commandDarkThemeOnTitle = "Dark theme: ON"
    override val commandDarkThemeOnDetails = "Enable dark theme."
    override val commandDarkThemeOffTitle = "Dark theme: OFF"
    override val commandDarkThemeOffDetails = "Disable dark theme."
    override val commandWifiOnTitle = "Wi-Fi: ON"
    override val commandWifiOnDetails = "Enable Wi-Fi communication."
    override val commandWifiOffTitle = "Wi-Fi: OFF"
    override val commandWifiOffDetails = "Disable Wi-Fi communication."
    override val commandDataOnTitle = "Cellular: ON"
    override val commandDataOnDetails = "Enable cellular communication."
    override val commandDataOffTitle = "Cellular: OFF"
    override val commandDataOffDetails = "Disable cellular communication."
    override val commandWifiAndDataOnTitle = "Wi-Fi and cellular: ON"
    override val commandWifiAndDataOnDetails = "Enable Wi-Fi and cellular communication."
    override val commandWifiAndDataOffTitle = "Wi-Fi and cellular: OFF"
    override val commandWifiAndDataOffDetails = "Disable Wi-Fi and cellular communication."
    override val commandScreenPinningOffTitle = "Screen pinning: OFF"
    override val commandScreenPinningOffDetails = "Disable screen pinning."

    override val textCommandStartEventFormat = "Start sending text「%s」"
    override val textCommandEndEventFormat = "End sending text「%s」"
    override val textCommandErrorEventFormat = "Error sending text「%s」"

    override val keyCommandStartEventFormat = "Start sending key「%s」"
    override val keyCommandEndEventFormat = "End sending key「%s」"
    override val keyCommandErrorEventFormat = "Error sending key「%s」"

    override val screenshotCommandStartEventFormat = "Start taking screenshot"
    override val screenshotCommandEndEventFormat = "End taking screenshot"
    override val screenshotCommandErrorEventFormat = "Error taking screenshot"
    override val screenshotCopyToClipbaordEventFormat: String = "Copy screenshot to clipboard"
    override val cantScreenshotCopyToClipbaordEventFormat: String = "Can't copy screen to clipboard"
    override val screenshotClearCache: String = "Delete screenshot"

    override val menuCommandTitle = "Command"
    override val menuInputTextTitle = "Send Text"
    override val menuScreenshot = "Screenshot"

    override val settingLanguageHeader = "Language"
    override val settingLanguageEnglish = "English"
    override val settingLanguageJapanese = "Japanese(日本語)"

    override val settingAppearanceHeader = "Appearance"
    override val settingAdbHeader = "ADB"
    override val settingAdbDirectoryPathTitle = "Binary Path"
    override val settingAdbPortNumberTitle = "Server Port"
    override val settingAndroidSdkHeader = "Android SDK"
    override val settingAndroidSdkDirectoryPathTitle = "Directory Path"
    override val settingAdbRestartTitle: String = "Restart ADB"

    override val adbErrorTitle = "Adb Error"
    override val adbErrorMessage = "Can't start adb server, Please change adb setting."
    override val adbErrorOpenSetting = "Open Setting"
}
