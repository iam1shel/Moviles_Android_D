enum class TipoVehiculo(val etiqueta: String) {
    MOTO("moto"),
    AUTO("auto"),
    CAMIONETA("camioneta");

    companion object {
        fun desdeEntrada(texto: String): TipoVehiculo? =
            entries.find { it.etiqueta.equals(texto.trim(), ignoreCase = true) }
    }
}

data class Registro(
    val placa: String,
    val tipo: TipoVehiculo,
    val horas: Int,
    val nombreCliente: String,
)

class RepositorioEnMemoria {
    val registros = mutableListOf<Registro>()
    val visitasPorCliente = mutableMapOf<String, Int>()

    fun guardar(registro: Registro) {
        registros += registro
        val claveCliente = registro.nombreCliente.lowercase()
        visitasPorCliente[claveCliente] = (visitasPorCliente[claveCliente] ?: 0) + 1
    }
}

fun main() {
    val repositorio = RepositorioEnMemoria()

    println("=== Playa de autos — registro de vehículos ===")
    print("¿Cuántos vehículos se van a registrar? ")
    val cantidad = leerEnteroMinimo(1)

    var indice = 0
    while (indice < cantidad) {
        println()
        println("--- Vehículo ${indice + 1} de $cantidad ---")

        print("Placa: ")
        val placa = leerTextoNoVacio()

        print("Tipo de vehículo (moto, auto, camioneta): ")
        val tipo = leerTipoVehiculo()

        print("Horas: ")
        val horas = leerHoras()

        print("Nombre del cliente: ")
        val nombreCliente = leerTextoNoVacio()

        repositorio.guardar(
            Registro(
                placa = placa,
                tipo = tipo,
                horas = horas,
                nombreCliente = nombreCliente,
            )
        )

        indice++
    }

    println()
    println("Se registraron ${repositorio.registros.size} vehículo(s).")
}

private fun leerTextoNoVacio(): String {
    while (true) {
        val valor = readln().trim()
        if (valor.isNotEmpty()) return valor
        print("El valor no puede estar vacío. Intente de nuevo: ")
    }
}

private fun leerEnteroMinimo(minimo: Int): Int {
    while (true) {
        val valor = readln().trim().toIntOrNull()
        if (valor != null && valor >= minimo) return valor
        print("Ingrese un número entero mayor o igual a $minimo: ")
    }
}

private fun leerHoras(): Int {
    while (true) {
        val valor = readln().trim().toIntOrNull()
        if (valor != null && valor >= 1) return valor
        print("Las horas no pueden ser negativas ni menores a 1. Ingrese las horas otra vez: ")
    }
}

private fun leerTipoVehiculo(): TipoVehiculo {
    while (true) {
        val tipo = TipoVehiculo.desdeEntrada(readln())
        if (tipo != null) return tipo
        print("Tipo inválido. Use moto, auto o camioneta: ")
    }
}