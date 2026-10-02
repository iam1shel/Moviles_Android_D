# Prompts de la mejora — Laboratorio 06

Alumna: Rojas Tuesta Luz Mishel

La mejora es el badge del ítem Favoritos en el drawer. El primer prompt dejó una versión que contaba toques. El segundo sube el conjunto de ids a `AppNavegacion`.

## Prompt 1 — badge que suma en cada toque

```text
En el Laboratorio 06, TECSUP Store, el drawer ya tiene los destinos Inicio, Mis pedidos, Favoritos, Perfil y Cerrar sesión. Cada tarjeta tiene un DropdownMenu con Favoritos, Compartir y Reportar.

Agrega un Badge en el ítem Favoritos de AppDrawer. Quiero que el número aumente cada vez que se toque Favoritos en el menú de una tarjeta. Si el total todavía es 0, no muestres el badge.

No cambies el encabezado MR, Maria Rojas, maria@tecsup.edu.pe, ni la navegación entre pantallas. El archivo del drawer sigue siendo AppDrawer.kt y el menú sigue en TarjetaProducto.kt, en el mismo Box del ícono de tres puntos.
```

Ese pedido produjo `toquesFavoritos++` en `AppNavegacion`. La pantalla Favoritos mostraba "Veces que elegiste Favoritos". Marcar y quitar el mismo producto también subía el número, y la pantalla no listaba los productos.

## Prompt 2 — corregir subiendo el estado

```text
El badge quedó mal: está contando toques, no productos marcados. Si toco Favoritos dos veces en el polo, el badge marca 2 aunque el producto haya vuelto a quedar sin marcar.

Corrige eso subiendo el estado. El conjunto de ids debe vivir en AppNavegacion, porque es el padre común del drawer y de las tarjetas. TarjetaProducto no debe guardar esFavorito con su propio remember: recibe si ese id está en el conjunto y una función para alternarlo.

Reglas:
- Favoritos agrega el id si no está y lo quita si ya está. El mismo producto no se cuenta dos veces.
- El badge del ítem Favoritos muestra el tamaño del conjunto.
- Si el total es 0, el badge no se muestra.
- La pantalla Favoritos usa esa misma lista y muestra esas tarjetas.
- Compartir y Reportar no cambian el conjunto.
- No muevas el estado expanded: cada DropdownMenu se abre solo en su tarjeta.
```

Ese pedido reemplazó el contador por `idsFavoritos` en `AppNavegacion`. El drawer recibe `idsFavoritos.size` y la pantalla Favoritos filtra `catalogoTecsup` con el mismo conjunto.
