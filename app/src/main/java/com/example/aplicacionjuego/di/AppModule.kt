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

/**
 * Un Módulo de Hilt es una clase que le enseña a Hilt cómo proporcionar instancias de ciertos tipos.
 * Es como un manual de instrucciones para la inyección de dependencias.
 *
 * @InstallIn(SingletonComponent::class) le dice a Hilt que las dependencias definidas aquí
 * vivirán mientras la aplicación esté viva (son "singletons").
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * La anotación @Provides se usa en funciones dentro de un módulo para decirle a Hilt cómo crear
     * una instancia de una clase que no podemos controlar directamente (como FirebaseAuth, que es de una librería externa).
     *
     * @Singleton asegura que Hilt creará UNA ÚNICA instancia de FirebaseAuth para toda la aplicación.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Este método le dice a Hilt cómo construir la implementación concreta de nuestro repositorio.
     * Como MediaRepositoryLocalImpl necesita una instancia de FirebaseAuth, Hilt buscará un proveedor
     * (el método de arriba, provideFirebaseAuth) y se la pasará automáticamente como parámetro.
     */
    @Provides
    @Singleton
    fun provideMediaRepositoryLocalImpl(auth: FirebaseAuth): MediaRepositoryLocalImpl {
        return MediaRepositoryLocalImpl(auth)
    }
}

/**
 * A menudo, es una buena práctica separar las "vinculaciones" (bindings) de las "provisiones" (provides).
 * Este módulo abstracto se encarga de decirle a Hilt qué implementación concreta debe usar cuando
 * una clase pide una interfaz.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * La anotación @Binds es más eficiente que @Provides. Se usa en métodos abstractos para decirle a Hilt:
     * "Cuando una clase pida una dependencia de tipo `MediaRepository` (la interfaz), entrégale la
     * implementación que te paso como parámetro (`impl: MediaRepositoryLocalImpl`)".
     *
     * Esto nos permite programar contra interfaces (una buena práctica) y dejar que Hilt se encargue
     * de inyectar la implementación correcta.
     */
    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryLocalImpl): MediaRepository
}