package com.example.aplicacionjuego.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionjuego.domain.usecases.LoginUseCase
import com.example.aplicacionjuego.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para las pantallas de autenticación (Login y Registro).
 * Su responsabilidad es gestionar el estado y la lógica de negocio relacionados con la autenticación.
 * Es independiente del `MediaViewModel` para separar las responsabilidades.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    // Hilt se encarga de inyectar los casos de uso que este ViewModel necesita.
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    // --- ESTADO PARA EL REGISTRO ---

    // `_authState` es la fuente de la verdad para el estado del registro. Es mutable y privado.
    private val _authState = MutableStateFlow<Result<Boolean>?>(null)
    // `authState` es la versión pública e inmutable del estado. La UI lo observará.
    val authState = _authState.asStateFlow()

    // --- ESTADO PARA EL LOGIN ---

    private val _loginState = MutableStateFlow<Result<Boolean>?>(null)
    val loginState = _loginState.asStateFlow()

    /**
     * Llama al caso de uso de Login. Se ejecuta en una corrutina lanzada con `viewModelScope`,
     * que está atada al ciclo de vida del ViewModel.
     */
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            // Llama al caso de uso (que es una clase "invocable" como una función).
            val result = loginUseCase(email, pass)
            // Publica el resultado en el StateFlow, lo que notificará a la UI.
            _loginState.value = result
        }
    }

    /**
     * Llama al caso de uso de Registro.
     */
    fun register(email: String, pass: String) {
        viewModelScope.launch {
            val result = registerUseCase(email, pass)
            _authState.value = result
        }
    }
}