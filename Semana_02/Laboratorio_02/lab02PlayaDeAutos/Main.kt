package com.yldefonso.playadeautos_rojastuesta

import java.util.Locale
import kotlin.math.round

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

data class DetalleHora(
    val hora: Int,
    val tarifa: Double,
    val recargo: Double,
    val importe: Double,
)

data class CalculoCobro(
    val detalles: List<DetalleHora>,
    val subtotal: Double,
    val descuento: Double,
    val totalFinal: Double,
)

class RepositorioEnMemoria {
    val registros = mutableListOf<Registro>()
    val visitasPorCliente = mutableMapOf<String, Int>()
    val calculos = mutableListOf<CalculoCobro>()

    fun guardar(registro: Registro) {
        registros += registro
        val claveCliente = registro.nombreCliente.lowercase()
        visitasPorCliente[claveCliente] = (visitasPorCliente[claveCliente] ?: 0) + 1
        val numeroVisita = visitasPorCliente.getValue(claveCliente)
        calculos += calcularCobro(registro, numeroVisita)
    }
}

fun tarifaBase(tipo: TipoVehiculo): Double = when (tipo) {
    TipoVehiculo.MOTO -> 2.00
    TipoVehiculo.AUTO -> 4.00
    TipoVehiculo.CAMIONETA -> 10.00
}

fun recargoDeHora(numeroHora: Int): Double = when {
    numeroHora <= 2 -> 0.0
    numeroHora <= 4 -> 20.0
    else -> 50.0
}

fun detallePorHora(registro: Registro): List<DetalleHora> {
    val tarifa = tarifaBase(registro.tipo)
    return (1..registro.horas).map { hora ->
        val recargo = recargoDeHora(hora)
        DetalleHora(
            hora = hora,
            tarifa = tarifa,
            recargo = recargo,
            importe = soles(tarifa * (1 + recargo / 100.0)),
        )
    }
}

fun subtotal(registro: Registro): Double =
    soles(detallePorHora(registro).sumOf { it.importe })

fun descuento(registro: Registro, numeroVisita: Int): Double {
    if (numeroVisita < 5) return 0.0
    return soles(subtotal(registro) * 0.10)
}

fun totalFinal(registro: Registro, numeroVisita: Int): Double =
    soles(subtotal(registro) - descuento(registro, numeroVisita))

fun calcularCobro(registro: Registro, numeroVisita: Int): CalculoCobro {
    return CalculoCobro(
        detalles = detallePorHora(registro),
        subtotal = subtotal(registro),
        descuento = descuento(registro, numeroVisita),
        totalFinal = totalFinal(registro, numeroVisita),
    )
}

fun imprimirTarifaBasica(registro: Registro, calculo: CalculoCobro) {
    println()
    println("TARIFA BASICA: ${registro.placa}")
    println("Hora\tTarifa\tRecargo\tImporte")
    for (detalle in calculo.detalles) {
        println(
            "${detalle.hora}\t${solesTexto(detalle.tarifa)}\t${detalle.recargo.toInt()}%\t${solesTexto(detalle.importe)}"
        )
    }
    println("Total\t\t\t${solesTexto(calculo.subtotal)}")
    if (calculo.descuento > 0.0) {
        println("Descuento cliente frecuente (10%)\t${solesTexto(calculo.descuento)}")
        println("Total a pagar\t\t\t${solesTexto(calculo.totalFinal)}")
    }
}

private fun soles(valor: Double): Double = round(valor * 100.0) / 100.0

private fun solesTexto(valor: Double): String = "%.2f".format(Locale.US, valor)

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

        val registro = Registro(
            placa = placa,
            tipo = tipo,
            horas = horas,
            nombreCliente = nombreCliente,
        )
        repositorio.guardar(registro)
        imprimirTarifaBasica(registro, repositorio.calculos.last())

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