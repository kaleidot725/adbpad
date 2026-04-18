package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem

interface TimeEditItemRepository {
    suspend fun getAllTimeEditItems(): List<TimeEditItem>

    suspend fun addTimeEditItem(item: TimeEditItem): Boolean

    suspend fun updateTimeEditItem(item: TimeEditItem): Boolean

    suspend fun removeTimeEditItem(itemId: String): Boolean
}
