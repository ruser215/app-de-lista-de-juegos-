package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Caso de Uso para actualizar un ítem existente.
 * Su responsabilidad es muy simple: actuar como un intermediario claro entre el ViewModel y el Repositorio.
 * Aunque no contiene lógica de negocio compleja, mantenerlo como un caso de uso separado
 * ayuda a mantener la consistencia de la arquitectura.
 */
class UpdateItemUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    /**
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param item El objeto `MediaItem` con los datos ya actualizados.
     * @return Un `Result` que indica si la operación fue exitosa.
     */
    suspend operator fun invoke(item: MediaItem): Result<Unit> {
        // Simplemente delega la llamada al repositorio.
        return repository.updateItem(item)
    }
}