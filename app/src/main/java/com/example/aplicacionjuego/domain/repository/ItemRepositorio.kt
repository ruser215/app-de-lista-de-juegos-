package com.example.aplicacionjuego.domain.repository

import com.example.aplicacionjuego.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

/**
 * Esta es la interfaz del Repositorio. Define un "contrato" que cualquier clase que gestione
 * los datos de la aplicación debe cumplir. Es una pieza clave en la arquitectura limpia (Clean Architecture).
 *
 * La ventaja de tener esta interfaz es que la capa de dominio (y por tanto, los ViewModels)
 * no necesita saber DE DÓNDE vienen los datos (Firebase, una base de datos local, un archivo...).
 * Solo necesita saber QUÉ operaciones se pueden realizar.
 */
interface MediaRepository {

    // --- MÉTODOS DE AUTENTICACIÓN ---

    /**
     * Intenta iniciar sesión con un email y contraseña.
     * @return Un `Result` que envuelve un `Boolean`. Es `success(true)` si el login es correcto,
     * o `failure(Exception)` si algo ha ido mal (contraseña incorrecta, usuario no existe, etc.).
     */
    suspend fun loginUser(email: String, pass: String): Result<Boolean>

    /**
     * Registra un nuevo usuario con un email y contraseña.
     * @return Un `Result` que indica si el registro se ha completado con éxito.
     */
    suspend fun registerUser(email: String, pass: String): Result<Boolean>

    // --- MÉTODOS DE GESTIÓN DE ITEMS ---

    /**
     * Obtiene todos los ítems de la colección.
     * @return Un `Flow<List<MediaItem>>`. Se usa Flow porque es un stream de datos asíncrono.
     * La UI se puede "suscribir" a este flujo y se actualizará automáticamente cada vez que haya cambios
     * en la lista de ítems (al añadir, borrar o actualizar uno).
     */
    fun getAllItems(): Flow<List<MediaItem>>

    /**
     * Añade un nuevo ítem a la colección.
     * @return Un `Result` que indica si la operación ha sido exitosa.
     */
    suspend fun addItem(item: MediaItem): Result<Unit>

    /**
     * Actualiza un ítem existente en la colección.
     * @return Un `Result` que indica si la operación ha sido exitosa.
     */
    suspend fun updateItem(item: MediaItem): Result<Unit>

    /**
     * Elimina un ítem de la colección.
     * @return Un `Result` que indica si la operación ha sido exitosa.
     */
    suspend fun deleteItem(item: MediaItem): Result<Unit>
}