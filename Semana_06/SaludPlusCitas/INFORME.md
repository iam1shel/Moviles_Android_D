# Informe — Clínica SaludPlus (App Paciente)

Alumna: Rojas Tuesta Luz Mishel

## Observaciones

1. El zip `SaludPlusCitas.zip` no fue entregado; se recreó el esqueleto según la estructura del PDF (`com.saludplus.citas`).
2. La NavigationBar en Inicio y el flujo con parámetros (`especialidadId`, `medicoId`, `fecha`, `hora`) + `popUpTo` fueron los puntos más delicados al armar la navegación.

## Conclusiones

1. Trabajar desde un esqueleto con TODOs obliga a respetar firmas del Repositorio y hace más claro qué falta por pantalla.
2. La Fase 1 (días fijos) deja listo el bloqueo de horarios; la Fase 2 (LocalDate) solo cambia cómo se generan los días, sin romper las citas en memoria.
