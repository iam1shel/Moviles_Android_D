# Laboratorio 05 - Navegación en Jetpack Compose

Alumna: Rojas Tuesta Luz Mishel

## Descripción del proyecto

Portal Académico en Jetpack Compose. La aplicación entra por un login, muestra una bienvenida y desde ahí se abre el directorio de alumnos o el perfil académico. Al elegir un alumno se abre su expediente.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose 2.7.7

## Estructura principal del proyecto

```
com.example.semana05_navegacion/
├── MainActivity.kt
├── model/
│   └── Alumno.kt
├── navigation/
│   ├── Screen.kt
│   └── AppNavigation.kt
├── screens/
│   ├── LoginScreen.kt
│   ├── HomeScreen.kt
│   ├── ListScreen.kt
│   ├── DetailScreen.kt
│   └── ProfileScreen.kt
└── ui/
```

Android Studio → File → Open → `Semana_05/Laboratorio_05/NavLab`.

## Flujo de navegación

```
Login
  ↓
Home
  ↓
Directorio de Alumnos
  ↓
Expediente Académico
```

```
Home
  ↓
Perfil Académico
```

## Navegación implementada

- `NavHost` registra las pantallas y define el login como inicio.
- `NavController` mueve la app entre rutas.
- `Screen.kt` concentra las rutas en una sealed class.
- El expediente recibe el id del alumno como argumento entero con `NavType.IntType`.
- Cerrar sesión vuelve al login y limpia la pila con `popUpTo`.

## Prompt utilizado con IA

Mejora visualmente la app de navegación del laboratorio S05, hecha en Kotlin y Jetpack Compose con Material 3. Mantén la navegación: login, home, lista, detalle con argumento entero y perfil. No cambies el paquete `com.example.semana05_navegacion` ni Navigation Compose.

Convierte las pantallas simples del laboratorio en un portal académico con fondo en degradado lila:

- Login: tarjeta blanca titulada Portal Académico, correo institucional, contraseña y botón INICIAR SESIÓN.
- Home: Bienvenido, Juan León, con dos tarjetas (Directorio de Alumnos y Mi Perfil Académico) y el texto Cerrar Sesión Segura.
- Lista: Directorio de Alumnos, con cinco estudiantes, avatar, carrera y flecha.
- Detalle: Expediente Académico del alumno seleccionado, con código, correo, facultad y biografía.
- Perfil: Configuración de Perfil de Juan León Suiyon, con datos personales, datos académicos y botón Cerrar Sesión.

Si en Android Studio se usó otro texto, reemplaza este bloque por ese prompt.

## Evidencias

Las capturas van en `docs/evidencias/`. El detalle de cada archivo está en `docs/evidencias/README.md`.
