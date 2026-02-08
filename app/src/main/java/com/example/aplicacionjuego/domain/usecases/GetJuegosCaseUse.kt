package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.domain.repository.JuegoRepositorio
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(
    private val repository: JuegoRepositorio
) {
    suspend operator fun invoke(): Flow<List<Juego>> {
        return repository.getAllJuegos()
    }
}