package com.example.aplicacionjuego.domain.usecases

import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllItemsUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(): Flow<List<MediaItem>> {
        return repository.getAllItems()
    }
}