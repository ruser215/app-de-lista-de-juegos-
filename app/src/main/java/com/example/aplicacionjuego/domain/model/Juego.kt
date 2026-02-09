package com.example.aplicacionjuego.domain.model

import org.json.JSONObject

enum class Estado {
    PENDIENTE, JUGANDO, PARADO, TERMINADO
}

data class Juego(
    val id: String = "",
    val title: String ="",
    val platform: String = "",
    val portada: String = "",
    val estado: Estado = Estado.PENDIENTE,
    val rating: Float = 0.0f,      // Nuevo campo para la valoración
    val opinion: String = ""       // Nuevo campo para el comentario
) {
    /**
     * Convierte este objeto Juego a un JSONObject.
     */
    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("platform", platform)
            put("portada", portada)
            put("estado", estado.name)
            put("rating", rating)      // Añadimos el nuevo campo al JSON
            put("opinion", opinion)    // Añadimos el nuevo campo al JSON
        }
    }
}