package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Caso de Uso para eliminar un ítem de la colección.
 * Al igual que el de actualizar, es un intermediario simple pero importante para mantener
 * una arquitectura limpia y coherente.
 */
class DeleteItemUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    /**
     * La sobrecarga del operador `invoke` permite llamar a la clase como si fuera una función.
     *
     * @param item El `MediaItem` que se va a eliminar.
     * @return Un `Result` que indica si la operación fue exitosa.
     */
    suspend operator fun invoke(item: MediaItem): Result<Unit> {
        return repository.deleteItem(item)
    }
}