package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<Boolean> {
        return repository.loginUser(email, pass)
    }
}