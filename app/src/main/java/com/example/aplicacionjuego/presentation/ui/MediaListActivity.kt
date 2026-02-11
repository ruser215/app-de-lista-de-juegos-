package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.example.aplicacionjuego.domain.model.Categoria
import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.presentation.viewmodel.MediaViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Esta es la Activity principal de la aplicación. Su rol en la arquitectura MVVM es:
 * - Controlar la UI (inflar layouts, configurar vistas).
 * - Observar los cambios de estado del `MediaViewModel`.
 * - Recibir las interacciones del usuario y delegar la lógica al `ViewModel`.
 * - Gestionar la navegación (en este caso, la visibilidad de los fragmentos).
 */
@AndroidEntryPoint
class MediaListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // ViewBinding para acceder a las vistas del layout de forma segura.
    private lateinit var binding: ActivityMediaListBinding
    // Inyección del ViewModel usando Hilt. `by viewModels()` asegura que el ViewModel sobrevive a cambios de configuración.
    private val viewModel: MediaViewModel by viewModels()
    private lateinit var mediaAdapter: MediaAdapter
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Llamadas a métodos de configuración para mantener el `onCreate` limpio.
        setupDrawer()
        setupRecyclerView()
        setupObservers()
        setupBackButton()

        binding.fabAddItem.setOnClickListener {
            showAddItemFragment()
        }
    }

    /** Configura el Navigation Drawer y el Toolbar. */
    private fun setupDrawer() {
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    /**
     * Configura los observadores que reaccionan a los cambios de estado del ViewModel.
     * `lifecycleScope.launch` y `repeatOnLifecycle(Lifecycle.State.STARTED)` es la forma recomendada
     * de coleccionar `Flows` en la UI de forma segura, ya que cancela y reinicia la corrutina
     * automáticamente según el ciclo de vida de la Activity.
     */
    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observador para la lista de ítems. Cada vez que `mediaItems` emite un nuevo valor, se actualiza el adapter.
                launch {
                    viewModel.mediaItems.collect { mediaAdapter.submitList(it) }
                }
                // Observador para el resultado de añadir un ítem.
                launch {
                    viewModel.addResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) {
                                Toast.makeText(this@MediaListActivity, "Item añadido", Toast.LENGTH_SHORT).show()
                                hideAddItemFragment()
                            } else {
                                Toast.makeText(this@MediaListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                            viewModel.resetAddResult() // Reseteamos el estado para no volver a mostrar el Toast.
                        }
                    }
                }
                 // Observador para el resultado de la actualización.
                launch {
                    viewModel.updateResult.collect { result ->
                        result?.let {
                            if (it.isSuccess) {
                                Toast.makeText(this@MediaListActivity, "Item actualizado", Toast.LENGTH_SHORT).show()
                                hideEditFragment()
                            } else {
                                Toast.makeText(this@MediaListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                            viewModel.resetUpdateResult()
                        }
                    }
                }
                // Observador para el resultado del borrado.
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

    /** Configura el RecyclerView y su Adapter. */
    private fun setupRecyclerView() {
        mediaAdapter = MediaAdapter(
            onItemClick = { showEditFragment(it) }, // Lambda que se ejecuta al pulsar en un ítem.
            onDeleteClick = { showDeleteConfirmDialog(it) } // Lambda que se ejecuta al pulsar en el botón de borrar.
        )
        binding.rvItems.apply {
            adapter = mediaAdapter
            layoutManager = LinearLayoutManager(this@MediaListActivity)
        }
    }

    /**
     * Se llama cuando se pulsa una opción del Navigation Drawer.
     * La lógica se delega al ViewModel para que actualice el filtro.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all -> viewModel.setCategoryFilter(null)
            R.id.nav_games -> viewModel.setCategoryFilter(Categoria.JUEGO)
            R.id.nav_movies -> viewModel.setCategoryFilter(Categoria.PELICULA)
            R.id.nav_series -> viewModel.setCategoryFilter(Categoria.SERIE)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START) // Cierra el menú.
        return true
    }

    /** Muestra el diálogo de confirmación para eliminar un ítem. */
    private fun showDeleteConfirmDialog(item: MediaItem) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Item")
            .setMessage("¿Estás seguro de que quieres eliminar '${item.title}'?")
            .setPositiveButton("Sí") { _, _ -> viewModel.deleteItem(item) }
            .setNegativeButton("No", null)
            .show()
    }

    // --- Lógica para mostrar y ocultar los fragmentos ---

    private fun showEditFragment(item: MediaItem) {
        viewModel.selectItem(item)
        binding.editFragmentContainer.visibility = View.VISIBLE
        binding.rvItems.visibility = View.GONE
        binding.fabAddItem.visibility = View.GONE
    }

    private fun hideEditFragment() {
        binding.editFragmentContainer.visibility = View.GONE
        binding.rvItems.visibility = View.VISIBLE
        binding.fabAddItem.visibility = View.VISIBLE
        viewModel.selectItem(null) // Limpiamos la selección.
    }

    private fun showAddItemFragment() {
        binding.addFragmentContainer.visibility = View.VISIBLE
        binding.rvItems.visibility = View.GONE
        binding.fabAddItem.visibility = View.GONE
    }

    private fun hideAddItemFragment() {
        binding.addFragmentContainer.visibility = View.GONE
        binding.rvItems.visibility = View.VISIBLE
        binding.fabAddItem.visibility = View.VISIBLE
    }
	
    /**
     * Gestiona el comportamiento del botón "atrás" del sistema.
     * Si hay un fragmento abierto, lo cierra. Si no, cierra la Activity.
     */
    private fun setupBackButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    binding.editFragmentContainer.isVisible -> hideEditFragment()
                    binding.addFragmentContainer.isVisible -> hideAddItemFragment()
                    else -> finish()
                }
            }
        })
    }
}