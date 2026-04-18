package jp.kaleidot725.adbpad.data.repository

import jp.kaleidot725.adbpad.data.local.TimeEditItemFileCreator
import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem
import jp.kaleidot725.adbpad.domain.repository.TimeEditItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TimeEditItemRepositoryImpl : TimeEditItemRepository {
    private val lock = Any()

    override suspend fun getAllTimeEditItems(): List<TimeEditItem> =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                TimeEditItemFileCreator.load().values
            }
        }

    override suspend fun addTimeEditItem(item: TimeEditItem): Boolean =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TimeEditItemFileCreator.load()
                val newSetting = oldSetting.copy(values = oldSetting.values + item)
                TimeEditItemFileCreator.save(newSetting)
            }
        }

    override suspend fun updateTimeEditItem(item: TimeEditItem): Boolean =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TimeEditItemFileCreator.load()
                val targetIndex = oldSetting.values.indexOfFirst { it.id == item.id }
                if (targetIndex < 0) return@withContext false

                val newItems = oldSetting.values.toMutableList()
                newItems[targetIndex] = item.copy(lastModified = System.currentTimeMillis())
                TimeEditItemFileCreator.save(oldSetting.copy(values = newItems))
            }
        }

    override suspend fun removeTimeEditItem(itemId: String): Boolean =
        withContext(Dispatchers.IO) {
            synchronized(lock) {
                val oldSetting = TimeEditItemFileCreator.load()
                val newItems = oldSetting.values.filterNot { it.id == itemId }
                if (newItems.size == oldSetting.values.size) return@withContext false

                TimeEditItemFileCreator.save(oldSetting.copy(values = newItems))
            }
        }
}
