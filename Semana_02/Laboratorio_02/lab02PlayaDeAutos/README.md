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


## Prompts usados

Se trabajó en tres commits. Cada prompt pedía no borrar lo anterior y solo agregar lo del commit.

### Commit 1 — registro

Haz un programa Kotlin solo de consola (no Android). Este es el COMMIT 1. No calcules tarifas ni imprimas boletas.

Necesito:
- Una data class con: placa, tipo de vehículo, horas y nombre del cliente.
- El tipo solo puede ser: moto, auto o camioneta.
- Al iniciar, preguntar cuántos vehículos se van a registrar.
- Usar un bucle while para ingresar los datos de cada vehículo por consola (readln).
- Validar que las horas no sean negativas ni menores a 1. Si están mal, volver a pedirlas.
- Guardar en memoria todos los registros y también las visitas de cada cliente (por nombre), porque después se usará para un descuento. En este commit solo guarda, no apliques descuento.

Al final, un println simple que confirme cuántos vehículos se registraron. Nada de TARIFA BASICA todavía.

### Commit 2 — cálculos

Este es el COMMIT 2 del mismo programa Kotlin de consola. NO borres la base del commit 1. SOLO agrega los cálculos. Todavía NO imprimas la boleta TARIFA BASICA.

Reglas de tarifa base por hora:
- Moto: 2.00 soles
- Auto: 4.00 soles
- Camioneta: 10.00 soles

Recargo por cada hora, sobre la tarifa original:
- Horas 1 y 2: recargo 0%. Importe = tarifa
- Horas 3 y 4: recargo 20%. Importe = tarifa * 1.20
- Hora 5 en adelante: recargo 50%. Importe = tarifa * 1.50

Cliente frecuente:
- Si el cliente ya vino más de 4 veces (en la 5ta visita), 10% de descuento sobre el TOTAL a pagar.
- En las visitas 1 a 4 no hay descuento.

Crea funciones que, para un registro, devuelvan:
- detalle por hora (hora, tarifa, recargo, importe)
- subtotal
- descuento
- total final

El dinero es en soles. No imprimas la tabla todavía.

### Commit 3 — impresión

Este es el COMMIT 3 del mismo programa Kotlin de consola. NO cambies las reglas de negocio. SOLO agrega la impresión de resultados.

Después de registrar cada vehículo, imprimir su tarifa básica con este formato (ejemplo, auto de 3 horas, placa ABC-123):

TARIFA BASICA: ABC-123
Hora	Tarifa	Recargo	Importe
1	4.00	0%	4.00
2	4.00	0%	4.00
3	4.00	20%	4.80
Total			12.80

Reglas de impresión:
- Título: TARIFA BASICA: <placa>
- Columnas: Hora, Tarifa, Recargo, Importe
- Recargo como 0%, 20% o 50%
- Montos con 2 decimales, en soles
- Si hay descuento de cliente frecuente, mostrarlo después y luego el total a pagar
- Hacer esto para cada vehículo del while

