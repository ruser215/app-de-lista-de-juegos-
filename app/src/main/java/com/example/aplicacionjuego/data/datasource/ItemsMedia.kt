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
        JSONObject().apply {
            put("id", "3")
            put("title", "Cyberpunk 2077")
            put("platform", "PC")
            put("portada", "https://media.vandal.net/m/20029/cyberpunk-2077-201961217172698_1.jpg")
            put("estado", Estado.COMENZADO.name)
            put("rating", 4.5f)
            put("opinion", "Night City es increíble.")
            put("categoria", Categoria.JUEGO.name)
        },
        JSONObject().apply {
            put("id", "4")
            put("title", "El Señor de los Anillos: La Comunidad del Anillo")
            put("platform", "Prime Video")
            put("portada", "https://es.web.img3.acsta.net/medias/nmedia/18/89/67/45/20061512.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 5.0f)
            put("opinion", "Una aventura épica.")
            put("categoria", Categoria.PELICULA.name)
        },
        JSONObject().apply {
            put("id", "5")
            put("title", "The Mandalorian")
            put("platform", "Disney+")
            put("portada", "https://es.web.img3.acsta.net/pictures/19/08/23/15/39/2728562.jpg?coixp=49&coiyp=62")
            put("estado", Estado.PENDIENTE.name)
            put("rating", 0.0f)
            put("opinion", "Tengo ganas de empezarla.")
            put("categoria", Categoria.SERIE.name)
        },
        JSONObject().apply {
            put("id", "6")
            put("title", "Stardew Valley")
            put("platform", "PC")
            put("portada", "https://i.3djuegos.com/juegos/12908/stardew_valley/fotos/ficha/stardew_valley-3324716.jpg")
            put("estado", Estado.PAUSADO.name)
            put("rating", 5.0f)
            put("opinion", "Relajante y adictivo.")
            put("categoria", Categoria.JUEGO.name)
        },
        JSONObject().apply {
            put("id", "7")
            put("title", "Pulp Fiction")
            put("platform", "Filmin")
            put("portada", "https://printler.com/media/photo/167927.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 4.0f)
            put("opinion", "Un clásico de Tarantino.")
            put("categoria", Categoria.PELICULA.name)
        },
        JSONObject().apply {
            put("id", "8")
            put("title", "Game of Thrones")
            put("platform", "Max")
            put("portada", "https://pics.filmaffinity.com/game_of_thrones_a_telltale_games_series-878553059-large.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 3.5f)
            put("opinion", "Increíble hasta la última temporada.")
            put("categoria", Categoria.SERIE.name)
        },
        JSONObject().apply {
            put("id", "9")
            put("title", "Hollow Knight")
            put("platform", "PC")
            put("portada", "https://pics.filmaffinity.com/Hollow_Knight-986585901-large.jpg")
            put("estado", Estado.COMENZADO.name)
            put("rating", 4.0f)
            put("opinion", "Difícil pero muy gratificante.")
            put("categoria", Categoria.JUEGO.name)
        },
        JSONObject().apply {
            put("id", "10")
            put("title", "Oppenheimer")
            put("platform", "Cine")
            put("portada", "https://es.web.img3.acsta.net/pictures/23/05/25/13/41/1835431.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 4.5f)
            put("opinion", "Intensa y muy bien dirigida.")
            put("categoria", Categoria.PELICULA.name)
        },
        JSONObject().apply {
            put("id", "11")
            put("title", "The Office")
            put("platform", "Netflix")
            put("portada", "https://es.web.img3.acsta.net/pictures/14/02/04/13/20/128334.jpg")
            put("estado", Estado.PAUSADO.name)
            put("rating", 5.0f)
            put("opinion", "Para ver una y otra vez.")
            put("categoria", Categoria.SERIE.name)
        },
        JSONObject().apply {
            put("id", "12")
            put("title", "Interstellar")
            put("platform", "Prime Video")
            put("portada", "https://es.web.img3.acsta.net/pictures/14/10/02/11/07/341344.jpg")
            put("estado", Estado.TERMINADO.name)
            put("rating", 5.0f)
            put("opinion", "Una maravilla visual y narrativa.")
            put("categoria", Categoria.PELICULA.name)
        }
    )
}