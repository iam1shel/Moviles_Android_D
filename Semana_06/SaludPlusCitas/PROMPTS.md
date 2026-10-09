# PROMPTS.md — SaludPlus (con-ia)

Alumna: Rojas Tuesta Luz Mishel

## Prompt 1 — Calendario dinámico

**Prompt usado:**
> En FechaHoraScreen de SaludPlus (Jetpack Compose), reemplaza la lista fija de días por un calendario con java.time.LocalDate: próximos 5 días hábiles, flechas < > por semana sin retroceder antes de la semana actual, título de mes/año dinámico, al cambiar de día recalcular horariosDisponibles y reiniciar la hora. En ConfirmarCita muestra la fecha en español.

**Respuesta resumida:**
La IA sugirió helpers con LocalDate, DayOfWeek y DateTimeFormatter, estado de semana en remember, y FilterChip por día.

**Qué corregí:**
- Evitar retroceder antes del lunes de la semana actual.
- Reiniciar la hora al cambiar día o semana.
- Mantener `Repositorio.horariosDisponibles` para no romper el bloqueo de citas ya reservadas.
- Habilitar core library desugaring porque minSdk = 24.

## Prompt 2 — Documentación

**Prompt usado:**
> Redacta PROMPTS.md del calendario dinámico de SaludPlus con prompt, resumen y correcciones.

**Respuesta resumida:**
Plantilla de tres secciones por prompt.

**Qué corregí:**
Ajusté el texto a CalendarioUtils.kt y al formato "Martes 16 de setiembre 2026".
