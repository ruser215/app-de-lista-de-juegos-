package com.example.aplicacionjuego.di

import com.example.aplicacionjuego.data.repository.JuegoRepositorioImpl
import com.example.aplicacionjuego.data.repository.JuegoRepositorioLocalImpl
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

    // Proveedor de FirebaseAuth, necesario para la autenticación
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Proveedor del Repositorio. Ahora inyecta FirebaseAuth en la implementación local.
    @Provides
    @Singleton
    fun provideGameRepository(auth: FirebaseAuth): JuegoRepositorio {
        // La app usará la implementación local, que ahora contiene la lógica de Firebase Auth.
        return JuegoRepositorioLocalImpl(auth)
    }
}