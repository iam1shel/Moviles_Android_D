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

Revisa el proyecto actual NavLab del Laboratorio 05 de Programación Móviles: Navegación en Jetpack Compose.

El proyecto ya fue creado y contiene las pantallas principales, pero necesito que verifiques que cumpla correctamente con los requisitos del laboratorio.

Primero analiza la estructura actual del proyecto y no generes código innecesario.

Revisa:

1. Compilación del proyecto:
- Corrige cualquier error de Gradle, dependencias o versiones.
- Mantén compatibilidad con Jetpack Compose y Navigation Compose.
- No actualices librerías sin necesidad.
- Verifica que compile correctamente.

2. Arquitectura:
Comprueba que exista una estructura organizada:

- navigation/
  - Screen.kt
  - AppNavigation.kt

- screens/
  - LoginScreen
  - HomeScreen
  - ListScreen
  - DetailScreen
  - ProfileScreen

Si la estructura está diferente, reorganízala sin romper funcionalidades.

3. Navegación:
Verifica que el flujo sea:

Portal Académico
↓
Bienvenido Juan León
↓
Directorio de Alumnos
↓
Expediente Académico

Y también:

Bienvenido Juan León
↓
Perfil Académico

Debe utilizar:
- NavController
- NavHost
- rutas mediante sealed class
- popBackStack()

4. Argumentos:
Verifica que el expediente académico reciba correctamente el id del alumno como Int usando NavType.IntType.

5. Interfaces:
Revisa que las pantallas tengan:

Login:
- correo institucional
- contraseña
- botón iniciar sesión

Home:
- bienvenida Juan León
- botones de navegación
- cerrar sesión

Directorio:
- lista de 5 alumnos
- navegación al seleccionar alumno

Expediente:
- código
- correo
- facultad
- biografía

Perfil:
- datos del estudiante Juan León

6. Diseño:
Mejora la interfaz usando Material3:
- Cards
- botones redondeados
- colores morados similares al ejemplo del laboratorio
- TopAppBar donde corresponda
- espaciado correcto

Al finalizar:
- indica qué archivos modificaste
- explica qué errores encontraste
- confirma si el proyecto queda listo para ejecutar en emulador.

No elimines funcionalidades existentes.

## Evidencias

Las capturas van en `docs/evidencias/`. El detalle de cada archivo está en `docs/evidencias/README.md`.
