package com.example.aplicacionjuego.di

import com.example.aplicacionjuego.data.repository.JuegoRepositorioImpl
import com.example.aplicacionjuego.data.repository.JuegoRepositorioLocalImpl // Importamos la nueva clase
import com.example.aplicacionjuego.domain.repository.JuegoRepositorio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Proveedores de Firebase (se mantienen por si quieres volver a usarlos) ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    // --- Proveedor del Repositorio ---
    @Provides
    @Singleton
    fun provideGameRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): JuegoRepositorio {
        
        // --- Opción 1: Implementación Local (ACTUALMENTE ACTIVA) ---
        // Hilt inyectará la versión que usa la lista en memoria.
        return JuegoRepositorioLocalImpl()

        // --- Opción 2: Implementación con Firebase (COMENTADA) ---
        // Para volver a Firebase, comenta la línea de arriba y descomenta la de abajo.
        // return JuegoRepositorioImpl(auth, db)
    }
}