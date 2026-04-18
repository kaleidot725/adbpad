package jp.kaleidot725.adbpad.domain.usecase.time

import jp.kaleidot725.adbpad.domain.model.time.TimeEditItem
import jp.kaleidot725.adbpad.domain.repository.TimeEditItemRepository

class GetTimeEditItemsUseCase(
    private val timeEditItemRepository: TimeEditItemRepository,
) {
    suspend operator fun invoke(): List<TimeEditItem> = timeEditItemRepository.getAllTimeEditItems()
}
