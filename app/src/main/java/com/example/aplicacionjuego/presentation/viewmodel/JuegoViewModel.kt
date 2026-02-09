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
    private val deleteJuegoUseCase: DeleteJuegoUseCase // Inyectamos el nuevo caso de uso
) : ViewModel() {

    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())
    val juegos: StateFlow<List<Juego>> get() = _juegos

    private val _addResult = MutableStateFlow<Result<Unit>?>(null)
    val addResult: StateFlow<Result<Unit>?> get() = _addResult

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult: StateFlow<Result<Unit>?> get() = _updateResult

    // StateFlow para el resultado del borrado
    private val _deleteResult = MutableStateFlow<Result<Unit>?>(null)
    val deleteResult: StateFlow<Result<Unit>?> get() = _deleteResult

    init {
        loadJuegos()
    }

    private fun loadJuegos() {
        viewModelScope.launch {
            getAllGamesUseCase()
                .catch { e ->
                    // Manejo de errores
                }
                .collect { gameList ->
                    _juegos.value = gameList
                }
        }
    }

    fun addJuego(juego: Juego) {
        viewModelScope.launch {
            val result = addGameUseCase(juego)
            _addResult.value = result
        }
    }

    fun updateJuego(juego: Juego) {
        viewModelScope.launch {
            val result = updateGameUseCase(juego)
            _updateResult.value = result
        }
    }

    /**
     * Llama al caso de uso para eliminar el juego.
     */
    fun deleteJuego(juego: Juego) {
        viewModelScope.launch {
            val result = deleteJuegoUseCase(juego)
            _deleteResult.value = result
        }
    }

    fun resetAddResult() {
        _addResult.value = null
    }

    fun resetUpdateResult() {
        _updateResult.value = null
    }

    /**
     * Resetea el estado del resultado del borrado.
     */
    fun resetDeleteResult() {
        _deleteResult.value = null
    }
}