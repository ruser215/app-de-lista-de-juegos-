package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

/**
 * Caso de Uso para añadir un nuevo ítem a la colección.
 * Su única responsabilidad es gestionar la lógica de negocio asociada a la creación de un ítem.
 */
class AddItemUseCase @Inject constructor(
    private val repository: MediaRepository
){
    /**
     * La sobrecarga del operador `invoke` permite que esta clase sea llamada como una función.
     *
     * @param item El objeto `MediaItem` que se va a añadir.
     * @return Un `Result` que indica si la operación fue exitosa. Devuelve `failure` si la validación falla.
     */
    suspend operator fun invoke(item: MediaItem): Result<Unit> {
        // Esta es una regla de negocio simple: no se pueden añadir ítems sin título.
        // Colocar esta lógica aquí (en el Caso de Uso) en lugar de en el ViewModel o la Activity
        // es una buena práctica porque centraliza las reglas de negocio y las hace reutilizables y fáciles de testear.
        if (item.title.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }
        // Si la validación pasa, delegamos la tarea de guardar el ítem al repositorio.
        return repository.addItem(item)
    }
}