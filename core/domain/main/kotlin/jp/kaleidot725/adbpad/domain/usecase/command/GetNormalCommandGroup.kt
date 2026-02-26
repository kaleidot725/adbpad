package jp.kaleidot725.adbpad.domain.usecase.command

import jp.kaleidot725.adbpad.domain.model.command.NormalCommandCategory
import jp.kaleidot725.adbpad.domain.model.command.NormalCommandGroup
import jp.kaleidot725.adbpad.domain.repository.NormalCommandFavoriteRepository
import jp.kaleidot725.adbpad.domain.repository.NormalCommandRepository

class GetNormalCommandGroup(
    private val normalCommandRepository: NormalCommandRepository,
    private val normalCommandFavoriteRepository: NormalCommandFavoriteRepository,
) {
    suspend operator fun invoke(): NormalCommandGroup {
        val favorites = normalCommandFavoriteRepository.getAll()
        val all =
            normalCommandRepository.getCommands().map {
                it.updateFavorite(favorites.contains(it::class.qualifiedName))
            }
        return NormalCommandGroup(
            all = all,
            communication = all.filter { it.category == NormalCommandCategory.COM },
            navigation = all.filter { it.category == NormalCommandCategory.NAVIGATION },
            theme = all.filter { it.category == NormalCommandCategory.THEME },
            display = all.filter { it.category == NormalCommandCategory.DISPLAY },
            device = all.filter { it.category == NormalCommandCategory.DEVICE },
            favorite = all.filter { it.isFavorite },
        )
    }
}
