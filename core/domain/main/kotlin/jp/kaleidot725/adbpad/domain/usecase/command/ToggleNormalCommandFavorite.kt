package jp.kaleidot725.adbpad.domain.usecase.command

import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.repository.NormalCommandFavoriteRepository

class ToggleNormalCommandFavorite(
    private val normalCommandFavoriteRepository: NormalCommandFavoriteRepository,
) {
    suspend operator fun invoke(command: NormalCommand): Boolean =
        if (command.isFavorite) {
            normalCommandFavoriteRepository.delete(command)
        } else {
            normalCommandFavoriteRepository.save(command)
        }
}
