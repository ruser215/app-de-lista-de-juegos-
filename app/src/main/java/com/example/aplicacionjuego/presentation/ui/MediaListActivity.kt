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
            showAddItemFragment()
        }
    }

    private fun setupDrawer() {
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name)
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
                            if (it.isSuccess) {
                                Toast.makeText(this@MediaListActivity, "Item añadido", Toast.LENGTH_SHORT).show()
                                hideAddItemFragment()
                            } else {
                                Toast.makeText(this@MediaListActivity, "Error: ${it.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                            viewModel.resetAddResult()
                        }
                    }
                }
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
        binding.editFragmentContainer.visibility = View.VISIBLE
        binding.rvItems.visibility = View.GONE
        binding.fabAddItem.visibility = View.GONE
    }

    private fun hideEditFragment() {
        binding.editFragmentContainer.visibility = View.GONE
        binding.rvItems.visibility = View.VISIBLE
        binding.fabAddItem.visibility = View.VISIBLE
        viewModel.selectItem(null)
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