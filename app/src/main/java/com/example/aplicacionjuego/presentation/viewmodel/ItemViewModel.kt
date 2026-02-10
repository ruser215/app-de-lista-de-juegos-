package com.example.aplicacionjuego.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.domain.usecases.AddItemUseCase
import com.example.aplicacionjuego.domain.usecases.DeleteItemUseCase
import com.example.aplicacionjuego.domain.usecases.GetAllItemsUseCase
import com.example.aplicacionjuego.domain.usecases.UpdateItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Renombramos la clase
@HiltViewModel
class MediaViewModel @Inject constructor(
    // 2. Inyectamos los nuevos casos de uso refactorizados
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    // 3. Actualizamos los StateFlows para que usen MediaItem
    private val _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())
    val mediaItems: StateFlow<List<MediaItem>> get() = _mediaItems

    private val _addResult = MutableStateFlow<Result<Unit>?>(null)
    val addResult: StateFlow<Result<Unit>?> get() = _addResult

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult: StateFlow<Result<Unit>?> get() = _updateResult

    private val _deleteResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteResult: StateFlow<Result<Unit>?> get() = _deleteResult

    private val _selectedItem = MutableStateFlow<MediaItem?>(null)
    val selectedItem: StateFlow<MediaItem?> get() = _selectedItem

    init {
        loadItems()
    }

    // 4. Renombramos los métodos y la lógica interna
    private fun loadItems() {
        viewModelScope.launch {
            getAllItemsUseCase()
                .catch { e -> /* Manejo de errores */ }
                .collect { _mediaItems.value = it }
        }
    }

    fun selectItem(item: MediaItem?) {
        _selectedItem.value = item
    }

    fun addItem(item: MediaItem) {
        viewModelScope.launch {
            _addResult.value = addItemUseCase(item)
        }
    }

    fun updateItem(item: MediaItem) {
        viewModelScope.launch {
            _updateResult.value = updateItemUseCase(item)
        }
    }

    fun deleteItem(item: MediaItem) {
        viewModelScope.launch {
            _deleteResult.value = deleteItemUseCase(item)
        }
    }

    // --- Métodos para resetear los resultados ---
    fun resetAddResult() { _addResult.value = null }
    fun resetUpdateResult() { _updateResult.value = null }
    fun resetDeleteResult() { _deleteResult.value = null }
}