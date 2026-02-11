package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Caso de Uso para el registro de un nuevo usuario.
 * Contiene la lógica de negocio asociada al registro, como la validación de la contraseña.
 */
class RegisterUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    /**
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param email El email con el que se registrará el usuario.
     * @param pass La contraseña para el nuevo usuario.
     * @return Un `Result` que indica si el registro fue exitoso. Devuelve `failure` si la contraseña no es válida.
     */
    suspend operator fun invoke(email: String, pass: String): Result<Boolean> {
        // Esta es una regla de negocio: la contraseña debe tener al menos 6 caracteres.
        // Colocar esta validación en el Caso de Uso asegura que se cumpla siempre, independientemente
        // de qué ViewModel o pantalla llame a esta función.
        if (pass.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        // Si la validación es correcta, delegamos la tarea de registro al repositorio.
        return repository.registerUser(email, pass)
    }
}