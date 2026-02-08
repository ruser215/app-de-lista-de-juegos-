package com.example.aplicacionjuego.data.repository

import com.example.aplicacionjuego.data.datasource.Juegos
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.domain.repository.JuegoRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

// Esta implementación actúa como traductor entre la fuente de datos JSON y el modelo de la app
class JuegoRepositorioLocalImpl @Inject constructor() : JuegoRepositorio {

    // StateFlow interno que contendrá la lista de objetos JUEGO (no JSON)
    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())

    init {
        // Al iniciar, leemos la lista de JSON, la transformamos y la cargamos
        _juegos.value = Juegos.juegos.map { it.toJuego() }
    }

    override suspend fun loginUser(email: String, pass: String): Result<Boolean> = Result.success(true)

    override suspend fun registerUser(email: String, pass: String): Result<Boolean> = Result.success(true)

    override fun getAllJuegos(): Flow<List<Juego>> {
        // La UI observará este Flow, que ya contiene la lista de objetos Juego
        return _juegos.asStateFlow()
    }

    override suspend fun addJuego(juego: Juego): Result<Unit> {
        val nuevoJuego = juego.copy(id = UUID.randomUUID().toString())
        
        // 1. Actualizamos la lista de modelo interna para que la UI reaccione
        _juegos.update { it + nuevoJuego }
        
        // 2. Actualizamos la fuente de datos JSON "original"
        Juegos.juegos.add(nuevoJuego.toJSONObject())
        
        return Result.success(Unit)
    }

    override suspend fun updateJuego(juego: Juego): Result<Unit> {
        // 1. Actualizamos la lista de modelo interna
        _juegos.update { list ->
            list.map { if (it.id == juego.id) juego else it }
        }
        
        // 2. Actualizamos la fuente de datos JSON "original"
        val index = Juegos.juegos.indexOfFirst { it.getString("id") == juego.id }
        if (index != -1) {
            Juegos.juegos[index] = juego.toJSONObject()
        }
        return Result.success(Unit)
    }
}

/**
 * Función de extensión para convertir un JSONObject en un objeto de nuestro modelo Juego.
 * Es privada para que solo el repositorio pueda usarla.
 */
private fun JSONObject.toJuego(): Juego {
    return Juego(
        id = this.optString("id"),
        title = this.optString("title"),
        platform = this.optString("platform"),
        portada = this.optString("portada"),
        estado = Estado.valueOf(this.optString("estado", Estado.PENDIENTE.name))
    )
}
