package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Caso de Uso para el inicio de sesión de un usuario.
 * Encapsula la lógica de negocio asociada al login.
 */
class LoginUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    /**
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param email El email del usuario.
     * @param pass La contraseña del usuario.
     * @return Un `Result` que indica si el login fue exitoso.
     */
    suspend operator fun invoke(email: String, pass: String): Result<Boolean> {
        // En un futuro, se podrían añadir aquí validaciones de formato de email o contraseña.
        // Por ahora, simplemente delega la llamada al repositorio.
        return repository.loginUser(email, pass)
    }
}