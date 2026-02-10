package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<Boolean> {
        // Aquí podrías validar que la contraseña tenga más de 6 caracteres antes de ir a Firebase
        if (pass.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        return repository.registerUser(email, pass)
    }
}