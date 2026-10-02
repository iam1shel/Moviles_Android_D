# Informe — Laboratorio 06: TECSUP Store

**Alumna:** Rojas Tuesta Luz Mishel  
**Curso:** Programación de dispositivos móviles  
**Docente:** Juan Leon Suiyon

## Caso propuesto

TECSUP Store es una tienda en Kotlin con Jetpack Compose. En el inicio están Audifonos, Smartwatch y Funda celular. Cada tarjeta tiene un ícono de tres puntos y, en el mismo `Box`, un `DropdownMenu` con Favoritos, Compartir y Reportar. Cada opción lleva `leadingIcon` y las separa un `HorizontalDivider`.

El ícono de menú de la barra abre un `ModalNavigationDrawer`. `AppDrawer` usa `ModalDrawerSheet`. El encabezado muestra las iniciales MR, el nombre Maria Rojas y el correo maria@tecsup.edu.pe. Los destinos son Inicio, Mis pedidos, Favoritos, Perfil y Cerrar sesión. El destino activo se resalta con otro color de fondo y esas opciones cambian de pantalla.

En la fase sin IA, Favoritos marca solo la tarjeta donde se tocó. El drawer no lleva contador, porque cada tarjeta guarda su propio `esFavorito`.

La mejora con IA es un badge en el ítem Favoritos del drawer con la cantidad de productos marcados desde el menú. El conjunto de ids vive en `AppNavegacion`, el padre común del drawer y de las tarjetas. La pantalla Favoritos usa esa misma lista. Si el total es 0, el badge no se muestra.

## Preguntas de reflexión

### 1. ¿Qué quedó resuelto sin IA y qué se dejó pendiente a propósito?

Sin IA armé el proyecto para abrirlo en Android Studio y fui agregando el caso por partes: el ícono de tres puntos con el estado `expanded`, las tres opciones, los íconos y los divisores, la estructura del drawer, la navegación real y al final el encabezado con el ítem activo.

Favoritos, en esa fase, cambia el color y el texto de una sola tarjeta. Compartir y Reportar dejan un aviso en esa misma tarjeta. El drawer navega a Inicio, Mis pedidos, Favoritos, Perfil y Cerrar sesión, pero no sabe cuántos productos están marcados. Ese contador quedó pendiente porque el estado todavía no estaba en un padre común.

### 2. ¿Qué le pedí a la IA y qué entregó la primera versión?

El primer prompt pidió un `Badge` en Favoritos que aumentara cada vez que se tocara esa opción del menú, y que no se viera si el total era 0. La IA lo resolvió con un entero `toquesFavoritos` en `AppNavegacion` y con `toquesFavoritos++` dentro del clic.

La tarjeta seguía teniendo su propio `remember` para saber si estaba marcada. El drawer solo veía el entero. Por eso la primera versión con IA contaba toques, no productos.

### 3. ¿Por qué contar toques no representa los favoritos y cómo se corrigió?

Un toque no es un producto. Si marco el polo y lo vuelvo a tocar, el entero sigue subiendo aunque el polo ya no esté en favoritos. Dos toques en la misma tarjeta cuentan como dos, y la pantalla Favoritos solo decía cuántas veces se había elegido la opción.

La corrección sube un conjunto de ids a `AppNavegacion`. `TarjetaProducto` recibe `esFavorito` y `onFavorito`; ya no decide sola si el producto está marcado. Alternar un id lo agrega o lo quita. El badge usa `idsFavoritos.size` y se oculta cuando el tamaño es 0. La pantalla Favoritos filtra el catálogo con ese mismo conjunto, así que muestra las tarjetas marcadas y no un contador de clics.

`expanded` se quedó en la tarjeta. Ese estado solo abre el menú de ese producto y no le sirve al drawer.

### 4. ¿Qué diferencia hay entre hacer el laboratorio sin IA y hacer la mejora con IA?

Sin IA el avance se ve en seis commits chicos: primero el ícono, después las opciones, después los íconos y divisores, después el drawer, después las pantallas y al final el encabezado. Cada paso compila y se entiende qué se agregó.

Con IA el mismo caso base entra en un commit y la mejora entra en dos: uno que cuenta toques y otro que lo corrige subiendo el estado. La IA escribió rápido el `Badge`, pero el primer resultado no cumplía el caso. Tuve que revisar dónde vivía el estado y pedir el conjunto de ids en el padre común. La diferencia no es solo la velocidad: sin IA el estado quedó en la tarjeta, y con IA hubo que corregir para que el drawer y la pantalla leyeran el mismo dato.

## Observaciones

1. En sin-ia, `esFavorito` vive dentro de `TarjetaProducto`. Marcar el polo cambia solo esa tarjeta y las otras siguen igual. El drawer no puede mostrar un total porque ese dato no sale de la tarjeta. Al ir a otra pantalla y volver al inicio, la marca se pierde, porque el `remember` se fue con la tarjeta.

2. En con-ia, la primera versión y la corregida no se comportan igual. El badge de toques crece aunque se quite el favorito. Después de subir `idsFavoritos` a `AppNavegacion`, marcar el polo y la taza deja el badge en 2, quitar el polo lo baja a 1, y la pantalla Favoritos muestra la taza. Si no hay ids, el badge no aparece.

3. El menú desplegable y el drawer no comparten el mismo tipo de estado. `expanded` es de una sola tarjeta y está bien ahí. Los favoritos los necesitan el drawer, el inicio y la pantalla Favoritos, así que tienen que vivir en `AppNavegacion`.

## Conclusiones

1. Sin IA el laboratorio queda completo en lo que pide la fase: menú con íconos y divisores, drawer con encabezado de Maria Rojas, destino activo con otro color de fondo y navegación real. El badge no entra en esa fase porque cada tarjeta guarda su favorito por separado y el drawer no tiene un total que mostrar.

2. Con IA la mejora sirve cuando el conjunto de ids está en el padre común. El badge, la marca de la tarjeta y la pantalla Favoritos leen la misma lista, el mismo producto no se cuenta dos veces y el badge se oculta si el total es 0. La primera respuesta, que solo sumaba toques, había que corregirla: la IA acelera el cambio, pero el lugar del estado en Compose lo tengo que comprobar yo.
