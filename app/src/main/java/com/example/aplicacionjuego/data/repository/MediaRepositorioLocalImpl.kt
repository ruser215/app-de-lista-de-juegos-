package com.example.aplicacionjuego.data.repository

import com.example.aplicacionjuego.data.datasource.ItemsMedia
import com.example.aplicacionjuego.domain.model.Categoria
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

class MediaRepositoryLocalImpl @Inject constructor(
    private val auth: FirebaseAuth
) : MediaRepository {

    private val _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())

    init {
        _mediaItems.value = ItemsMedia.juegos.map { it.toMediaItem() }
    }

    override suspend fun loginUser(email: String, pass: String): Result<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            if (result.user?.isEmailVerified == true) Result.success(true)
            else {
                auth.signOut()
                Result.failure(Exception("Debes verificar tu correo electrónico."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerUser(email: String, pass: String): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await().user?.sendEmailVerification()?.await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllItems(): Flow<List<MediaItem>> {
        return _mediaItems.asStateFlow()
    }

    override suspend fun addItem(item: MediaItem): Result<Unit> {
        val nuevoItem = item.copy(id = UUID.randomUUID().toString())
        _mediaItems.update { it + nuevoItem }
        ItemsMedia.juegos.add(nuevoItem.toJSONObject())
        return Result.success(Unit)
    }

    override suspend fun updateItem(item: MediaItem): Result<Unit> {
        _mediaItems.update { list -> list.map { if (it.id == item.id) item else it } }
        val index = ItemsMedia.juegos.indexOfFirst { it.optString("id") == item.id }
        if (index != -1) {
            ItemsMedia.juegos[index] = item.toJSONObject()
        }
        return Result.success(Unit)
    }

    override suspend fun deleteItem(item: MediaItem): Result<Unit> {
        _mediaItems.update { list -> list.filter { it.id != item.id } }
        val index = ItemsMedia.juegos.indexOfFirst { it.optString("id") == item.id }
        if (index != -1) {
            ItemsMedia.juegos.removeAt(index)
        }
        return Result.success(Unit)
    }
}

private fun JSONObject.toMediaItem(): MediaItem {
    return MediaItem(
        id = this.optString("id"),
        title = this.optString("title"),
        platform = this.optString("platform"),
        portada = this.optString("portada"),
        estado = Estado.valueOf(this.optString("estado", Estado.PENDIENTE.name)),
        rating = this.optDouble("rating", 0.0).toFloat(),
        opinion = this.optString("opinion"),
        categoria = Categoria.valueOf(this.optString("categoria", Categoria.JUEGO.name))
    )
}