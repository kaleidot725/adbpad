package jp.kaleidot725.adbpad.di

import jp.kaleidot725.adbpad.domain.usecase.adb.RestartAdbUseCase
import jp.kaleidot725.adbpad.domain.usecase.adb.StartAdbUseCase
import jp.kaleidot725.adbpad.domain.usecase.app.ShutdownAppUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.GetAccentColorUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.GetAppearanceUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.SaveAccentColorUseCase
import jp.kaleidot725.adbpad.domain.usecase.appearance.SaveAppearanceUseCase
import jp.kaleidot725.adbpad.domain.usecase.command.ExecuteCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.command.ExecuteDeviceControlCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.command.GetNormalCommandGroup
import jp.kaleidot725.adbpad.domain.usecase.command.ToggleNormalCommandFavorite
import jp.kaleidot725.adbpad.domain.usecase.device.GetSelectedDeviceFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.device.SelectDeviceUseCase
import jp.kaleidot725.adbpad.domain.usecase.device.UpdateDevicesUseCase
import jp.kaleidot725.adbpad.domain.usecase.language.GetLanguageUseCase
import jp.kaleidot725.adbpad.domain.usecase.language.SaveLanguageUseCase
import jp.kaleidot725.adbpad.domain.usecase.refresh.RefreshUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.DeleteScrcpyNewDisplayProfileUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.GetScrcpyNewDisplayProfilesUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.GetScrcpySettingsUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.LaunchScrcpyNewDisplayUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.LaunchScrcpyUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.SaveScrcpyNewDisplayProfileUseCase
import jp.kaleidot725.adbpad.domain.usecase.scrcpy.SaveScrcpySettingsUseCase
import jp.kaleidot725.adbpad.domain.usecase.screenshot.GetScreenshotCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.screenshot.RenameScreenshotUseCase
import jp.kaleidot725.adbpad.domain.usecase.screenshot.TakeScreenshotUseCase
import jp.kaleidot725.adbpad.domain.usecase.sdkpath.GetSdkPathUseCase
import jp.kaleidot725.adbpad.domain.usecase.sdkpath.SaveSdkPathUseCase
import jp.kaleidot725.adbpad.domain.usecase.text.AddTextCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.text.ExecuteTextCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.text.GetTextCommandUseCase
import jp.kaleidot725.adbpad.domain.usecase.theme.GetDarkModeFlowUseCase
import jp.kaleidot725.adbpad.domain.usecase.window.GetWindowSizeUseCase
import jp.kaleidot725.adbpad.domain.usecase.window.SaveWindowSizeUseCase
import org.koin.dsl.module

val domainModule =
    module {
        factory {
            StartAdbUseCase(get())
        }
        factory {
            RestartAdbUseCase(get())
        }
        factory {
            ExecuteCommandUseCase(get(), get())
        }
        factory {
            ExecuteDeviceControlCommandUseCase(get())
        }
        factory {
            GetNormalCommandGroup(get(), get())
        }
        factory {
            ToggleNormalCommandFavorite(get())
        }
        factory {
            GetSelectedDeviceFlowUseCase(get())
        }
        factory {
            SelectDeviceUseCase(get())
        }
        factory {
            AddTextCommandUseCase(get())
        }
        factory {
            ExecuteTextCommandUseCase(get())
        }
        factory {
            GetTextCommandUseCase(get())
        }
        factory {
            TakeScreenshotUseCase(get())
        }
        factory {
            RenameScreenshotUseCase(get())
        }
        factory {
            GetScreenshotCommandUseCase(get())
        }
        factory {
            GetWindowSizeUseCase(get())
        }
        factory {
            SaveWindowSizeUseCase(get())
        }
        factory {
            GetSdkPathUseCase(get())
        }
        factory {
            SaveSdkPathUseCase(get())
        }
        factory {
            GetScrcpySettingsUseCase(get())
        }
        factory {
            GetScrcpyNewDisplayProfilesUseCase(get())
        }
        factory {
            SaveScrcpyNewDisplayProfileUseCase(get())
        }
        factory {
            DeleteScrcpyNewDisplayProfileUseCase(get())
        }
        factory {
            SaveScrcpySettingsUseCase(get())
        }
        factory {
            LaunchScrcpyUseCase(get(), get(), get())
        }
        factory {
            LaunchScrcpyNewDisplayUseCase(get(), get())
        }
        factory {
            GetAppearanceUseCase(get())
        }
        factory {
            SaveAppearanceUseCase(get())
        }
        factory {
            GetAccentColorUseCase(get())
        }
        factory {
            SaveAccentColorUseCase(get())
        }
        factory {
            GetDarkModeFlowUseCase(get())
        }
        factory {
            SaveLanguageUseCase(get())
        }
        factory {
            GetLanguageUseCase(get())
        }
        factory {
            RefreshUseCase(get(), get(), get())
        }
        factory {
            UpdateDevicesUseCase(get())
        }
        factory {
            ShutdownAppUseCase(get(), get())
        }
    }
