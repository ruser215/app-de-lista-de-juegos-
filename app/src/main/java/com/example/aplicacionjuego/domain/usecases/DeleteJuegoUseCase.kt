package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.domain.repository.JuegoRepositorio
import javax.inject.Inject

class DeleteJuegoUseCase @Inject constructor(
    private val repository: JuegoRepositorio
) {
    suspend operator fun invoke(juego: Juego): Result<Unit> {
        return repository.deleteJuego(juego)
    }
}