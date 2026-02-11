package com.example.aplicacionjuego

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Esta es la clase de Aplicación personalizada. Es el punto de entrada de la app y el lugar
 * donde se inicializan las librerías que necesitan un contexto global, como Hilt.
 *
 * En el `AndroidManifest.xml`, se debe declarar esta clase en la etiqueta `<application>`
 * con `android:name=".MiItemApp"` para que el sistema la use en lugar de la clase Application por defecto.
 */
@HiltAndroidApp
/**
 * Esta anotación le dice a Hilt que esta es la clase de la que debe partir para generar
 * el grafo de dependencias. Hilt creará un contenedor de dependencias a nivel de aplicación
 * que estará disponible durante todo el ciclo de vida de la app.
 *
 * Es el punto de partida para la inyección de dependencias en todo el proyecto.
 */
class MiItemApp : Application()