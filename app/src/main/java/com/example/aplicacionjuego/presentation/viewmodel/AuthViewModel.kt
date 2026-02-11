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

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<Result<Boolean>?>(null)
    val authState = _authState.asStateFlow()


    private val _loginState = MutableStateFlow<Result<Boolean>?>(null)
    val loginState = _loginState.asStateFlow()


    fun login(email: String, pass: String) {
        viewModelScope.launch {
            val result = loginUseCase(email, pass)
            _loginState.value = result
        }
    }

    fun register(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = registerUseCase(email, pass)
        }
    }
}