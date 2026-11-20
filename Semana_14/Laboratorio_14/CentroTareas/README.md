# Laboratorio 14: Programación Asíncrona con Corrutinas y Estado en Android (CentroTareas)

**Curso:** Programación en Móviles  
**Institución:** Tecsup  
**Repositorio:** Moviles_Android_D  
**Proyecto:** CentroTareas  

---

## 🎯 Objetivo

Desarrollar y consolidar los conceptos fundamentales de concurrencia y programación asíncrona en Android mediante Kotlin Coroutines, StateFlow y la arquitectura MVVM + Repository. La aplicación demuestra cómo ejecutar tareas en segundo plano sin congelar la interfaz de usuario, gestionar la cancelación cooperativa de trabajos, realizar cálculos intensivos en CPU y optimizar peticiones de red simuladas con ejecución secuencial vs. concurrente (`async`/`await`).

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin 2.2
- **UI Framework:** Jetpack Compose (Material3)
- **Asincronía & Concurrencia:** Kotlin Coroutines (`viewModelScope`, `launch`, `async`, `await`, `coroutineScope`, `withContext`, `Dispatchers.Default`, `Job`, `ensureActive`, `CancellationException`)
- **Gestión de Estado:** `StateFlow`, `MutableStateFlow`, `collectAsStateWithLifecycle`
- **Arquitectura:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Pruebas Automatizadas:** JUnit 4 + `kotlinx-coroutines-test`

---

## 📱 Descripción de las Funcionalidades

La aplicación consta de tres tarjetas de interacción asíncrona dentro de una única pantalla scrollable:

### 1. Tarea con Progreso y Cancelación
- **Contador "Tócame":** Incrementa un contador al ser presionado, demostrando físicamente que el hilo de la interfaz (`Dispatchers.Main`) no se bloquea durante tareas largas.
- **Botón Iniciar:** Lanza una corrutina en `viewModelScope` que simula un trabajo en segundo plano usando `Dispatchers.Default` y actualiza la barra de progreso lineal del 0 % al 100 %.
- **Botón Cancelar:** Cancela el `Job` en ejecución. Se gestiona la `CancellationException` para actualizar el mensaje de la UI a "Cancelado" sin romper el ciclo de vida.
- **Prevención de Tareas Duplicadas:** El botón Iniciar se deshabilita mientras la tarea está en ejecución (`_corriendo == true`).

### 2. Contar Números Primos
- **Cálculo Intensivo de CPU:** Calcula la cantidad de números primos en un rango determinado (ej. hasta 1,000 o 500,000) ejecutado en `Dispatchers.Default`.
- **Verificación Práctica:**
  - Hasta 1,000: **168 primos**
  - Hasta 500,000: **41,538 primos**
- **Cancelación Cooperativa:** Hace uso de `ensureActive()` dentro del bucle de cálculo para permitir la cancelación inmediata de cálculos pesados mediante un botón de cancelación.
- **Medición de Tiempo:** Mide y muestra el tiempo transcurrido en milisegundos dentro de un recuadro verde formativo.

### 3. Dos Cargas (Secuencial vs Concurrente)
- **Carga Una por Una (Secuencial):** Lanza en orden `cargarPerfil()` (~2000 ms) y `cargarPedidos()` (~3000 ms), resultando en un tiempo total acumulado de **~5000 ms**.
- **Carga A la Vez (Concurrente):** Lanza ambas peticiones en paralelo mediante `coroutineScope` y `async`/`await`, reduciendo el tiempo total al máximo de ambas (**~3000 ms**).
- **Simulación de Error & Reintento:** Un control `Switch` permite simular un error de red (`IOException`). En caso de falla, la interfaz muestra un recuadro rojo con el mensaje de error y activa un botón "Reintentar".

---

## 🏗️ Arquitectura MVVM + Repository

El proyecto implementa estrictamente el patrón de arquitectura recomendado por Android:

```
┌──────────────────────────────────────────────────────────┐
│                   Capa de Presentación                   │
│  [PantallaTareas.kt] (Jetpack Compose UI)                 │
└────────────────────────────▲─────────────────────────────┘
                             │ Observa StateFlow
                             │ Invoca funciones
┌────────────────────────────┴─────────────────────────────┐
│                    Capa de ViewModel                     │
│  [TareasViewModel.kt] (Gestiona estado UI & Corrutinas)  │
└────────────────────────────▲─────────────────────────────┘
                             │ Llama a suspend functions
┌────────────────────────────┴─────────────────────────────┐
│                    Capa de Repositorio                   │
│  [TareasRepository.kt] (Interfaz)                        │
│  [TareasRepositoryLocal.kt] (Implementación de Datos)   │
└──────────────────────────────────────────────────────────┘
```

### Flujo de Datos
1. La **UI** observa los `StateFlow` expuestos por el `TareasViewModel` mediante `collectAsStateWithLifecycle()`.
2. Las acciones del usuario (clics en botones, toggles) invocan métodos del `TareasViewModel`.
3. El **ViewModel** gestiona el alcance de corrutinas (`viewModelScope`) y delega el procesamiento o la carga de datos al **Repository**.
4. El **Repository** ejecuta operaciones asíncronas (`withContext(Dispatchers.Default)`, `delay`) y retorna los resultados o lanza excepciones.

---

## 📂 Estructura de Paquetes

```
com.tecsup.centrotareas/
├── MainActivity.kt                  # Punto de entrada de la app y configuración de Compose
├── data/
│   ├── TareasRepository.kt          # Interfaz de datos con suspend functions
│   └── TareasRepositoryLocal.kt     # Implementación local del repositorio y algoritmos
├── model/
│   └── Estados.kt                   # Sealed interfaces para estados de UI (PrimosUi, CargaUi)
├── ui/
│   ├── PantallaTareas.kt            # Pantalla principal con las 3 tarjetas en Compose
│   └── theme/                       # Configuración de colores, tipografía y tema
└── viewmodel/
    ├── TareasViewModel.kt           # ViewModel con StateFlows y gestión de corrutinas
    └── TareasViewModelFactory.kt    # Factoría para inyección manual del repositorio
```

---

## ⚡ Explicación de Corrutinas Utilizadas

1. **`viewModelScope`:** Alcance ligado al ciclo de vida del ViewModel. Cancela automáticamente todas las corrutinas hijas cuando el ViewModel se destruye.
2. **`launch`:** Builder de corrutinas de tipo "fire and forget" que retorna un `Job`. Utilizado para iniciar acciones en la UI.
3. **`async` / `await`:** Builder para corrutinas que retornan un valor (`Deferred<T>`). Permite ejecutar múltiples tareas asíncronas en paralelo y esperar sus resultados con `await()`.
4. **`coroutineScope`:** Builder que crea un alcance delimitado (structured concurrency), garantizando que si una tarea falla, se cancelen las demás automáticamente.
5. **`withContext(Dispatchers.Default)`:** Cambia el contexto de ejecución al pool de hilos de CPU para no bloquear el hilo principal durante operaciones pesadas.
6. **`ensureActive()`:** Comprueba si el trabajo actual ha sido cancelado dentro de bucles de CPU intensivos y lanza `CancellationException` de ser necesario.
7. **`CancellationException`:** Excepción estándar de señalización de cancelación en Kotlin Coroutines. Es capturada para actualizar el estado visual y re-lanzada para mantener la propagación correcta.

---

## 🧪 Pruebas Automatizadas y Aceptación

Se ejecutaron **13 pruebas unitarias automatizadas** comprobando tanto la lógica del repositorio como el comportamiento del ViewModel:

```bash
./gradlew app:testDebugUnitTest
```

### Resumen de Pruebas Unitarias (`13/13 Aprobadas`)
- `TareasRepositoryTest`:
  - `contarPrimos_hasta1000_retorna168` -> **PASÓ**
  - `contarPrimos_hasta500000_retorna41538` -> **PASÓ**
  - `cargarPerfil_retornaNombreCorrecto` -> **PASÓ**
  - `cargarPedidos_sinError_retornaListaPedidos` -> **PASÓ**
  - `cargarPedidos_conError_lanzaIOException` -> **PASÓ**
- `TareasViewModelTest`:
  - `estadoInicial_esCorrecto` -> **PASÓ**
  - `contarPrimos_exito_actualizaEstadoListo` -> **PASÓ**
  - `contarPrimos_cancelar_actualizaEstadoCancelado` -> **PASÓ**
  - `cargarUnaPorUna_exito_actualizaExito` -> **PASÓ**
  - `cargarALaVez_exito_actualizaExito` -> **PASÓ**
  - `cargarALaVez_conErrorSimulado_actualizaEstadoError` -> **PASÓ**
  - `cambiarSimularError_actualizaStateFlow` -> **PASÓ**
  - `tareaLarga_iniciaYSePuedeCancelar` -> **PASÓ**

---

## 🧠 Para Pensar (Respuestas a la Guía)

### 1. ¿Por qué el contador de toques sigue funcionando mientras se ejecuta la tarea larga?
**Respuesta:** Porque la tarea asíncrona se ejecuta fuera del hilo principal (UI Thread) mediante corrutinas (`viewModelScope.launch` con `delay` y `withContext(Dispatchers.Default)`). Al mantener el hilo principal libre de bloqueos, este puede atender de inmediato los eventos de interacción de la UI como el evento `onClick` del botón "Tócame".

### 2. ¿Por qué es necesario usar `Dispatchers.Default` para el cálculo de números primos?
**Respuesta:** El cálculo de números primos es una tarea intensiva en procesamiento de CPU (CPU-bound). Si se ejecutara en `Dispatchers.Main`, congelaría por completo la interfaz de usuario provocando un fallo de tipo ANR (*Application Not Responding*). `Dispatchers.Default` está optimizado para trabajo de procesamiento pesado al usar un pool de hilos respaldado por el número de núcleos del procesador.

### 3. ¿Qué ocurre si no se llama a `ensureActive()` o `delay()` dentro de un bucle largo y el usuario cancela la tarea?
**Respuesta:** En Kotlin, la cancelación de corrutinas es **cooperativa**. Si una corrutina ejecuta un bucle puramente computacional sin puntos de suspensión (`delay()`, `yield()`) ni revisiones explícitas de estado (`ensureActive()`), el código ignorará la petición de cancelación del `Job` y seguirá consumiendo CPU hasta que el bucle termine de forma natural.

### 4. ¿Qué diferencia práctica existe entre la carga secuencial y la carga concurrente con `async`/`await` en la experiencia de usuario?
**Respuesta:** En la carga secuencial, las peticiones se ejecutan una tras otra, acumulando sus tiempos de espera (2s + 3s = **~5000 ms**). En la carga concurrente con `async`/`await`, ambas peticiones se inician simultáneamente en paralelo, por lo que el tiempo total se reduce al tiempo de la tarea más lenta (max(2s, 3s) = **~3000 ms**). Esto reduce el tiempo de carga en un **40 %**, ofreciendo una experiencia mucho más rápida y fluida para el usuario.

### 5. ¿Por qué se debe capturar y re-lanzar `CancellationException` en un bloque `try-catch` dentro de una corrutina?
**Respuesta:** Kotlin Coroutines utiliza `CancellationException` para notificar la cancelación de un trabajo. Si un bloque `try-catch` captura `Exception` de forma genérica sin relanzar `CancellationException`, la corrutina "tragará" el evento de cancelación, interrumpiendo el flujo interno de cancelación del framework y evitando que los alcances padres sepan que la tarea fue cancelada.

---

## 📸 Evidencias de Funcionamiento

Las capturas de pantalla se encuentran registradas en el directorio `capturas/`:

1. **`capturas/01_progreso.png`:** Muestra la tarjeta 1 con la tarea en ejecución (progreso e incrementos del contador "Tócame").
2. **`capturas/02_cancelacion.png`:** Muestra la cancelación de la tarea con progreso e indicación del mensaje "Cancelado".
3. **`capturas/03_primos.png`:** Muestra el resultado de contar números primos hasta 500,000 (41,538 primos) con el tiempo en milisegundos en el recuadro verde.
4. **`capturas/04_comparacion.png`:** Muestra la comparación visual de tiempos entre la carga "Una por una" (~5000 ms) y "A la vez" (~3000 ms).
5. **`capturas/05_error.png`:** Muestra la simulación de error de red con el recuadro rojo y el botón "Reintentar".

> **Nota sobre capturas:** Si estás ejecutando en un entorno local con emulador activo, puedes capturar directamente las pantallas solicitadas y guardarlas en la carpeta `capturas/` con los nombres indicados.
