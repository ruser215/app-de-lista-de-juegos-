package com.example.aplicacionjuego.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.ItemItemBinding
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem

/**
 * El Adapter es el componente que sirve de puente entre los datos (la lista de `MediaItem`) y el `RecyclerView`.
 * Hereda de `ListAdapter`, que es una versión avanzada del `RecyclerView.Adapter` que gestiona
 * las actualizaciones de la lista de forma muy eficiente gracias al `DiffUtil`.
 */
class MediaAdapter(
    // El adapter no debe manejar la lógica de los clics. En su lugar, expone lambdas
    // para que la Activity o el Fragment que lo usan decidan qué hacer.
    private val onItemClick: (MediaItem) -> Unit,
    private val onDeleteClick: (MediaItem) -> Unit
) : ListAdapter<MediaItem, MediaAdapter.MediaViewHolder>(MediaDiffCallback()) {

    /**
     * El `ViewHolder` representa una única vista de ítem en la lista. Su trabajo es "sostener"
     * las referencias a las vistas (TextViews, ImageView, etc.) para que no haya que buscarlas
     * con `findViewById` cada vez que se recicla una vista, lo cual es muy costoso.
     */
    class MediaViewHolder(val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Se llama cuando el `RecyclerView` necesita crear un nuevo `ViewHolder` (una nueva tarjeta para un ítem).
     * Aquí es donde se "infla" el layout XML del ítem.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    /**
     * Se llama cuando el `RecyclerView` quiere mostrar los datos de un ítem en una posición específica.
     * Aquí es donde se "vinculan" los datos del `MediaItem` con las vistas del `ViewHolder`.
     */
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tvTitulo.text = item.title
            tvPlataforma.text = item.platform
            chipEstado.text = item.estado.name

            Glide.with(ivPortada.context)
                .load(item.portada)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(ivPortada)

            val estadoColor = when (item.estado) {
                Estado.PENDIENTE -> R.color.status_pendiente
                Estado.COMENZADO -> R.color.status_comenzado
                Estado.PAUSADO -> R.color.status_pausado
                Estado.TERMINADO -> R.color.status_terminado
                else -> R.color.status_pendiente
            }
            chipEstado.setChipBackgroundColorResource(estadoColor)

            // Asignamos las lambdas a los listeners de las vistas.
            root.setOnClickListener { onItemClick(item) }
            btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}

/**
 * `DiffUtil` es una herramienta de optimización. Ayuda al `ListAdapter` a calcular la forma más eficiente
 * de actualizar la lista, comparando la lista antigua y la nueva y detectando solo los ítems
 * que han cambiado, se han añadido o se han eliminado. Esto evita que toda la lista se tenga que
 * redibujar, lo que mejora enormemente el rendimiento.
 */
class MediaDiffCallback : DiffUtil.ItemCallback<MediaItem>() {
    // Comprueba si dos ítems representan la misma entidad (normalmente, se usa el ID).
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.id == newItem.id
    }

    // Comprueba si los datos de dos ítems son exactamente los mismos.
    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem == newItem
    }
}