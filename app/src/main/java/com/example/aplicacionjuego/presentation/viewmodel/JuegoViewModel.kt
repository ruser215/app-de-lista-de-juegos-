package com.example.aplicacionjuego.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.domain.usecases.AddJuedosUseCase
import com.example.aplicacionjuego.domain.usecases.DeleteJuegoUseCase
import com.example.aplicacionjuego.domain.usecases.GetGamesUseCase
import com.example.aplicacionjuego.domain.usecases.UpdateJuegoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuegoViewModel @Inject constructor(
    private val getAllGamesUseCase: GetGamesUseCase,
    private val updateGameUseCase: UpdateJuegoUseCase,
    private val addGameUseCase: AddJuedosUseCase,
    private val deleteJuegoUseCase: DeleteJuegoUseCase
) : ViewModel() {

    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())
    val juegos: StateFlow<List<Juego>> get() = _juegos

    private val _addResult = MutableStateFlow<Result<Unit>?>(null)
    val addResult: StateFlow<Result<Unit>?> get() = _addResult

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult: StateFlow<Result<Unit>?> get() = _updateResult

    private val _deleteResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteResult: StateFlow<Result<Unit>?> get() = _deleteResult

    // StateFlow para el juego que se está editando
    private val _juegoSeleccionado = MutableStateFlow<Juego?>(null)
    val juegoSeleccionado: StateFlow<Juego?> get() = _juegoSeleccionado

    init {
        loadJuegos()
    }

    private fun loadJuegos() {
        viewModelScope.launch {
            getAllGamesUseCase()
                .catch { e -> /* Manejo de errores */ }
                .collect { _juegos.value = it }
        }
    }

    /**
     * Guarda el juego seleccionado para que el fragmento de edición lo observe.
     * Acepta un valor nulo para poder limpiar la selección.
     */
    fun seleccionarJuego(juego: Juego?) {
        _juegoSeleccionado.value = juego
    }

    fun addJuego(juego: Juego) {
        viewModelScope.launch {
            _addResult.value = addGameUseCase(juego)
        }
    }

    fun updateJuego(juego: Juego) {
        viewModelScope.launch {
            _updateResult.value = updateGameUseCase(juego)
        }
    }

    fun deleteJuego(juego: Juego) {
        viewModelScope.launch {
            _deleteResult.value = deleteJuegoUseCase(juego)
        }
    }

    fun resetAddResult() {
        _addResult.value = null
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }

    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}