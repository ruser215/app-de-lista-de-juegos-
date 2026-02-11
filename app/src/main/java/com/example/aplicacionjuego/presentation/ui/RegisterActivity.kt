package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.aplicacionjuego.databinding.ActivityRegisterBinding
import com.example.aplicacionjuego.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity para el registro de un nuevo usuario.
 * Al igual que la de Login, su única responsabilidad es gestionar la UI y delegar la lógica
 * de negocio al `AuthViewModel` compartido.
 */
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listener para el botón de registro.
        binding.btnRegisterReal.setOnClickListener {
            val email = binding.etEmailReg.text.toString().trim()
            val pass = binding.etPassReg.text.toString().trim()

            // Realizamos validaciones básicas en la UI antes de proceder.
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass.length >= 6) {
                    // Si todo es correcto, delegamos la acción al ViewModel.
                    viewModel.register(email, pass)
                } else {
                    Toast.makeText(this, "La contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener para el botón de volver atrás.
        binding.btnBackToLogin.setOnClickListener {
            finish() // Cierra la Activity actual y vuelve a la anterior (Login).
        }

        observarRegistro()
    }

    /**
     * Configura el observador que reacciona al estado del registro en el ViewModel.
     */
    private fun observarRegistro() {
        lifecycleScope.launch {
            viewModel.authState.collect { result ->
                result?.let { // `let` se ejecuta solo si el resultado no es nulo.
                    it.onSuccess {
                        // Si el registro es exitoso, mostramos un mensaje informativo y cerramos la pantalla.
                        Toast.makeText(
                            this@RegisterActivity,
                            "¡Cuenta creada! Revisa tu email para verificarla.",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }.onFailure { error ->
                        // Si el registro falla, mostramos un Toast con el mensaje de error.
                        Toast.makeText(this@RegisterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}