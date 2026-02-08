package com.example.aplicacionjuego.data.datasource

import org.json.JSONObject

enum class Estado {
    PENDIENTE, JUGANDO, PARADO, TERMINADO
}

object Juegos {
    // Cambiamos a una MutableList para que el repositorio pueda modificarla
    val juegos : MutableList<JSONObject> = mutableListOf(
        JSONObject().apply {
            put("id", "1")
            put("title", "The Legend of Zelda: Breath of the Wild")
            put("platform", "Nintendo Switch")
            put("portada", "https://comicstores.es/imagenes_grandes/5050574/505057434040.JPG")
            put("estado", Estado.PENDIENTE)
        },
        JSONObject().apply {
            put("id", "2")
            put("title", "Super Mario Odyssey")
            put("platform", "Nintendo Switch")
            put("portada", "https://media.vandal.net/m/43395/super-mario-odyssey-2017102611035_25.jpg")
            put("estado", Estado.PENDIENTE)
        },
    )
}
