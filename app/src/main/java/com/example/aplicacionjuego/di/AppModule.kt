package com.example.aplicacionjuego.di

import com.example.aplicacionjuego.data.repository.MediaRepositoryLocalImpl
import com.example.aplicacionjuego.domain.repository.MediaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    // Este provider solo se encarga de construir la implementación concreta
    @Provides
    @Singleton
    fun provideMediaRepositoryLocalImpl(auth: FirebaseAuth): MediaRepositoryLocalImpl {
        return MediaRepositoryLocalImpl(auth)
    }
}

// Este módulo abstracto le dice a Hilt qué implementación usar para la interfaz
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryLocalImpl): MediaRepository
}