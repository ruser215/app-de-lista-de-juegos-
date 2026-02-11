package com.example.aplicacionjuego.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aplicacionjuego.databinding.ActivityLoginBinding
import com.example.aplicacionjuego.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity de Login. Es el punto de entrada de la aplicación (definido en el AndroidManifest.xml).
 * Su responsabilidad es exclusivamente gestionar la UI del inicio de sesión.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    // Inyectamos el ViewModel de autenticación. Este ViewModel es específico para las pantallas de login y registro.
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listener para el botón de login.
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            // Validación simple en la UI antes de llamar al ViewModel.
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Delegamos la lógica de negocio al ViewModel.
                viewModel.login(email, pass)
            }
        }

        // Listener para el texto que lleva a la pantalla de registro.
        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        observarLogin()
    }

    /**
     * Configura el observador que reacciona al estado del login en el ViewModel.
     */
    private fun observarLogin() {
        lifecycleScope.launch {
            // Nos suscribimos al `loginState` del ViewModel.
            viewModel.loginState.collect { result ->
                result?.let { // `let` se ejecuta solo si el resultado no es nulo.
                    it.onSuccess {
                        // Si el login es exitoso, navegamos a la pantalla principal de la lista.
                        startActivity(Intent(this@LoginActivity, MediaListActivity::class.java))
                        finish() // Cerramos esta activity para que el usuario no pueda volver atrás.
                    }.onFailure {
                        // Si el login falla, mostramos un Toast con el mensaje de error.
                        Toast.makeText(this@LoginActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}