package com.example.aplicacionjuego.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.ItemItemBinding // 1. Usamos el ViewBinding correcto
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem // 2. Usamos el modelo correcto

// 3. Renombramos la clase
class MediaAdapter(
    private val onItemClick: (MediaItem) -> Unit,
    private val onDeleteClick: (MediaItem) -> Unit
) : ListAdapter<MediaItem, MediaAdapter.MediaViewHolder>(MediaDiffCallback()) {

    // 4. Renombramos el ViewHolder y actualizamos su binding
    class MediaViewHolder(val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val item = getItem(position) // Ahora es un MediaItem
        with(holder.binding) {
            tvTitulo.text = item.title
            tvPlataforma.text = item.platform
            tvEstado.text = item.estado.name

            Glide.with(ivPortada.context)
                .load(item.portada)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(ivPortada)

            val color = when (item.estado) {
                Estado.PENDIENTE -> android.R.color.holo_orange_light
                Estado.COMENZADO -> android.R.color.holo_green_dark
                Estado.PAUSADO -> android.R.color.holo_red_dark
                Estado.TERMINADO -> android.R.color.holo_blue_dark
            }
            tvEstado.backgroundTintList = ContextCompat.getColorStateList(tvEstado.context, color)

            root.setOnClickListener { onItemClick(item) }
            btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}

// 5. Actualizamos el DiffCallback para que use MediaItem
class MediaDiffCallback : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem == newItem
    }
}