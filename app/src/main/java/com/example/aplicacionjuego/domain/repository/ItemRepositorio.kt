package com.example.aplicacionjuego.domain.repository

import com.example.aplicacionjuego.domain.model.MediaItem // Usamos el nuevo modelo
import kotlinx.coroutines.flow.Flow

// 1. Renombramos la interfaz
interface MediaRepository {
    // Autenticación
    suspend fun loginUser(email: String, pass: String): Result<Boolean>
    suspend fun registerUser(email: String, pass: String): Result<Boolean>

    // 2. Actualizamos los métodos para que sean genéricos
    fun getAllItems(): Flow<List<MediaItem>>
    suspend fun addItem(item: MediaItem): Result<Unit>
    suspend fun updateItem(item: MediaItem): Result<Unit>
    suspend fun deleteItem(item: MediaItem): Result<Unit>
}