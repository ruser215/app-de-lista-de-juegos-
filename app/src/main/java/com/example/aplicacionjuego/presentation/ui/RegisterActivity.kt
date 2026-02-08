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

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterReal.setOnClickListener {
            val email = binding.etEmailReg.text.toString().trim()
            val pass = binding.etPassReg.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass.length >= 6) {
                    viewModel.register(email, pass)
                } else {
                    Toast.makeText(this, "La contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }



        binding.btnBackToLogin.setOnClickListener {
            finish()
        }

        observarRegistro()
    }
    private fun observarRegistro() {
        lifecycleScope.launch {
            viewModel.authState.collect { result ->
                result?.onSuccess {
                    Toast.makeText(
                        this@RegisterActivity,
                        "¡Cuenta creada! Revisa tu email para verificarla.",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                }?.onFailure { error ->
                    Toast.makeText(this@RegisterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}