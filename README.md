# Gestor de Contenido Multimedia (Juegos y más)

Esta aplicación es una herramienta desarrollada en Kotlin para Android que permite gestionar una colección personal de contenido multimedia, centrándose principalmente en videojuegos, aunque también permite organizar películas y series.

## Tecnologías y Arquitectura

Este proyecto ha sido desarrollado siguiendo estándares modernos de la industria, adecuados para un entorno de aprendizaje de Desarrollo de Aplicaciones Multiplataforma.

### MVVM (Model-View-ViewModel) y Clean Architecture
La estructura del proyecto se basa en el patrón MVVM y los principios de Clean Architecture para asegurar un código ordenado, mantenible y escalable.

*   **Capa de Presentación (Presentation)**:
    *   **Vistas (Activities/Fragments)**: Se encargan unicamente de mostrar la interfaz de usuario y observar los cambios en los datos. Utilizan *ViewBinding* para acceder a los elementos visuales de forma segura.
    *   **ViewModels**: Actúan como intermediarios entre la vista y la lógica de negocio. Exponen el estado de la UI mediante *LiveData* o *StateFlow* y manejan eventos de usuario, sobreviviendo a los cambios de configuración.

*   **Capa de Dominio (Domain)**:
    *   Contiene la lógica de negocio pura de la aplicación, independiente de cualquier framework de Android.
    *   **Casos de Uso (UseCases)**: Clases que encapsulan una única acción de negocio (ej. `AddItemUseCase`, `GetItemCaseUse`). Esto hace que la lógica sea reutilizable y fácil de probar.
    *   **Modelos**: Clases de datos simples (ej. `MediaItem`) que representan la información en la aplicación.

*   **Capa de Datos (Data)**:
    *   Implementa las interfaces de los repositorios definidos en la capa de dominio.
    *   Se encarga de decidir de dónde vienen los datos (en este caso, principalmente de Firebase) y cómo se guardan.

### Hilt (Inyección de Dependencias)
Para la gestión de dependencias se utiliza **Hilt**, la biblioteca recomendada por Google construida sobre Dagger.

*   **Propósito**: Facilita la provisión de objetos necesarios (como repositorios o instancias de Firebase) a las clases que los necesitan (como los ViewModels) sin que estas clases tengan que saber cómo crearlos.
*   **Beneficios**: Reduce el código repetitivo (boilerplate), desacopla las clases y hace que la aplicación sea mucho más fácil de testear y mantener.

### Firebase (Backend)
La aplicación se apoya en los servicios de Google Firebase para la gestión del backend, lo que permite centrarse en el desarrollo de la app cliente.

*   **Firebase Authentication**: Maneja todo el sistema de registro e inicio de sesión de usuarios, proporcionando un entorno seguro para la autenticación.
*   **Firebase Firestore**: Base de datos NoSQL en la nube. Se utiliza para almacenar la información de los items (título, plataforma, estado, puntuación, etc.) de forma sincronizada. Permite operaciones de lectura y escritura rápidas y escalables.

## Funcionalidades

La aplicación ofrece las siguientes características principales:

1.  **Autenticación de Usuarios**: Pantallas de Login y Registro para asegurar los datos de cada usuario.
2.  **Listado de Multimedia**: Visualización clara de los juegos, películas y series guardados.
3.  **Gestión de Items (CRUD)**:
    *   Crear nuevos registros con detalles como título, plataforma, imagen de portada, etc.
    *   Actualizar el estado (Pendiente, Comenzado, Terminado) y la valoración personal.
    *   Eliminar items de la colección.
4.  **Categorización**: Soporte para diferentes tipos de medios (Juego, Película, Serie).

## Video Demostrativo de la App

A continuación se presenta un video demostrando el funcionamiento y flujo de la aplicación:

[Video demostrativo](https://youtu.be/nqtqFnqJmVo)

