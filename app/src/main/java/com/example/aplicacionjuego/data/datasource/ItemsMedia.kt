package com.example.aplicacionjuego.data.datasource

import com.example.aplicacionjuego.domain.model.Categoria
import com.example.aplicacionjuego.domain.model.Estado
import org.json.JSONObject

object ItemsMedia {
    val juegos : MutableList<JSONObject> = mutableListOf(
        JSONObject().apply {
            put("id", "1")
            put("title", "The Legend of Zelda: Breath of the Wild")
            put("platform", "Nintendo Switch")
            put("portada", "https://art.gametdb.com/switch/cover/us/lztda.jpg")
            put("estado", Estado.EN_PROGRESO.name)
            put("rating", 5.0f)
            put("opinion", "Una obra maestra. Cien horas y las que me quedan.")
            put("categoria", Categoria.JUEGO.name)
        },
        JSONObject().apply {
            put("id", "2")
            put("title", "Breaking Bad")
            put("platform", "Netflix")
            put("portada", "https://www.themoviedb.org/t/p/original/ztkUQFLlC19CCMYHW9o1zWhJEnq.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 5.0f)
            put("opinion", "La mejor serie que he visto nunca.")
            put("categoria", Categoria.SERIE.name) 
        },
    )
}