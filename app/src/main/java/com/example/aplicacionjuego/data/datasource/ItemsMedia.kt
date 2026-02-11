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
            put("portada", "https://bonewall.es/wp-content/uploads/2024/02/poster-zelda-breath-of-the-wild.jpg")
            put("estado", Estado.PAUSADO.name)
            put("rating", 5.0f)
            put("opinion", "Una obra maestra. Cien horas y las que me quedan.")
            put("categoria", Categoria.JUEGO.name)
        },
        JSONObject().apply {
            put("id", "2")
            put("title", "Breaking Bad")
            put("platform", "Netflix")
            put("portada", "https://es.web.img3.acsta.net/pictures/18/04/04/22/52/3191575.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 5.0f)
            put("opinion", "La mejor serie que he visto nunca.")
            put("categoria", Categoria.SERIE.name) 
        },
    )
}