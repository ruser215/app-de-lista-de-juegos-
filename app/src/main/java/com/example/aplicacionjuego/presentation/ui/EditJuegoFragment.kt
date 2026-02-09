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
import com.example.aplicacionjuego.databinding.FragmentEditJuegoBinding
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.presentation.viewmodel.JuegoViewModel
import kotlinx.coroutines.launch

class EditJuegoFragment : Fragment() {

    private var _binding: FragmentEditJuegoBinding? = null
    private val binding get() = _binding!!

    // Usamos activityViewModels() para compartir el ViewModel con la Activity
    private val viewModel: JuegoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditJuegoBinding.inflate(inflater, container, false)
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
                viewModel.juegoSeleccionado.collect { juego ->
                    juego?.let { bindJuegoData(it) }
                }
            }
        }
    }

    private fun setupListeners() {
        // Actualizar la imagen en tiempo real al cambiar la URL
        binding.etUrlPortada.addTextChangedListener {
            Glide.with(this)
                .load(it.toString())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.ivPortadaEdit)
        }

        // Guardar los cambios
        binding.btnGuardar.setOnClickListener {
            val juegoActual = viewModel.juegoSeleccionado.value ?: return@setOnClickListener
            
            val juegoActualizado = juegoActual.copy(
                portada = binding.etUrlPortada.text.toString(),
                rating = binding.rbRating.rating,
                estado = Estado.valueOf(binding.spinnerEstadoEdit.selectedItem.toString()),
                opinion = binding.etOpinion.text.toString()
            )
            
            viewModel.updateJuego(juegoActualizado)
            // Podríamos cerrar el fragmento aquí si quisiéramos
        }
    }

    private fun bindJuegoData(juego: Juego) {
        binding.apply {
            etUrlPortada.setText(juego.portada)
            rbRating.rating = juego.rating
            etOpinion.setText(juego.opinion)

            Glide.with(requireContext())
                .load(juego.portada)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(ivPortadaEdit)
            
            // Configurar el Spinner de Estado
            val estados = Estado.values().map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerEstadoEdit.adapter = adapter
            spinnerEstadoEdit.setSelection(estados.indexOf(juego.estado.name))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }
}