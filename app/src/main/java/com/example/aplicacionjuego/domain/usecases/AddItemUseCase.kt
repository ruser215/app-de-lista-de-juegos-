package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val repository: MediaRepository
){
    suspend operator fun invoke(item: MediaItem): Result<Unit> {
        if (item.title.isBlank()) {
            return Result.failure(Exception("El título no puede estar vacío"))
        }
        return repository.addItem(item)
    }
}