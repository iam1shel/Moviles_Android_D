# Laboratorio 02 — Playa de autos

Programa **Kotlin de consola** (Android Studio, sin emulador). Es el paralelo de `lab02CarritoKotlin`: se registra por `readln` y el resultado sale en la pestaña **Run**.

Ruta en el curso:
Semana_02/Laboratorio_02/lab02PlayaDeAutos/Main.kt

## Cómo ejecutarlo

1. Abre el proyecto de la semana en Android Studio.
2. En la vista **Project**, entra a `Laboratorio_02` → `lab02PlayaDeAutos`.
3. Abre `Main.kt`.
4. Clic derecho en el archivo → **Run 'MainKt'** (no uses el botón del celular).
5. Responde las preguntas abajo, en **Run**.

## Qué hace

1. Pregunta cuántos vehículos se van a registrar.
2. Con un `while` pide placa, tipo, horas y nombre del cliente.
3. El tipo solo puede ser `moto`, `auto` o `camioneta`.
4. Las horas deben ser un entero mayor o igual a 1.
5. Calcula la tarifa por hora (con recargo) y, si aplica, el descuento de cliente frecuente.
6. Imprime la boleta **TARIFA BASICA** de ese vehículo.
7. Al final indica cuántos vehículos se registraron.

## Tarifas (soles por hora)

| Tipo      | Tarifa base |
|-----------|-------------|
| Moto      | 2.00        |
| Auto      | 4.00        |
| Camioneta | 10.00       |

Recargo sobre esa tarifa, **por cada hora**:

| Horas         | Recargo | Importe       |
|---------------|---------|---------------|
| 1 y 2         | 0%      | tarifa        |
| 3 y 4         | 20%     | tarifa × 1.20 |
| 5 en adelante | 50%     | tarifa × 1.50 |

**Cliente frecuente:** en las visitas 1 a 4 no hay descuento. Desde la 5.ª visita, 10% sobre el total.

## Commits

| Commit | Qué incluye |
|--------|-------------|
| 1 | Data class, `while`, validación de horas, visitas por cliente (sin calcular ni imprimir) |
| 2 | Tarifas, recargos y descuento (sin boleta) |
| 3 | Impresión de TARIFA BASICA por cada vehículo |

