package jp.kaleidot725.adbpad.di

import jp.kaleidot725.adbpad.data.local.ScrcpyNewDisplayProfileFileCreator
import jp.kaleidot725.adbpad.data.repository.DeviceSettingsRepositoryImpl
import jp.kaleidot725.adbpad.data.repository.NormalCommandFavoriteRepositoryImpl
import jp.kaleidot725.adbpad.data.repository.ScrcpyNewDisplayProfileRepositoryImpl
import jp.kaleidot725.adbpad.data.repository.SettingRepositoryImpl
import jp.kaleidot725.adbpad.data.repository.TextCommandRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.DeviceControlCommandRepository
import jp.kaleidot725.adbpad.domain.repository.DeviceControlCommandRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.DeviceRepository
import jp.kaleidot725.adbpad.domain.repository.DeviceRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.DeviceSettingsRepository
import jp.kaleidot725.adbpad.domain.repository.NormalCommandFavoriteRepository
import jp.kaleidot725.adbpad.domain.repository.NormalCommandOutputRepository
import jp.kaleidot725.adbpad.domain.repository.NormalCommandOutputRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.NormalCommandRepository
import jp.kaleidot725.adbpad.domain.repository.NormalCommandRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProcessRepository
import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProcessRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.ScrcpyNewDisplayProfileRepository
import jp.kaleidot725.adbpad.domain.repository.ScrcpyProcessRepository
import jp.kaleidot725.adbpad.domain.repository.ScrcpyProcessRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.ScreenshotCommandRepository
import jp.kaleidot725.adbpad.domain.repository.ScreenshotCommandRepositoryImpl
import jp.kaleidot725.adbpad.domain.repository.SettingRepository
import jp.kaleidot725.adbpad.domain.repository.TextCommandRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single { ScrcpyNewDisplayProfileFileCreator() }
        single<ScrcpyNewDisplayProfileRepository> { ScrcpyNewDisplayProfileRepositoryImpl(get()) }
        single<DeviceRepository> {
            DeviceRepositoryImpl(get())
        }
        single<NormalCommandOutputRepository> {
            NormalCommandOutputRepositoryImpl()
        }
        single<NormalCommandRepository> {
            NormalCommandRepositoryImpl()
        }
        single<NormalCommandFavoriteRepository> {
            NormalCommandFavoriteRepositoryImpl()
        }
        single<TextCommandRepository> {
            TextCommandRepositoryImpl()
        }
        single<ScreenshotCommandRepository> {
            ScreenshotCommandRepositoryImpl()
        }
        factory<SettingRepository> {
            SettingRepositoryImpl()
        }
        factory<DeviceControlCommandRepository> {
            DeviceControlCommandRepositoryImpl()
        }
        single<ScrcpyProcessRepository> {
            ScrcpyProcessRepositoryImpl()
        }
        single<ScrcpyNewDisplayProcessRepository> {
            ScrcpyNewDisplayProcessRepositoryImpl()
        }
        factory<DeviceSettingsRepository> {
            DeviceSettingsRepositoryImpl()
        }
    }
