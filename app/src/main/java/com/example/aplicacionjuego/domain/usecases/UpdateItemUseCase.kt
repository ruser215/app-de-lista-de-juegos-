package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import javax.inject.Inject

class UpdateItemUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(item: MediaItem): Result<Unit> {
        return repository.updateItem(item)
    }
}