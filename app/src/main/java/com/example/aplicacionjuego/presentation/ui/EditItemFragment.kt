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

/**
 * Fragmento encargado de la interfaz para editar un ítem existente.
 * Es muy similar al de añadir, pero con una diferencia clave: este fragmento
 * OBSERVA los datos de un ítem seleccionado en el ViewModel para rellenar su UI.
 */
class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!

    // Obtenemos el ViewModel compartido con la Activity.
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

    /**
     * Configura los observadores del ViewModel. En este caso, solo nos interesa el `selectedItem`.
     */
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Nos suscribimos a los cambios del `selectedItem` en el ViewModel.
                viewModel.selectedItem.collect { item ->
                    // Si el ítem no es nulo, llamamos a la función que rellena la UI con sus datos.
                    item?.let { bindMediaItemData(it) }
                }
            }
        }
    }

    /** Configura los listeners de la UI. */
    private fun setupListeners() {
        binding.etUrlPortada.addTextChangedListener {
            Glide.with(requireContext()).load(it.toString()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(binding.ivPortadaEdit)
        }

        binding.btnGuardar.setOnClickListener {
            val currentItem = viewModel.selectedItem.value ?: return@setOnClickListener
            
            // Creamos una copia del ítem actual con los nuevos datos de la UI.
            val updatedItem = currentItem.copy(
                portada = binding.etUrlPortada.text.toString(),
                rating = binding.rbRating.rating,
                estado = Estado.valueOf(binding.spinnerEstadoEdit.selectedItem.toString()),
                opinion = binding.etOpinion.text.toString(),
                categoria = Categoria.valueOf(binding.spinnerCategoriaEdit.selectedItem.toString())
            )
            
            // Delegamos la acción de actualizar al ViewModel.
            viewModel.updateItem(updatedItem)
        }
    }

    /**
     * Rellena todos los campos de la UI con los datos del `MediaItem` proporcionado.
     */
    private fun bindMediaItemData(item: MediaItem) {
        binding.apply {
            etUrlPortada.setText(item.portada)
            rbRating.rating = item.rating
            etOpinion.setText(item.opinion)

            Glide.with(requireContext()).load(item.portada).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(ivPortadaEdit)
            
            // Configuración de los Spinners para que muestren el valor actual del ítem.
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
        _binding = null // Es importante limpiar el binding para evitar fugas de memoria.
    }
}