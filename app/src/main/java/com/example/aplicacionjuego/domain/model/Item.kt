package com.example.aplicacionjuego.domain.model

import org.json.JSONObject

enum class Categoria {
    JUEGO, PELICULA, SERIE
}

enum class Estado {
    PENDIENTE, COMENZADO, TERMINADO, PAUSADO
}

data class MediaItem(
    val id: String = "",
    val title: String ="",
    val platform: String = "",
    val portada: String = "",
    val estado: Estado = Estado.PENDIENTE,
    val rating: Float = 0.0f,
    val opinion: String = "",
    val categoria: Categoria = Categoria.JUEGO
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
            put("categoria", categoria.name)
        }
    }
}