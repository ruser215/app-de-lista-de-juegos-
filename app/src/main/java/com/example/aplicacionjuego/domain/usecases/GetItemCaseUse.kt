package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de Uso para obtener todos los ítems (juegos, películas, series) del repositorio.
 * Los Casos de Uso son una buena práctica en arquitecturas limpias (Clean Architecture) porque
 * encapsulan una única regla de negocio o acción, haciendo que el código sea más reutilizable y fácil de testear.
 */
class GetAllItemsUseCase @Inject constructor(
    // Hilt inyecta aquí la implementación que hemos definido en el módulo de inyección.
    // Esto sigue el principio de Inversión de Dependencias: esta clase no sabe CÓMO se obtienen los datos,
    // solo sabe que existe un "contrato" (la interfaz MediaRepository) que se lo proporciona.
    private val repository: MediaRepository
) {
    /**
     * Sobrecargamos el operador `invoke` para que la clase pueda ser llamada como si fuera una función.
     * Esto es una convención común para los Casos de Uso.
     * Por ejemplo, en el ViewModel, en lugar de escribir `getAllItemsUseCase.execute()`, podemos escribir `getAllItemsUseCase()`.
     *
     * @return Un Flow de Kotlin que emite una lista de MediaItem. Usamos Flow porque es un stream de datos
     * que puede emitir valores de forma asíncrona. La UI se suscribirá a este Flow y se actualizará
     * automáticamente cada vez que la lista de ítems cambie en el repositorio.
     */
    operator fun invoke(): Flow<List<MediaItem>> {
        return repository.getAllItems()
    }
}