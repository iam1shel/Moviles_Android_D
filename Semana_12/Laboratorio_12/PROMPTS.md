# Prompts utilizados

Alumna: Rojas Tuesta Luz Mishel

## Consulta 1

- **Fecha:** 11/11/2026
- **Herramienta:** Cursor (agente)
- **Prompt:** «Necesito elaborar esto en la carpeta de la semana 12 [guía del Laboratorio 11-12]. Como el tema de la app es libre, ayúdame a elegir y planear eso primero.»
- **Qué respondió (resumen):** probó en vivo varias APIs públicas sin clave con lista y detalle (FreeToGame, Rick and Morty, TheMealDB, TheCocktailDB, Valorant, Makeup API, DummyJSON, Disney, Studio Ghibli). Descartó REST Countries v3.1 (deprecada), Jikan (sin respuesta) y Art Institute (403).
- **Qué usé / qué cambié y por qué:** elegí Studio Ghibli. Son 22 películas con póster, banner y sinopsis. Como el id es un UUID, el contrato usa `id: String`.
- **Qué aprendí:** antes de crear el DTO hay que abrir la URL y revisar los nombres reales de los campos (`rt_score`, `running_time` y `release_date` llegan como texto).

## Consulta 2

- **Fecha:** 11/11/2026
- **Herramienta:** Cursor (agente)
- **Prompt:** «Prepara el proyecto para abrirlo en Android Studio.»
- **Qué respondió (resumen):** proyecto base con las versiones de mi Laboratorio 08 (AGP 8.7.3, Kotlin 2.1.0, KSP 2.1.0-1.0.29, Room 2.6.1), Retrofit, Firebase y Coil; estructura de paquetes de la guía y contratos (`UiState`, `Sesion`, `AuthRepository`, `ItemRepository`, `Notificador`).
- **Qué usé / qué cambié y por qué:** el plugin `google-services` solo se aplica si existe `app/google-services.json`, para poder compilar antes de configurar Firebase sin subir la clave al repositorio.
- **Qué aprendí:** si el plugin se declara con versión en la raíz (`apply false`), en el módulo va sin versión.

## Consulta 3

- **Fecha:** 11/11/2026
- **Herramienta:** Cursor (agente)
- **Prompt:** «Completa el proyecto: implementa todos los requisitos, compílalo, ejecuta las pruebas y súbelo a la rama de la semana 12.»
- **Qué respondió (resumen):** capa de datos (Retrofit → Room), repositorio, login con rol desde Firestore, lista con estados Cargando/Error/Vacío, detalle con el segundo endpoint, favoritos, modo sin conexión, panel Admin, notificación local y 16 pruebas unitarias con repositorio y notificador falsos.
- **Qué usé / qué cambié y por qué:**
  - `ListaViewModel`: cuando el refresco termina y la pantalla sigue en Cargando, toma la lista actual de Room (`items.first()`) en lugar de poner una lista vacía, para que no parpadee el estado vacío.
  - `DetalleViewModel` combina el detalle de la API con `favoritos` de Room, así el corazón se actualiza al instante.
  - `AuthSinConfigurar` evita que la app se cierre con «Default FirebaseApp is not initialized» cuando falta el JSON.
- **Qué aprendí:** el `@Upsert` reemplaza la fila completa; si no se leen antes los ids favoritos, se pierden al sincronizar. La prueba `los favoritos se conservan al sincronizar otra vez` lo comprueba.
