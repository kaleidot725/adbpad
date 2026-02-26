package jp.kaleidot725.adbpad.domain.repository

import jp.kaleidot725.adbpad.domain.model.command.NormalCommand

interface NormalCommandFavoriteRepository {
    suspend fun save(command: NormalCommand): Boolean

    suspend fun delete(command: NormalCommand): Boolean

    suspend fun getAll(): List<String>
}
