package com.example.aplicacionjuego.domain.repository

import com.example.aplicacionjuego.domain.model.Juego
import kotlinx.coroutines.flow.Flow

interface JuegoRepositorio {
    // Autenticación
    suspend fun loginUser(email: String, pass: String): Result<Boolean>
    suspend fun registerUser(email: String, pass: String): Result<Boolean>

    // Funcionalidad de Juegos
    suspend fun addJuego(Juego: Juego): Result<Unit>
    fun getAllJuegos(): Flow<List<Juego>>
    suspend fun updateJuego(juego: Juego): Result<Unit>
}