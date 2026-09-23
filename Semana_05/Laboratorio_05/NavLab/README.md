# Laboratorio S05

Alumna: Rojas Tuesta Luz Mishel

## Descripción del proyecto

Portal académico en Jetpack Compose. La app entra por un login, muestra una bienvenida y permite abrir el directorio de alumnos o el perfil académico. Al elegir un alumno se abre su expediente. El identificador viaja como entero con `NavType.IntType`.

Cerrar sesión vuelve al login y limpia el historial de pantallas con `popUpTo`, para no dejar pantallas anteriores en la pila.

## Tecnologías utilizadas

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose 2.7.7

## Cómo abrirlo

Android Studio → File → Open → `Semana_05/Laboratorio_05/NavLab`.

Paquete: `com.example.semana05_navegacion`.

## Flujo

1. Portal Académico: iniciar sesión navega a Home.
2. Home: tarjetas hacia el directorio y el perfil, y cerrar sesión.
3. Directorio de alumnos: lista en `LazyColumn`.
4. Expediente académico: recibe `itemId` como `Int`.
5. Perfil académico: datos de Juan León Suiyon y cerrar sesión.

## Prompt utilizado para mejorar la interfaz

Mejora visualmente la app de navegación del laboratorio S05, hecha en Kotlin y Jetpack Compose con Material 3. Mantén la navegación: login, home, lista, detalle con argumento entero y perfil. No cambies el paquete `com.example.semana05_navegacion` ni Navigation Compose.

Convierte las pantallas simples del laboratorio en un portal académico con fondo en degradado lila:

- Login: tarjeta blanca titulada Portal Académico, correo institucional, contraseña y botón INICIAR SESIÓN.
- Home: Bienvenido, Juan León, con dos tarjetas (Directorio de Alumnos y Mi Perfil Académico) y el texto Cerrar Sesión Segura.
- Lista: Directorio de Alumnos, con cinco estudiantes, avatar, carrera y flecha.
- Detalle: Expediente Académico del alumno seleccionado, con código, correo, facultad y biografía.
- Perfil: Configuración de Perfil de Juan León Suiyon, con datos personales, datos académicos y botón Cerrar Sesión.

Si en Android Studio se usó otro texto, reemplaza este bloque por ese prompt.

## Evidencias

Coloca las capturas en `docs/evidencias/`:

- `01-inicial.png`: pantalla de inicio (Portal Académico).
- `02-home.png`: bienvenida.
- `03-directorio.png`: directorio de alumnos.
- `04-detalle.png`: expediente de un alumno.
- `05-perfil.png`: perfil académico.

Después de guardar esas imágenes, agrégalas al README con `![descripcion](docs/evidencias/nombre.png)`.
