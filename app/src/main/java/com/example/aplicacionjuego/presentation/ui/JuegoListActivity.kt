package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.juegos.collect { juegos ->
                        juegoAdapter.submitList(juegos)
                    }
                }
                launch {
                    viewModel.addResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) Toast.makeText(this@JuegoListActivity, "Juego añadido", Toast.LENGTH_SHORT).show()
                            else Toast.makeText(this@JuegoListActivity, "Error al añadir: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            viewModel.resetAddResult()
                        }
                    }
                }
                launch {
                    viewModel.updateResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) Toast.makeText(this@JuegoListActivity, "Estado actualizado", Toast.LENGTH_SHORT).show()
                            else Toast.makeText(this@JuegoListActivity, "Error al actualizar: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            viewModel.resetUpdateResult()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        juegoAdapter = JuegoAdapter(
            onJuegoClick = { juego -> showChangeStateDialog(juego) },
            onDeleteClick = { juego -> showDeleteConfirmDialog(juego) } // Le pasamos el nuevo lambda
        )
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
                    val nuevoJuego = Juego(title = title, platform = platform, portada = portada, estado = estadoSeleccionado)
                    viewModel.addJuego(nuevoJuego)
                } else {
                    Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showChangeStateDialog(juego: Juego) {
        val estados = Estado.values()
        val estadoNombres = estados.map { it.name }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Cambiar estado de: ${juego.title}")
            .setItems(estadoNombres) { dialog, which ->
                val nuevoEstado = estados[which]
                val juegoActualizado = juego.copy(estado = nuevoEstado)
                viewModel.updateJuego(juegoActualizado)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar el juego.
     */
    private fun showDeleteConfirmDialog(juego: Juego) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Juego")
            .setMessage("¿Estás seguro de que quieres eliminar '${juego.title}'?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                viewModel.deleteJuego(juego) // Llamamos al ViewModel
            }
            .setNegativeButton("No", null)
            .show()
    }
}