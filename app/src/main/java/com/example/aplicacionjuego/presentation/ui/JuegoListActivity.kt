package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle // <-- IMPORTACIÓN AÑADIDA
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionjuego.databinding.ActivityJuegoListBinding
import com.example.aplicacionjuego.databinding.DialogAddJuegoBinding
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.presentation.viewmodel.JuegoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JuegoListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJuegoListBinding
    private val viewModel: JuegoViewModel by viewModels()
    private lateinit var juegoAdapter: JuegoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuegoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.fabAddGame.setOnClickListener {
            showAddGameDialog()
        }

        // --- OBSERVADORES DEL VIEWMODEL (CON REPEATONLIFECYCLE) ---
        // Recolectamos los flows de forma segura y eficiente
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observador para la lista de juegos
                launch {
                    viewModel.juegos.collect { juegos ->
                        juegoAdapter.submitList(juegos)
                    }
                }

                // Observador para el resultado de añadir un juego
                launch {
                    viewModel.addResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) {
                                Toast.makeText(this@JuegoListActivity, "Juego añadido correctamente", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@JuegoListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                            viewModel.resetAddResult() // Limpiar el resultado para no volver a mostrar el Toast
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        juegoAdapter = JuegoAdapter { juego ->
            // Aquí puedes manejar el clic en un juego (para editarlo, etc.)
        }
        binding.rvJuegos.apply {
            adapter = juegoAdapter
            layoutManager = LinearLayoutManager(this@JuegoListActivity)
        }
    }

    private fun showAddGameDialog() {
        val dialogBinding = DialogAddJuegoBinding.inflate(layoutInflater)

        val estados = Estado.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        dialogBinding.spinnerEstado.adapter = adapter

        AlertDialog.Builder(this)
            .setTitle("Añadir a mi colección")
            .setView(dialogBinding.root)
            .setPositiveButton("Añadir") { _, _ ->
                val title = dialogBinding.etTitle.text.toString()
                val platform = dialogBinding.etPlatform.text.toString()
                val portada = dialogBinding.etPortada.text.toString()
                val estadoSeleccionado = Estado.valueOf(dialogBinding.spinnerEstado.selectedItem.toString())

                if (title.isNotEmpty()) {
                    val nuevoJuego = Juego(
                        title = title,
                        platform = platform,
                        portada = portada,
                        estado = estadoSeleccionado
                    )
                    viewModel.addJuego(nuevoJuego)
                } else {
                    Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}