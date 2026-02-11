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

/**
 * Esta es la implementación concreta de nuestra interfaz `MediaRepository`.
 * Es la clase que realmente "hace el trabajo" que el contrato (la interfaz) promete.
 * En nuestro caso, tiene una doble responsabilidad:
 *  1. Gestionar la autenticación de usuarios usando Firebase.
 *  2. Gestionar una lista de ítems (juegos, películas...) de forma local, en memoria.
 */
class MediaRepositoryLocalImpl @Inject constructor(
    private val auth: FirebaseAuth // Inyectamos la dependencia de Firebase Auth.
) : MediaRepository {

    /**
     * `_mediaItems` es la "fuente de la verdad" para los datos de los ítems.
     * Es un `MutableStateFlow`, lo que significa que es un flujo de datos que emite su estado actual
     * a cualquier colector y puede ser modificado.
     * Es `private` para que solo el repositorio pueda modificar la lista original.
     */
    private val _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())

    init {
        // Cuando se crea una instancia de este repositorio (al arrancar la app gracias a Hilt),
        // se cargan los datos iniciales desde la clase `ItemsMedia`.
        _mediaItems.value = ItemsMedia.juegos.map { it.toMediaItem() }
    }

    // --- SECCIÓN DE AUTENTICACIÓN (Delegada a Firebase) ---

    override suspend fun loginUser(email: String, pass: String): Result<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await() // Usamos await() para trabajar con corrutinas
            if (result.user?.isEmailVerified == true) {
                Result.success(true)
            } else {
                auth.signOut() // Si el email no está verificado, cerramos sesión
                Result.failure(Exception("Debes verificar tu correo electrónico."))
            }
        } catch (e: Exception) {
            Result.failure(e) // Capturamos cualquier excepción de Firebase (ej: contraseña incorrecta)
        }
    }

    override suspend fun registerUser(email: String, pass: String): Result<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            result.user?.sendEmailVerification()?.await() // Enviamos el email de verificación
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e) // Capturamos cualquier excepción (ej: email ya en uso)
        }
    }

    // --- SECCIÓN DE GESTIÓN DE ITEMS (Local) ---

    override fun getAllItems(): Flow<List<MediaItem>> {
        // Exponemos la lista como un `Flow` inmutable para que la capa superior (ViewModel)
        // no pueda modificarla directamente, solo observarla.
        return _mediaItems.asStateFlow()
    }

    override suspend fun addItem(item: MediaItem): Result<Unit> {
        val nuevoItem = item.copy(id = UUID.randomUUID().toString())
        _mediaItems.update { it + nuevoItem } // `update` es una forma segura de modificar el StateFlow
        ItemsMedia.juegos.add(nuevoItem.toJSONObject()) // También actualizamos la fuente de datos "original"
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

/**
 * Función de extensión privada para convertir un `JSONObject` en un `MediaItem`.
 * Esto es parte de la "traducción" entre la capa de datos y la de dominio.
 * Es `private` porque solo el repositorio necesita saber cómo hacer esta conversión.
 */
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