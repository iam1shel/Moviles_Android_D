# Laboratorio 13 — Uso de IA (Gemini)

## Fase 2: Mejoras de CampusGPS

Se utilizó Gemini como asistente para implementar mejoras en la aplicación CampusGPS, manteniendo Kotlin, Jetpack Compose, Material 3 y la arquitectura MVVM + Repository.

| Prompt utilizado | Respuesta de Gemini | Cambios realizados o descartados | Justificación técnica |
|---|---|---|---|
| Implementar filtros por categoría con FilterChip. | Agregó filtros para Estudio, Comida, Eventos y Servicios. | Se incorporaron filtros para el mapa y la lista. | Permite consultar lugares por categoría sin modificar los datos del Repository. |
| Implementar un selector de radio de 100, 200 y 500 metros. | Agregó controles de radio y un círculo en Google Maps. | Se incorporó la selección del radio de cercanía. | Facilita identificar los lugares cercanos al usuario. |
| Guardar el último lugar seleccionado mediante DataStore. | Implementó persistencia de la selección. | Se agregó un Repository para gestionar DataStore. | Permite conservar la selección al reiniciar la aplicación sin colocar Context en el ViewModel. |
| Modernizar CampusGPS utilizando Material 3. | Generó una interfaz con barra azul, tarjetas, filtros y controles visuales. | Se corrigieron referencias al ícono Navigation por problemas de compilación. | Mejora la presentación sin modificar la lógica de negocio. |

## Validación

- Se compiló el proyecto con Gradle.
- Se probó la interfaz en el emulador Pixel 6.
- Se tomaron capturas de las tres mejoras funcionales.
- Se corrigió un error de compilación relacionado con el ícono Navigation.

## Conclusión

Gemini ayudó a implementar filtros, radio de cercanía, persistencia con DataStore y mejoras visuales. Se revisó el resultado y se corrigió un error de compilación para mantener la aplicación funcional.
