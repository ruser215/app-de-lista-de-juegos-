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
    val estado: Estado = Estado.PENDIENTE
) {
    /**
     * Convierte este objeto Juego a un JSONObject.
     */
    fun toJSONObject(): JSONObject {
        // Dentro de 'apply', 'this' se refiere al JSONObject. Accedemos a las
        // propiedades de Juego directamente, sin 'this'.
        return JSONObject().apply {
            put("id", id)
            put("title", title)
            put("platform", platform)
            put("portada", portada)
            put("estado", estado.name)
        }
    }
}