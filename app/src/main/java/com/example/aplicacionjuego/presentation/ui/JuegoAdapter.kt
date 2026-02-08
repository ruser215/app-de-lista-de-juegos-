package com.example.aplicacionjuego.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionjuego.R // <-- Importamos la clase R del proyecto
import com.example.aplicacionjuego.databinding.ItemJuegoBinding
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.Juego

class JuegoAdapter(
    private val onJuegoClick: (Juego) -> Unit // Para cuando pinches en el juego
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

            // 1. Cargar Imagen con Glide usando los nuevos drawables locales
            Glide.with(ivPortada.context)
                .load(juego.portada)
                .placeholder(R.drawable.ic_placeholder) // Usamos el placeholder local
                .error(R.drawable.ic_error)             // Usamos el icono de error local
                .into(ivPortada)

            // 2. Cambiar color según el Estado
            val color = when (juego.estado) {
                Estado.PENDIENTE -> android.R.color.holo_orange_light
                Estado.JUGANDO -> android.R.color.holo_green_dark
                Estado.PARADO -> android.R.color.holo_red_dark
                Estado.TERMINADO -> android.R.color.holo_blue_dark
            }
            tvEstado.backgroundTintList = ContextCompat.getColorStateList(tvEstado.context, color)

            // 3. Listener de Click
            root.setOnClickListener { onJuegoClick(juego) }
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