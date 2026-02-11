package com.example.aplicacionjuego.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.ItemItemBinding
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.MediaItem

class MediaAdapter(
    private val onItemClick: (MediaItem) -> Unit,
    private val onDeleteClick: (MediaItem) -> Unit
) : ListAdapter<MediaItem, MediaAdapter.MediaViewHolder>(MediaDiffCallback()) {

    class MediaViewHolder(val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaViewHolder(binding)
    }

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

            root.setOnClickListener { onItemClick(item) }
            btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}

class MediaDiffCallback : DiffUtil.ItemCallback<MediaItem>() {
    override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
        return oldItem == newItem
    }
}