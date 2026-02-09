package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
        setupObservers()
        setupBackButton()

        binding.fabAddGame.setOnClickListener {
            showAddGameDialog()
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.juegos.collect { juegoAdapter.submitList(it) }
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
                            if (it.isSuccess) {
                                Toast.makeText(this@JuegoListActivity, "Juego actualizado", Toast.LENGTH_SHORT).show()
                                hideEditFragment() // Ocultamos el fragmento al guardar
                            } else {
                                Toast.makeText(this@JuegoListActivity, "Error al actualizar: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                            viewModel.resetUpdateResult()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        juegoAdapter = JuegoAdapter(
            onJuegoClick = { showEditFragment(it) },
            onDeleteClick = { showDeleteConfirmDialog(it) }
        )
        binding.rvJuegos.apply {
            adapter = juegoAdapter
            layoutManager = LinearLayoutManager(this@JuegoListActivity)
        }
    }

    private fun showAddGameDialog() {
        // ... (el código del diálogo de añadir no cambia)
    }

    private fun showDeleteConfirmDialog(juego: Juego) {
        // ... (el código del diálogo de borrar no cambia)
    }

    private fun showEditFragment(juego: Juego) {
        viewModel.seleccionarJuego(juego)
        binding.fragmentContainerView.visibility = View.VISIBLE
        binding.rvJuegos.visibility = View.GONE
        binding.fabAddGame.visibility = View.GONE
    }

    private fun hideEditFragment() {
        binding.fragmentContainerView.visibility = View.GONE
        binding.rvJuegos.visibility = View.VISIBLE
        binding.fabAddGame.visibility = View.VISIBLE
        viewModel.seleccionarJuego(null) // Limpiamos la selección
    }

    private fun setupBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.fragmentContainerView.isVisible) {
                    hideEditFragment()
                } else {
                    finish()
                }
            }
        })
    }
}