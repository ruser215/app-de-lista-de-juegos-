package com.example.aplicacionjuego.data.repository

import com.example.aplicacionjuego.data.datasource.Juegos
import com.example.aplicacionjuego.domain.model.Estado
import com.example.aplicacionjuego.domain.model.Juego
import com.example.aplicacionjuego.domain.repository.JuegoRepositorio
import com.google.firebase.auth.FirebaseAuth // Importamos FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await // Importamos await
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

// Le inyectamos FirebaseAuth para que pueda gestionar la autenticación
class JuegoRepositorioLocalImpl @Inject constructor(
    private val auth: FirebaseAuth
) : JuegoRepositorio {

    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())

    init {
        _juegos.value = Juegos.juegos.map { it.toJuego() }
    }

    // --- LÓGICA DE AUTENTICACIÓN CON FIREBASE ---

    override suspend fun loginUser(email: String, pass: String): Result<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            if (result.user?.isEmailVerified == true) {
                Result.success(true)
            } else {
                auth.signOut()
                Result.failure(Exception("Debes verificar tu correo electrónico."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerUser(email: String, pass: String): Result<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            result.user?.sendEmailVerification()?.await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- LÓGICA DE JUEGOS (LOCAL) ---

    override fun getAllJuegos(): Flow<List<Juego>> {
        return _juegos.asStateFlow()
    }

    override suspend fun addJuego(juego: Juego): Result<Unit> {
        val nuevoJuego = juego.copy(id = UUID.randomUUID().toString())
        _juegos.update { it + nuevoJuego }
        Juegos.juegos.add(nuevoJuego.toJSONObject())
        return Result.success(Unit)
    }

    override suspend fun updateJuego(juego: Juego): Result<Unit> {
        _juegos.update { list ->
            list.map { if (it.id == juego.id) juego else it }
        }
        val index = Juegos.juegos.indexOfFirst { it.getString("id") == juego.id }
        if (index != -1) {
            Juegos.juegos[index] = juego.toJSONObject()
        }
        return Result.success(Unit)
    }

    override suspend fun deleteJuego(juego: Juego): Result<Unit> {
        _juegos.update { list ->
            list.filter { it.id != juego.id }
        }
        val index = Juegos.juegos.indexOfFirst { it.getString("id") == juego.id }
        if (index != -1) {
            Juegos.juegos.removeAt(index)
        }
        return Result.success(Unit)
    }
}

private fun JSONObject.toJuego(): Juego {
    return Juego(
        id = this.optString("id"),
        title = this.optString("title"),
        platform = this.optString("platform"),
        portada = this.optString("portada"),
        estado = Estado.valueOf(this.optString("estado", Estado.PENDIENTE.name))
    )
}
