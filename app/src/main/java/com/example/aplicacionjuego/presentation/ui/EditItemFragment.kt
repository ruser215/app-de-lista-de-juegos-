package com.example.aplicacionjuego.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.FragmentEditItemBinding
import com.example.aplicacionjuego.domain.model.Categoria
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem
import com.example.aplicacionjuego.presentation.viewmodel.MediaViewModel
import kotlinx.coroutines.launch

class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MediaViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedItem.collect { item ->
                    item?.let { bindMediaItemData(it) }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.etUrlPortada.addTextChangedListener {
            Glide.with(this).load(it.toString()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(binding.ivPortadaEdit)
        }

        binding.btnGuardar.setOnClickListener {
            val currentItem = viewModel.selectedItem.value ?: return@setOnClickListener
            
            val updatedItem = currentItem.copy(
                portada = binding.etUrlPortada.text.toString(),
                rating = binding.rbRating.rating,
                estado = Estado.valueOf(binding.spinnerEstadoEdit.selectedItem.toString()),
                opinion = binding.etOpinion.text.toString(),
                categoria = Categoria.valueOf(binding.spinnerCategoriaEdit.selectedItem.toString())
            )
            
            viewModel.updateItem(updatedItem)
        }
    }

    private fun bindMediaItemData(item: MediaItem) {
        binding.apply {
            etUrlPortada.setText(item.portada)
            rbRating.rating = item.rating
            etOpinion.setText(item.opinion)

            Glide.with(requireContext()).load(item.portada).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(ivPortadaEdit)
            
            val estados = Estado.values().map { it.name }
            val estadoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerEstadoEdit.adapter = estadoAdapter
            spinnerEstadoEdit.setSelection(estados.indexOf(item.estado.name))

            val categorias = Categoria.values().map { it.name }
            val categoriaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categorias).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerCategoriaEdit.adapter = categoriaAdapter
            spinnerCategoriaEdit.setSelection(categorias.indexOf(item.categoria.name))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}