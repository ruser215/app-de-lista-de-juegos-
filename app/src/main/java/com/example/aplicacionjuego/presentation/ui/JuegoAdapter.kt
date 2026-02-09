package com.example.aplicacionjuego.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R
import com.example.aplicacionjuego.databinding.ItemJuegoBinding
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.Juego

class JuegoAdapter(
    private val onJuegoClick: (Juego) -> Unit,       // Lambda para el clic en el item
    private val onDeleteClick: (Juego) -> Unit      // Lambda para el clic en el botón de eliminar
) : ListAdapter<Juego, JuegoAdapter.JuegoViewHolder>(JuegoDiffCallback()) {

    class JuegoViewHolder(val binding: ItemJuegoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegoViewHolder {
        val binding = ItemJuegoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JuegoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JuegoViewHolder, position: Int) {
        val juego = getItem(position)
        with(holder.binding) {
            tvTitulo.text = juego.title
            tvPlataforma.text = juego.platform
            tvEstado.text = juego.estado.name

            Glide.with(ivPortada.context)
                .load(juego.portada)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(ivPortada)

            val color = when (juego.estado) {
                Estado.PENDIENTE -> android.R.color.holo_orange_light
                Estado.JUGANDO -> android.R.color.holo_green_dark
                Estado.PARADO -> android.R.color.holo_red_dark
                Estado.TERMINADO -> android.R.color.holo_blue_dark
            }
            tvEstado.backgroundTintList = ContextCompat.getColorStateList(tvEstado.context, color)

            // Asignamos los listeners
            root.setOnClickListener { onJuegoClick(juego) }
            btnDelete.setOnClickListener { onDeleteClick(juego) }
        }
    }
}

class JuegoDiffCallback : DiffUtil.ItemCallback<Juego>() {
    override fun areItemsTheSame(oldItem: Juego, newItem: Juego): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Juego, newItem: Juego): Boolean {
        return oldItem == newItem
    }
}