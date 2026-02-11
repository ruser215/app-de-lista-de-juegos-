package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.ActivityMediaListBinding
import com.example.aplicacionjuego.databinding.DialogAddItemBinding
import com.example.aplicacionjuego.domain.model.Categoria
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.presentation.viewmodel.MediaViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMediaListBinding
    private val viewModel: MediaViewModel by viewModels()
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer()
        setupRecyclerView()
        setupObservers()
        setupBackButton()

        binding.fabAddItem.setOnClickListener {
            showAddItemDialog()
        }
    }

    private fun setupDrawer() {
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.mediaItems.collect { mediaAdapter.submitList(it) }
                }
                launch {
                    viewModel.addResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) Toast.makeText(this@MediaListActivity, "Item añadido", Toast.LENGTH_SHORT).show()
                            else Toast.makeText(this@MediaListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            viewModel.resetAddResult()
                        }
                    }
                }
                launch {
                    viewModel.updateResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) {
                                Toast.makeText(this@MediaListActivity, "Item actualizado", Toast.LENGTH_SHORT).show()
                                hideEditFragment() // <-- La línea que faltaba
                            } else {
                                Toast.makeText(this@MediaListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                            viewModel.resetUpdateResult()
                        }
                    }
                }
                launch {
                    viewModel.deleteResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) Toast.makeText(this@MediaListActivity, "Item eliminado", Toast.LENGTH_SHORT).show()
                            else Toast.makeText(this@MediaListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            viewModel.resetDeleteResult()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        mediaAdapter = MediaAdapter(
            onItemClick = { showEditFragment(it) },
            onDeleteClick = { showDeleteConfirmDialog(it) }
        )
        binding.rvItems.apply {
            adapter = mediaAdapter
            layoutManager = LinearLayoutManager(this@MediaListActivity)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all -> viewModel.setCategoryFilter(null)
            R.id.nav_games -> viewModel.setCategoryFilter(Categoria.JUEGO)
            R.id.nav_movies -> viewModel.setCategoryFilter(Categoria.PELICULA)
            R.id.nav_series -> viewModel.setCategoryFilter(Categoria.SERIE)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showAddItemDialog() {
        val dialogBinding = DialogAddItemBinding.inflate(layoutInflater)
        
        val estados = Estado.values().map { it.name }
        val estadoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        dialogBinding.spinnerEstado.adapter = estadoAdapter

        val categorias = Categoria.values().map { it.name }
        val categoriaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        dialogBinding.spinnerCategoria.adapter = categoriaAdapter

        AlertDialog.Builder(this)
            .setTitle("Añadir a mi colección")
            .setView(dialogBinding.root)
            .setPositiveButton("Añadir") { _, _ ->
                val title = dialogBinding.etTitle.text.toString()
                val platform = dialogBinding.etPlatform.text.toString()
                val portada = dialogBinding.etPortada.text.toString()
                val estado = Estado.valueOf(dialogBinding.spinnerEstado.selectedItem.toString())
                val categoria = Categoria.valueOf(dialogBinding.spinnerCategoria.selectedItem.toString())

                if (title.isNotEmpty()) {
                    val newItem = MediaItem(title = title, platform = platform, portada = portada, estado = estado, categoria = categoria)
                    viewModel.addItem(newItem)
                } else {
                    Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showDeleteConfirmDialog(item: MediaItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Item")
            .setMessage("¿Estás seguro de que quieres eliminar '${item.title}'?")
            .setPositiveButton("Sí") { _, _ -> viewModel.deleteItem(item) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showEditFragment(item: MediaItem) {
        viewModel.selectItem(item)
        binding.fragmentContainerView.visibility = View.VISIBLE
        binding.rvItems.visibility = View.GONE
        binding.fabAddItem.visibility = View.GONE
    }

    private fun hideEditFragment() {
        binding.fragmentContainerView.visibility = View.GONE
        binding.rvItems.visibility = View.VISIBLE
        binding.fabAddItem.visibility = View.VISIBLE
        viewModel.selectItem(null)
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