package com.example.aplicacionjuego.domain.model

import org.json.JSONObject

// El nuevo enum para la categoría
enum class Categoria {
    JUEGO, PELICULA, SERIE
}

// Hacemos el enum Estado más genérico
enum class Estado {
    PENDIENTE, EN_PROGRESO, TERMINADO, EN_PAUSA
}

// Renombramos la data class a MediaItem
data class MediaItem(
    val id: String = "",
    val title: String ="",
    val platform: String = "", // Ahora puede ser "Netflix", "PS5", "Cine", etc.
    val portada: String = "",
    val estado: Estado = Estado.PENDIENTE,
    val rating: Float = 0.0f,
    val opinion: String = "",
    val categoria: Categoria = Categoria.JUEGO // El nuevo campo
) {
    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("platform", platform)
            put("portada", portada)
            put("estado", estado.name)
            put("rating", rating)
            put("opinion", opinion)
            put("categoria", categoria.name) // Añadimos la categoría al JSON
        }
    }
}