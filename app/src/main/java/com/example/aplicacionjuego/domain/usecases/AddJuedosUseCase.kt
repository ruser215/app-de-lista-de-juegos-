package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.domain.repository.JuegoRepositorio
import javax.inject.Inject

class AddJuedosUseCase @Inject constructor(
    private val repository: JuegoRepositorio
){
    suspend operator fun invoke(juego: Juego): Result<Unit> {
        // Aquí podrías validar, por ejemplo, que el título no esté vacío
        if (juego.title.isBlank()) {
            return Result.failure(Exception("El título del juego no puede estar vacío"))
        }
        return repository.addJuego(juego)
    }
}