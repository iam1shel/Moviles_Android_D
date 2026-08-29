package com.rojastuesta.lab02playadeautos

data class DetalleHora(
    val hora: Int,
    val tarifaBase: Double,
    val recargoPorcentaje: Int,
    val importe: Double
)

class Vehiculo(
    placa: String,
    tipo: String,
    horas: Int,
    val esClienteFrecuente: Boolean,
    nombreCliente: String
) {
    val placa: String
    val tipo: String
    val horas: Int
    val nombreCliente: String

    init {
        require(placa.isNotBlank()) { "La placa no puede estar vacia." }
        require(horas >= 1) { "Las horas deben ser al menos 1." }
        require(nombreCliente.isNotBlank()) { "El nombre del cliente no puede estar vacio." }
        val tipoOk = tipo.trim().lowercase()
        require(tipoOk == "moto" || tipoOk == "auto" || tipoOk == "camioneta") {
            "Tipo de vehiculo no valido. Use moto, auto o camioneta."
        }
        this.placa = placa.trim().uppercase()
        this.tipo = tipoOk
        this.horas = horas
        this.nombreCliente = nombreCliente.trim()
    }

    fun tarifaBase(): Double {
        return when (tipo) {
            "moto" -> 2.00
            "auto" -> 4.00
            "camioneta" -> 10.00
            else -> 0.0
        }
    }

    fun recargoPorHora(hora: Int): Int {
        return when {
            hora <= 2 -> 0
            hora <= 5 -> 20
            else -> 50
        }
    }

    fun detalleHoras(): List<DetalleHora> {
        val detalle = mutableListOf<DetalleHora>()
        val tarifa = tarifaBase()
        var hora = 1
        while (hora <= horas) {
            val recargo = recargoPorHora(hora)
            val importe = tarifa * (1 + recargo / 100.0)
            detalle.add(DetalleHora(hora, tarifa, recargo, importe))
            hora++
        }
        return detalle
    }

    fun calcularSubtotal(): Double {
        var subtotal = 0.0
        for (d in detalleHoras()) {
            subtotal += d.importe
        }
        return subtotal
    }

    fun calcularDescuento(): Double {
        return if (esClienteFrecuente) {
            calcularSubtotal() * 0.10
        } else {
            0.0
        }
    }

    fun calcularTotal(): Double {
        return calcularSubtotal() - calcularDescuento()
    }

    fun imprimirTablaHoras() {
        println()
        println(String.format("%-6s %-14s %-11s %-12s",
            "Hora", "Tarifa Base", "%Recargo", "Importe"))
        println("-".repeat(50))
        for (d in detalleHoras()) {
            println(String.format("%-6d %-14s %-11s %-12s",
                d.hora,
                String.format("S/ %.2f", d.tarifaBase),
                "${d.recargoPorcentaje}%",
                String.format("S/ %.2f", d.importe)))
        }
        println(String.format("%-30s S/ %8.2f", "Total vehiculo:", calcularSubtotal()))
    }
}
