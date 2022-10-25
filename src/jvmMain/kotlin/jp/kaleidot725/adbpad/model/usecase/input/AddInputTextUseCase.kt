package jp.kaleidot725.adbpad.model.usecase.input

import jp.kaleidot725.adbpad.model.data.Setting
import jp.kaleidot725.adbpad.model.service.SettingFileService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddInputTextUseCase {
    suspend operator fun invoke(text: String): Boolean {
        return withContext(Dispatchers.IO) {
            val oldSetting = SettingFileService.load() ?: Setting()
            if (oldSetting.inputTexts.any { it == text }) return@withContext true

            val newInputTexts = oldSetting.inputTexts.toMutableList().apply { add(text) }
            val newSetting = oldSetting.copy(inputTexts = newInputTexts)
            return@withContext SettingFileService.save(newSetting)
        }
    }
}
