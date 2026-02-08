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
import kotlin.jvm.java

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() { // Antes MainActivity

    private lateinit var binding: ActivityLoginBinding // Asegúrate de que el XML se llame activity_login.xml
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para entrar
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.login(email, pass)
            }
        }

        // Navegación al Registro
        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        observarLogin()
    }

    private fun observarLogin() {
        lifecycleScope.launch {
            viewModel.loginState.collect { result ->
                result?.onSuccess {
                    // Si el login es correcto, vamos a la lista de juegos
                    startActivity(Intent(this@LoginActivity, JuegoListActivity::class.java))
                    finish()
                }?.onFailure {
                    Toast.makeText(this@LoginActivity, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}