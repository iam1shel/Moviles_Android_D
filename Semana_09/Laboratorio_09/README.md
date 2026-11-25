# MuroCampus — Servicio Web (Con Mejoras Adicionales)

## Descripción del proyecto

**MuroCampus — Servicio Web** es una aplicación Android desarrollada para el Laboratorio 09 del curso de Programación en Móviles (Tecsup). Permite listar publicaciones desde el servicio web JSONPlaceholder, visualizar el detalle de una publicación junto con sus comentarios, filtrar las publicaciones por usuario, buscar por título en tiempo real, crear nuevas publicaciones y eliminarlas mediante peticiones DELETE.

## Tecnologías utilizadas

- Lenguaje: **Kotlin 2.1.0**
- UI: **Jetpack Compose** (Material 3, Material You)
- Red y Serialización: **Retrofit 2.11.0**, **OkHttp 4.12.0** (con Logging Interceptor y Custom Accept Interceptor), **kotlinx.serialization**
- Arquitectura: **MVVM + Repository**, Corrutinas y Flows (`StateFlow`, `collectAsStateWithLifecycle`)
- API REST: **JSONPlaceholder** (`https://jsonplaceholder.typicode.com/`)

## Mejoras Adicionales Implementadas

1. **Buscador de publicaciones por título:**
   - **Funcionamiento:** Permite filtrar en tiempo real las publicaciones descargadas mediante un campo de texto con icono de lupa y botón "X" para limpiar. Funciona sin distinguir mayúsculas/minúsculas y es compatible con los filtros por usuario. Si no hay resultados, muestra el mensaje "No se encontraron publicaciones".
   - **Archivos involucrados:** `PantallaLista.kt`, `PostsViewModel.kt`.

2. **Interceptor HTTP personalizado:**
   - **Funcionamiento:** Interceptor de OkHttp (`AcceptJsonInterceptor`) que añade automáticamente el encabezado `Accept: application/json` a todas las peticiones salientes de `PostApi` sin duplicar cabeceras y coexistiendo con el `HttpLoggingInterceptor`.
   - **Archivos involucrados:** `AcceptJsonInterceptor.kt`, `RetrofitClient.kt`.

3. **Eliminación de publicaciones mediante DELETE:**
   - **Funcionamiento:** Permite eliminar una publicación desde la pantalla de detalle mediante un botón rojo con icono de papelera y un diálogo de confirmación (`AlertDialog`). Tras una respuesta HTTP exitosa, remueve la publicación de la lista local y regresa a la pantalla principal.
   - **Archivos involucrados:** `PostApi.kt`, `PostRepository.kt`, `PostsViewModel.kt`, `PantallaDetalle.kt`.

## Estructura de paquetes

```text
com.tecsup.murocampus/
├── MainActivity.kt
├── model/
│   └── Modelos.kt
├── data/
│   ├── remote/
│   │   ├── PostApi.kt
│   │   ├── RetrofitClient.kt
│   │   └── AcceptJsonInterceptor.kt
│   └── PostRepository.kt
├── viewmodel/
│   ├── PostsViewModel.kt
│   ├── PostsViewModelFactory.kt
│   └── UiState.kt
└── ui/
    ├── Componentes.kt
    ├── PantallaLista.kt
    ├── PantallaDetalle.kt
    ├── PantallaCrear.kt
    └── theme/
```

## Limitaciones de JSONPlaceholder

JSONPlaceholder es un entorno de prueba simulado (`mock`). Las operaciones `POST` devuelven el ID `101` y las operaciones `DELETE` devuelven éxito HTTP `200/204`, pero **no almacenan ni eliminan de forma persistente** los datos en un servidor real. Por esta razón, tras crear o eliminar un elemento, una recarga posterior desde el servidor volverá a mostrar los datos originales precargados.

## Capturas del emulador

| Captura | Espacio / Estado | Qué aparece |
| --- | --- | --- |
| Lista | [Insertar captura aquí] | Barra MuroCampus, buscador, chips de filtro y tarjetas con etiquetas de usuario. |
| Detalle | [Insertar captura aquí] | Publicación completa, comentarios y botón rojo de eliminación. |
| Filtro | [Insertar captura aquí] | Chip de usuario activo y publicaciones filtradas. |
| Crear | [Insertar captura aquí] | Formulario de creación y mensaje de éxito de publicación. |
| Error de conexión | [Insertar captura hier] | Mensaje de error de red y botón Reintentar. |

## Instrucciones de ejecución

1. Abrir la carpeta del proyecto en Android Studio.
2. Sincronizar el proyecto con Gradle (`Sync Project with Gradle Files`).
3. Ejecutar la aplicación en un emulador o dispositivo físico Android con conexión a internet.

## Registro de asistencia de IA

Desarrollo guiado e implementado con la asistencia de Inteligencia Artificial (Gemini) conforme a los lineamientos del Laboratorio 09 de Programación en Móviles (Tecsup), con registro detallado en `PROMPTS.md`.
