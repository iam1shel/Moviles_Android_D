# Ghibli Films

**Alumna:** Rojas Tuesta Luz Mishel (trabajo individual)  
**Curso:** Programación en Móviles · Laboratorio 11-12  
**Tecnología:** Kotlin, Jetpack Compose + Material 3, Retrofit + kotlinx.serialization, Room, Firebase Authentication + Firestore, notificaciones locales  
**Patrón:** MVVM + Repository (interface)

Catálogo de películas de Studio Ghibli. La persona ingresa con correo y contraseña; su rol (admin o usuario) se lee de Firestore. La lista se descarga de internet, se guarda en Room y funciona sin conexión. Se pueden marcar favoritos y, al sincronizar, llega una notificación con cuántas películas se guardaron. El admin además tiene un panel con totales, Sincronizar y Limpiar caché.

## Tema y variante

| | |
|---|---|
| Tema | Películas de Studio Ghibli |
| API | [ghibliapi.vercel.app](https://ghibliapi.vercel.app/films) (pública, sin clave) |
| Lista | `GET /films` |
| Detalle | `GET /films/{id}` |
| Id | Texto (UUID), por eso el contrato usa `id: String` |

Room guarda solo los datos de la tarjeta (título, póster, director, año y puntaje). La sinopsis, el productor, la duración y el banner se piden al segundo endpoint al abrir el detalle.

## Cómo abrir y compilar

1. Android Studio → **File → Open** → `Semana_12/Laboratorio_12`.
2. Esperar la sincronización de Gradle (JDK 17 o superior; el daemon usa JDK 21).
3. Configurar Firebase (siguiente sección) y copiar `google-services.json` dentro de `app/`.
4. Ejecutar en un emulador **con Google Play** (Android 8.0 o superior).

Sin `google-services.json` el proyecto compila y abre igual, pero el login muestra «Firebase no está configurado».

Pruebas unitarias: `./gradlew testDebugUnitTest`.

## Configurar Firebase

1. En [console.firebase.google.com](https://console.firebase.google.com/) crear un proyecto.
2. Registrar una app Android con el paquete `com.tecsup.ghiblifilms`.
3. Descargar `google-services.json` y ponerlo en `app/` (no se sube: está en `.gitignore`).
4. **Authentication → Método de acceso:** activar Correo electrónico/contraseña.
5. **Authentication → Usuarios:** crear los dos usuarios de prueba:

   | Correo | Rol |
   |---|---|
   | `admin@ghibli.test` | admin |
   | `usuario@ghibli.test` | usuario |

6. **Firestore Database:** crear la base de datos y la colección `usuarios`. Un documento por usuario cuyo **ID es el UID** de Authentication, con los campos `rol` (`admin` o `usuario`) y `nombre`.
7. **Firestore → Reglas:**

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{uid} {
      allow read: if request.auth != null && request.auth.uid == uid;
      allow write: if false;
    }
  }
}
```

## Árbol de paquetes

```
com.tecsup.ghiblifilms
├── MainActivity.kt          permiso de notificaciones + AppNav
├── GhibliApp.kt             crea el AppContainer una sola vez
├── AppContainer.kt          API, base de datos, repositorios y notificador
├── data
│   ├── remote               ApiService, RetrofitClient, dto/ItemDto
│   ├── local                ItemEntity, ItemDao, AppDatabase
│   ├── auth                 AuthRepository, FirebaseAuthRepository, AuthSinConfigurar
│   ├── repository           ItemRepository, ItemRepositoryImpl
│   └── mapper               Mappers (Dto → Entity → Item)
├── domain/model             Item, Sesion + Rol
├── notification             Notificador, NotificadorAndroid
└── ui
    ├── navigation           AppNav (login ↔ catálogo, rutas por rol)
    ├── UiState.kt, Mensajes.kt
    ├── login                LoginScreen, LoginViewModel
    ├── lista                ListaScreen, ListaViewModel
    ├── detalle              DetalleScreen, DetalleViewModel
    ├── favoritos            FavoritosScreen, FavoritosViewModel
    ├── admin                AdminScreen, AdminViewModel
    ├── components           Cargando, ErrorView, VacioView, AvisoBanner, PeliculaCard
    └── theme
```

## Decisiones de arquitectura

- **Room es la fuente única de verdad.** `ItemRepository.items` sale de Room; Retrofit solo la actualiza con `refrescar()`.
- **Favoritos que sobreviven a la sincronización.** Antes del `@Upsert` se leen los ids favoritos y se vuelven a aplicar.
- **Sin conexión.** Si falla el refresco y hay datos guardados, se muestran con el aviso «Sin conexión: mostrando datos guardados». Si no hay nada, aparece el error con Reintentar.
- **Roles.** El rol se lee de `usuarios/{uid}` en Firestore. El icono del panel solo aparece para el admin y la ruta `admin` también valida el rol.
- **Notificación.** `ListaViewModel` y `AdminViewModel` solo conocen la interface `Notificador`. `NotificadorAndroid` crea el canal, revisa `POST_NOTIFICATIONS` y abre la app al tocarla. Si el permiso está negado, no muestra nada y la app sigue normal.
- **ViewModels sin Context.** Se crean con `viewModelFactory` a partir del `AppContainer`.

## Requisitos

| Cód. | Dónde |
|---|---|
| R01 | `LoginScreen`, `LoginViewModel`, `FirebaseAuthRepository` |
| R02 | `FirebaseAuthRepository.leerSesion`, `AppNav` |
| R03 | `ListaViewModel` + `Cargando`, `ErrorView`, `VacioView` |
| R04 | `ApiService.detalle`, `DetalleViewModel` |
| R05 | `UiState`, ViewModels, `ItemRepository` (interface) |
| R06 | `ItemEntity`, `ItemDao`, `AppDatabase` |
| R07 | `ItemDao.alternarFavorito`, `FavoritosScreen`, `ItemRepositoryImpl.refrescar` |
| R08 | `ListaViewModel.refrescar` + `AvisoBanner` |
| R09 | `NotificadorAndroid`, permiso en `MainActivity` |
| R10 | `AdminScreen`, `AdminViewModel` |
| R11 | `LoginViewModel` (sesión al abrir) y botón Cerrar sesión |
| R12 | `app/src/test`: `ListaViewModelTest` (7), `LoginViewModelTest` (5), `MappersTest` (4) |
| R13 | `PROMPTS.md` |

## Capturas

Pendientes: login, lista, detalle, favoritos, panel Admin y notificación (se toman con Firebase configurado).
