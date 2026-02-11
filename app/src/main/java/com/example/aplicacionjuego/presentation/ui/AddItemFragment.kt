package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.FragmentAddItemBinding
import com.example.aplicacionjuego.domain.model.Categoria
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.presentation.viewmodel.MediaViewModel

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MediaViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        binding.apply {
            val estados = Estado.values().map { it.name }
            val estadoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerEstadoAdd.adapter = estadoAdapter

            val categorias = Categoria.values().map { it.name }
            val categoriaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerCategoriaAdd.adapter = categoriaAdapter
        }
    }

    private fun setupListeners() {
        binding.etUrlPortadaAdd.addTextChangedListener {
            Glide.with(requireContext())
                .load(it.toString())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.ivPortadaAdd)
        }

        binding.btnAnadirItem.setOnClickListener {
            val title = binding.etTitleAdd.text.toString()
            if (title.isNotEmpty()) {
                val newItem = MediaItem(
                    title = title,
                    platform = binding.etPlatformAdd.text.toString(),
                    portada = binding.etUrlPortadaAdd.text.toString(),
                    estado = Estado.valueOf(binding.spinnerEstadoAdd.selectedItem.toString()),
                    categoria = Categoria.valueOf(binding.spinnerCategoriaAdd.selectedItem.toString())
                )
                viewModel.addItem(newItem)
            } else {
                Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancelar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}