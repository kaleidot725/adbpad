package jp.kaleidot725.adbpad.data.repository

import jp.kaleidot725.adbpad.data.local.NormalCommandFavoriteFileCreator
import jp.kaleidot725.adbpad.domain.model.command.NormalCommand
import jp.kaleidot725.adbpad.domain.repository.NormalCommandFavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NormalCommandFavoriteRepositoryImpl : NormalCommandFavoriteRepository {
    override suspend fun save(command: NormalCommand): Boolean {
        return withContext(Dispatchers.IO) {
            val favorites = NormalCommandFavoriteFileCreator.load().toMutableList()
            val className = command::class.qualifiedName ?: return@withContext false
            if (!favorites.contains(className)) {
                favorites.add(className)
                return@withContext NormalCommandFavoriteFileCreator.save(favorites)
            }
            return@withContext false
        }
    }

    override suspend fun delete(command: NormalCommand): Boolean {
        return withContext(Dispatchers.IO) {
            val favorites = NormalCommandFavoriteFileCreator.load().toMutableList()
            val className = command::class.qualifiedName ?: return@withContext false
            if (favorites.contains(className)) {
                favorites.remove(className)
                return@withContext NormalCommandFavoriteFileCreator.save(favorites)
            }
            return@withContext false
        }
    }

    override suspend fun getAll(): List<String> {
        return withContext(Dispatchers.IO) {
            return@withContext NormalCommandFavoriteFileCreator.load()
        }
    }
}
