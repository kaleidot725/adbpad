package jp.kaleidot725.adbpad.domain.model.language.resources

object JapaneseResources : StringResources {
    override val version = "v1.1.0"
    override val windowTitle = "AdbPad($version)"

    override val notFoundDevice = "デバイスがありません"
    override val notFoundCommand = "コマンドがありません"
    override val notFoundInputText = "テキストがありません"
    override val notFoundScreenshot = "スクリーンショットがありません"

    override val execute = "実行"
    override val save = "保存"
    override val delete = "削除"
    override val send = "送信"
    override val cancel = "キャンセル"
    override val targetDevice = "端末"
    override val tool = "ツール"
    override val setting = "設定　"
    override val dark = "Dark"
    override val light = "Light"
    override val system = "System"

    override val screenshotTakeByCurrentTheme = "現在のテーマで撮影する"
    override val screenshotTakeByDarkTheme = "ダークテーマで撮影する"
    override val screenshotTakeByLightTheme = "ライトテーマで撮影する"
    override val screenshotTakeByBothTheme = "両方のテーマで撮影する"

    override val commandStartEventFormat = "「%s」のコマンド送信を開始しました"
    override val commandEndEventFormat = "「%s」のコマンド送信が完了しました"
    override val commandErrorEventFormat = "「%s」のコマンド送信に失敗しました"

    override val commandLayoutBorderOnTitle = "レイアウト境界表示: ON"
    override val commandLayoutBorderOnDetails = "レイアウトの境界やマージンなどの表示を有効化します"
    override val commandLayoutBorderOffTitle = "レイアウト境界表示: OFF"
    override val commandLayoutBorderOffDetails = "レイアウトの境界やマージンなどの表示を無効化します"
    override val commandTapEffectOnTitle = "タップ表示: ON"
    override val commandTapEffectOnDetails = "タップ表示を有効化します"
    override val commandTapEffectOffTitle = "タップ表示: OFF"
    override val commandTapEffectOffDetails = "タップ表示を無効化します"
    override val commandSleepModeOnTitle = "スリープモード: ON"
    override val commandSleepModeOnDetails = "端末がスリープモードに移行可能にする"
    override val commandSleepModeOffTitle = "スリープモード: OFF"
    override val commandSleepModeOffDetails = "端末がスリープモードに移行不可能にする"
    override val commandDarkThemeOnTitle = "ダークテーマ: ON"
    override val commandDarkThemeOnDetails = "端末のテーマをダークテーマにする"
    override val commandDarkThemeOffTitle = "ダークテーマ: OFF"
    override val commandDarkThemeOffDetails = "端末のテーマをライトテーマにする"
    override val commandWifiOnTitle = "Wi-Fi: ON"
    override val commandWifiOnDetails = "Wi-Fi通信を有効化する"
    override val commandWifiOffTitle = "Wi-Fi: OFF"
    override val commandWifiOffDetails = "Wi-Fi通信を無効化する"
    override val commandDataOnTitle = "モバイル通信: ON"
    override val commandDataOnDetails = "モバイル通信を有効化する"
    override val commandDataOffTitle = "モバイル通信: OFF"
    override val commandDataOffDetails = "モバイル通信を無効化する"
    override val commandWifiAndDataOnTitle = "Wi-Fi&モバイル通信: ON"
    override val commandWifiAndDataOnDetails = "Wi-Fi通信とモバイル通信の両方を有効化する"
    override val commandWifiAndDataOffTitle = "Wi-Fi&モバイル通信: OFF"
    override val commandWifiAndDataOffDetails = "Wi-Fi通信とモバイル通信の両方を無効化する"
    override val commandScreenPinningOffTitle = "画面ピン留め: OFF"
    override val commandScreenPinningOffDetails = "ピン留め中のアプリのピン留めを解除する"

    override val textCommandStartEventFormat = "「%s」のテキスト送信を開始しました"
    override val textCommandEndEventFormat = "「%s」のテキスト送信が完了しました"
    override val textCommandErrorEventFormat = "「%s」のテキスト送信に失敗しました"

    override val screenshotCommandStartEventFormat = "スクリーンショットの撮影を開始しました"
    override val screenshotCommandEndEventFormat = "スクリーンショットの撮影が完了しました"
    override val screenshotCommandErrorEventFormat = "スクリーンショットの撮影に失敗しました"
    override val screenshotCopyToClipbaordEventFormat: String = "スクリーンショットをクリップボードにコピーしました"
    override val cantScreenshotCopyToClipbaordEventFormat: String = "スクリーンショットをクリップボードにコピーできません"
    override val screenshotClearCache: String = "スクリーンショットを削除しました"

    override val menuCommandTitle = "コマンド"
    override val menuInputTextTitle = "テキスト送信"
    override val menuScreenshot = "スクリーンショット"

    override val settingLanguageHeader = "表示言語"
    override val settingLanguageEnglish = "英語(English)"
    override val settingLanguageJapanese = "日本語"

    override val settingAppearanceHeader = "テーマ"
    override val settingAdbHeader = "ADB"
    override val settingAdbDirectoryPathTitle = "Binary Path"
    override val settingAdbPortNumberTitle = "Server Port"
    override val settingAndroidSdkHeader = "Android SDK"
    override val settingAndroidSdkDirectoryPathTitle = "Directory Path"

    override val adbErrorTitle = "ADBエラー"
    override val adbErrorMessage = "ADBサーバーを開始できませんでした、ADBの設定を変更してください"
    override val adbErrorOpenSetting = "設定を開く"
}
