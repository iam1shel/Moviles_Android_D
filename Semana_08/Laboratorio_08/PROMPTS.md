# Prompts utilizados

Alumna: Rojas Tuesta Luz Mishel

## Prompt 1

**Texto enviado a Gemini:**

```
Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un
VetRepository (interface). Agrega en la ficha de la mascota un grafico
de linea con el peso registrado en cada cita ATENDIDA. La consulta va
en CitaDao, la funcion en VetRepository y su implementacion, y la lista
de puntos se expone como StateFlow en MascotaViewModel. La View solo
dibuja el grafico con Canvas. No uses XML ni librerias externas.
Muestrame solo los archivos que cambian.
```

**Que genero la IA:** consulta `observarPesoHistorial` en `CitaDao`, función en la interface `VetRepository` y en `VetRepositoryRoom`, `StateFlow` en `MascotaViewModel` y `GraficoLineas` en la pestaña Datos de `FichaMascotaScreen`.

**Que tuve que corregir yo:** el mapeo de columnas (`pesoRegistrado AS peso`) para el POJO `PuntoPeso` y el caso de lista vacía para no dividir entre cero.

**Respeta MVVM?:** Si. La View solo dibuja; el ViewModel no conoce el DAO; la consulta se declaró primero en la interface.

## Prompt 2

**Texto enviado a Gemini:**

```
En HuellitasVet (Compose, MVVM, Room, VetRepository interface) agrega
reprogramar cita. El dueño cambia fecha y hora de una cita PENDIENTE o
CONFIRMADA. Debe respetar horarios libres (una cita no cancelada por
horario) y la regla de 2 horas. La actualizacion va en CitaDao, la
funcion en VetRepository y MisCitasViewModel. La View muestra un
dialogo con FechaChip y HoraChip. No uses XML.
```

**Que genero la IA:** `reprogramar` en el DAO, `reprogramarCita` en el repository, `reprogramar()` en `MisCitasViewModel` y un diálogo en `MisCitasScreen`.

**Que tuve que corregir yo:** validar de nuevo si el horario sigue ocupado antes de guardar, y no mostrar Reprogramar si faltan 2 horas o menos (misma regla que Cancelar).

**Respeta MVVM?:** Si. La View no consulta el DAO; el ViewModel llama a la interface.

## Prompt 3

**Texto enviado a Gemini:**

```
En PacientesScreen del veterinario, filtra la lista de MascotaConDueno
por texto (nombre de mascota o de dueño) y por especie al mismo tiempo
usando combine en PacientesViewModel. Chips Todos / Perros / Gatos /
Otros. La View solo observa el StateFlow filtrado. No uses DAO ni XML.
```

**Que genero la IA:** `consulta` y `especie` como `MutableStateFlow` combinados con `observarTodasLasMascotas()`.

**Que tuve que corregir yo:** el filtro de texto en minúsculas y tratar `especie == null` como Todos.

**Respeta MVVM?:** Si. El filtrado vive en el ViewModel; la View no conoce Room.

## Reflexion (5 lineas)

- Que mejoro la IA y que no? Armó rápido las firmas de DAO/Repository/ViewModel, pero dejó huecos en validaciones (horario ocupado, lista vacía del gráfico).
- Que error cometio? En el gráfico no cubrió división por cero ni el alias SQL del peso.
- Que aprendi sobre MVVM, Room y reglas de negocio? La View no decide reglas; el ViewModel valida; Room persiste. Si agregas una función, va primero en la interface. Las reglas de 2 horas y horario único no pertenecen al DAO.
