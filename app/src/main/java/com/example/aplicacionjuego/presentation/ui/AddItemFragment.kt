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

/**
 * Fragmento encargado de la interfaz para añadir un nuevo ítem a la colección.
 * Sigue el patrón MVVM: su única responsabilidad es gestionar la UI y delegar toda la lógica
 * de negocio y de datos al `MediaViewModel`.
 */
class AddItemFragment : Fragment() {

    // _binding es nullable para poder limpiarlo en onDestroyView y evitar fugas de memoria.
    private var _binding: FragmentAddItemBinding? = null
    // Esta propiedad de solo lectura nos permite acceder al binding de forma segura sin el `?`.
    private val binding get() = _binding!!

    // Usamos `activityViewModels()` para obtener una instancia del ViewModel que está "atada" al ciclo de vida
    // de la Activity. Esto nos permite compartir el mismo ViewModel entre esta Activity y sus fragmentos.
    private val viewModel: MediaViewModel by activityViewModels()

    /**
     * Se llama para que el fragmento "infle" su layout. Aquí es donde el XML se convierte en objetos View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Se llama justo después de `onCreateView`, cuando la jerarquía de vistas del fragmento ya ha sido creada.
     * Es el lugar ideal para configurar las vistas y añadir listeners.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinners()
        setupListeners()
    }

    /** Configura los `ArrayAdapter` para los Spinners de estado y categoría. */
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

    /** Configura los `onClickListener` para los botones y otros listeners de la UI. */
    private fun setupListeners() {
        // Listener para la URL: actualiza la imagen en tiempo real.
        binding.etUrlPortadaAdd.addTextChangedListener {
            Glide.with(requireContext())
                .load(it.toString())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.ivPortadaAdd)
        }

        // Listener para el botón de añadir.
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
                // Delegamos la acción al ViewModel.
                viewModel.addItem(newItem)
            } else {
                Toast.makeText(requireContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show()
            }
        }

        // Listener para el botón de cancelar.
        binding.btnCancelar.setOnClickListener {
            // Simulamos el evento de pulsar "atrás" para que la Activity se encargue de ocultar el fragmento.
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Se llama cuando la vista del fragmento va a ser destruida. Es crucial limpiar aquí la referencia
     * al `_binding` para evitar fugas de memoria (memory leaks).
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}