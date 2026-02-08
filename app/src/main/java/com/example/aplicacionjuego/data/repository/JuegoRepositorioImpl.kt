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
import javax.inject.Singleton

@Singleton
class JuegoRepositorioImpl @Inject constructor() : JuegoRepositorio {

    // Usamos un StateFlow interno para que el repositorio sea la única fuente de verdad reactiva
    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())

    init {
        // Al iniciar, cargamos los datos del JSON y los convertimos en nuestra lista de modelo
        _juegos.value = Juegos.juegos.map { it.toJuego() }
    }

    // --- Métodos de autenticación simulados ---
    override suspend fun loginUser(email: String, pass: String): Result<Boolean> = Result.success(true)
    override suspend fun registerUser(email: String, pass: String): Result<Boolean> = Result.success(true)

    // --- Métodos del repositorio que operan sobre la lista de objetos Juego ---

    override fun getAllJuegos(): Flow<List<Juego>> {
        return _juegos.asStateFlow()
    }

    override suspend fun addJuego(juego: Juego): Result<Unit> {
        val nuevoJuego = juego.copy(id = UUID.randomUUID().toString())
        // Actualizamos nuestra lista de modelo interna
        _juegos.update { it + nuevoJuego }
        // Actualizamos la fuente de datos JSON "original"
        Juegos.juegos.add(nuevoJuego.toJSONObject())
        return Result.success(Unit)
    }

    override suspend fun updateJuego(juego: Juego): Result<Unit> {
        // Actualizamos nuestra lista de modelo interna
        _juegos.update { list ->
            list.map { if (it.id == juego.id) juego else it }
        }
        // Actualizamos la fuente de datos JSON "original"
        val index = Juegos.juegos.indexOfFirst { it.getString("id") == juego.id }
        if (index != -1) {
            Juegos.juegos[index] = juego.toJSONObject()
        }
        return Result.success(Unit)
    }
}

// --- FUNCIONES DE MAPEO PRIVADAS ---

private fun JSONObject.toJuego(): Juego {
    return Juego(
        id = this.optString("id"),
        title = this.optString("title"),
        platform = this.optString("platform"),
        portada = this.optString("portada"),
        estado = Estado.valueOf(this.optString("estado", Estado.PENDIENTE.name))
    )
}

private fun Juego.toJSONObject(): JSONObject {
    // CORRECCIÓN: Eliminamos 'this' para acceder a las propiedades de Juego
    return JSONObject().apply {
        put("id", id)
        put("title", title)
        put("platform", platform)
        put("portada", portada)
        put("estado", estado.name)
    }
}