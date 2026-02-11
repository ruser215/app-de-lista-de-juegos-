package com.example.aplicacionjuego.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionjuego.domain.model.Categoria
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla principal. Es el "cerebro" de la UI.
 * Se encarga de:
 * - Pedir los datos a la capa de Dominio (a través de los Casos de Uso).
 * - Gestionar el estado de la UI (la lista de ítems, filtros, resultados de operaciones...).
 * - Exponer este estado a la Vista (Activity/Fragments) para que se pinten los datos.
 */
@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getAllItemsUseCase: GetAllItemsUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val addItemUseCase: AddItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase
) : ViewModel() {

    // --- GESTIÓN DEL ESTADO DE LA LISTA Y FILTRADO ---

    // `_allItems` es la "fuente de la verdad". Contiene la lista COMPLETA y sin filtrar de ítems.
    private val _allItems = MutableStateFlow<List<MediaItem>>(emptyList())

    // `_categoryFilter` guarda la categoría seleccionada por el usuario. `null` significa "mostrar todos".
    private val _categoryFilter = MutableStateFlow<Categoria?>(null)

    /**
     * `mediaItems` es el `StateFlow` PÚBLICO que la UI observará.
     * Se crea usando `combine`, que mezcla los datos de `_allItems` y `_categoryFilter`.
     * Cada vez que cualquiera de los dos flujos de origen emite un nuevo valor, `combine` se ejecuta
     * y emite una nueva lista filtrada. Esto crea una UI completamente reactiva.
     */
    val mediaItems: StateFlow<List<MediaItem>> = 
        combine(_allItems, _categoryFilter) { items, filter ->
            if (filter == null) {
                items
            } else {
                items.filter { it.categoria == filter }
            }
        }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Lazily, emptyList())


    // --- GESTIÓN DEL ESTADO DE OPERACIONES (ADD, UPDATE, DELETE) ---

    // StateFlows para notificar a la UI sobre el resultado de las operaciones.
    // Son `nullable` para poder "resetearlos" y evitar que un Toast se muestre múltiples veces.
    private val _addResult = MutableStateFlow<Result<Unit>?>(null)
    val addResult: StateFlow<Result<Unit>?> get() = _addResult

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult: StateFlow<Result<Unit>?> get() = _updateResult

    private val _deleteResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteResult: StateFlow<Result<Unit>?> get() = _deleteResult

    // --- GESTIÓN DEL ESTADO DEL ÍTEM SELECCIONADO (para la edición) ---

    private val _selectedItem = MutableStateFlow<MediaItem?>(null)
    val selectedItem: StateFlow<MediaItem?> get() = _selectedItem

    init {
        loadItems() // Se cargan los datos iniciales al crear el ViewModel.
    }

    /** Carga la lista completa de ítems desde el repositorio. */
    private fun loadItems() {
        viewModelScope.launch {
            getAllItemsUseCase()
                .catch { e -> /* Aquí se podría gestionar un error de carga */ }
                .collect { _allItems.value = it }
        }
    }

    // --- FUNCIONES PÚBLICAS (Llamadas por la UI) ---

    fun setCategoryFilter(categoria: Categoria?) {
        _categoryFilter.value = categoria
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

    // --- FUNCIONES PARA RESETEAR EL ESTADO DE LAS OPERACIONES ---

    fun resetAddResult() { _addResult.value = null }
    fun resetUpdateResult() { _updateResult.value = null }
    fun resetDeleteResult() { _deleteResult.value = null }
}