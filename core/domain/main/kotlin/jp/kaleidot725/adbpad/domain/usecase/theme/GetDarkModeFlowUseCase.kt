package jp.kaleidot725.adbpad.domain.usecase.theme

import com.jthemedetecor.OsThemeDetector
import jp.kaleidot725.adbpad.domain.model.setting.Appearance
import jp.kaleidot725.adbpad.domain.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.function.Consumer

class GetDarkModeFlowUseCase(
    private val settingRepository: SettingRepository,
) {
    suspend operator fun invoke(): Flow<Boolean> =
        when (withContext(Dispatchers.IO) { settingRepository.getAppearance() }) {
            Appearance.DARK -> flow { emit(true) }
            Appearance.LIGHT -> flow { emit(false) }
            Appearance.SYSTEM -> {
                val detector = OsThemeDetector.getDetector()
                callbackFlow {
                    this.trySend(detector.isDark)
                    val listener: Consumer<Boolean> =
                        Consumer<Boolean> { isDark ->
                            this@callbackFlow.trySend(isDark)
                        }
                    detector.registerListener(listener)
                    awaitClose { detector.removeListener(listener) }
                }
            }
        }
}
