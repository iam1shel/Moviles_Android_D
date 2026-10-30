# HuellitasVet

**Alumna:** Rojas Tuesta Luz Mishel  
**Curso:** Programación en Móviles · Semana 08  
**Tecnología:** Kotlin + Jetpack Compose + Material 3, Room 2.6.1, DataStore, Navigation Compose  
**Patrón:** MVVM + Repository (interface)

App de una veterinaria con tres perfiles en el mismo dispositivo. El dueño registra mascotas, pide citas y ve el historial clínico. El veterinario ve la agenda, confirma, atiende y administra servicios. El administrador registra mascotas y elige la raza de una lista. Los tres comparten la misma base Room (`huellitasvet.db`). Datos ficticios (Ley 29733).

Cuentas de prueba:

- Dueña: `ana@correo.test` / `1234`
- Dueño: `luis@correo.test` / `1234`
- Veterinaria: `vet@huellitas.test` / `admin`
- Administradora: `admin@huellitas.test` / `admin123` (código `ADM-001`, Camila Torres)

Android Studio → File → Open → `Semana_08/Laboratorio_08`.

## Árbol de paquetes

```
com.tecsup.huellitasvet
├── MainActivity.kt
├── VetApp.kt
├── data
│   ├── local
│   │   ├── entity          Servicio, Usuario, Mascota, Cita
│   │   ├── dao             ServicioDao, UsuarioDao, MascotaDao, CitaDao
│   │   ├── relation        MascotaConDueno, CitaConDetalle
│   │   ├── VetDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── repository          VetRepository, VetRepositoryRoom
│   └── prefs               PreferenciasStore
├── model                   Rol, Razas, EstadoCita, Especie, Sexo, Horarios, ConteoServicio, UltimaVisita
├── viewmodel
└── ui
    ├── theme
    ├── components          (+ graficos/)
    ├── navigation          Rutas, NavVet
    ├── dueno
    ├── veterinario
    └── administrador
```

## Diagrama MVVM

```
VIEW (Composable)
│ eventos (clics)          ▲ collectAsState()
▼                          │
VIEWMODEL (StateFlow + reglas)
│ llama                    ▲ Flow / resultados
▼                          │
VetRepository (interface)
│ la implementa
▼
VetRepositoryRoom ──► DAO ──► SQLite (huellitasvet.db)
DataStore (solo preferencias) ──► ViewModel
```

La View no usa DAO ni Repository. El ViewModel solo conoce `VetRepository`. Lectura que actualiza la pantalla → `Flow`. Escritura o acción única → `suspend`.

## Capturas

6 capturas (3 del dueño y 3 del veterinario) en `docs/capturas/`.

### Dueño

| Login | Inicio | Pedir cita |
| --- | --- | --- |
| ![Login](docs/capturas/dueno_login.png) | ![Inicio](docs/capturas/dueno_inicio.png) | ![Pedir cita](docs/capturas/dueno_pedir_cita.png) |

### Veterinario

| Panel | Agenda | Servicios |
| --- | --- | --- |
| ![Panel](docs/capturas/vet_panel.png) | ![Agenda](docs/capturas/vet_agenda.png) | ![Servicios](docs/capturas/vet_servicios.png) |

## Pruebas de aceptación

| Prueba | Resultado esperado |
| --- | --- |
| Login con clave incorrecta | Mensaje de error, no entra |
| Dueño: registrar mascota sin nombre | Errores junto a cada campo, no se guarda |
| Dueño: eliminar una mascota con citas | Mensaje de aviso y no se elimina |
| Dueño: pedir cita | Aparece en Mis citas como PENDIENTE y con el precio del servicio |
| Dueño: elegir un horario ocupado | El horario aparece gris y no se puede elegir |
| Veterinario: agenda | La cita aparece en su hora y puede confirmarla; el dueño la ve CONFIRMADA |
| Veterinario: atender una cita | Pasa a ATENDIDA, el dueño la ve en el historial y el peso se actualizó |
| Dueño: cancelar una cita a menos de 2 horas | No se permite cancelar |
| Dueño: cancelar una cita a más de 2 horas | Se cancela (se compara la duración exacta, no horas truncadas) |
| Veterinario: ingresos del mes | Coinciden con la suma del precio de las citas atendidas |
| Veterinario: desactivar un servicio | Ya no se puede elegir al pedir una cita |
| Veterinario: eliminar un servicio con citas | No se elimina; se muestra un mensaje |
| Cerrar y abrir la app | Las citas siguen ahí y el perfil y el modo oscuro se recuerdan |
| Dueño: reprogramar una cita | Los horarios ocupados y pasados aparecen en gris y no se pueden elegir |

## Para pensar

**¿Por qué el Repository es una interface?**  
En la Semana 9 se conectará a un servicio web. ViewModels y pantallas no cambian: solo cambia la implementación (`VetRepositoryRoom` por una remota).

**¿Por qué fecha y hora son texto?**  
`'aaaa-MM-dd'` se ordena igual que las fechas, se compara con `=` en SQL y se convierte con `LocalDate.parse()`.

**¿Por qué guardar el precio en la cita?**  
Es una foto del momento: si mañana cambia el precio de la vacuna, las citas antiguas conservan lo que costaron y los ingresos del panel no se alteran.

**¿Por qué `registrarAtencion` usa `withTransaction`?**  
Actualiza la cita (diagnóstico, tratamiento, peso, estado ATENDIDA) y el peso de la mascota. Si algo falla, no queda nada a medias.

**¿Dónde viven las reglas de negocio?**  
En los ViewModels: visibilidad por dueño, validaciones, horario único, cancelación con más de 2 horas, estados del veterinario. Ni la View ni el DAO deciden eso.

## Fase 2 (mejoras)

1. Gráfico de peso en la ficha (`GraficoLineas` + `observarPesoHistorial`).
2. Reprogramar cita (PENDIENTE o CONFIRMADA, horarios libres y regla de 2 horas).
3. Búsqueda de pacientes por nombre de mascota o dueño y filtro por especie (`combine`).

Los prompts están en `PROMPTS.md`.
