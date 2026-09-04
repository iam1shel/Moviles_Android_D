package com.rojastuesta.lab02playadeautos

class PlayaDeAutos {
    private val vehiculos = mutableListOf<Vehiculo>()

    fun agregarVehiculo(vehiculo: Vehiculo) {
        vehiculos.add(vehiculo)
        println("Vehiculo agregado: ${vehiculo.placa}")
    }

    fun mostrarBoletas() {
        val clientesYaImpresos = mutableListOf<String>()
        for (primero in vehiculos) {
            val clave = primero.nombreCliente.lowercase()
            if (clientesYaImpresos.contains(clave)) {
                continue
            }
            clientesYaImpresos.add(clave)

            val delCliente = mutableListOf<Vehiculo>()
            for (v in vehiculos) {
                if (v.nombreCliente.lowercase() == clave) {
                    delCliente.add(v)
                }
            }

            println()
            println("=".repeat(54))
            println("           BOLETA DE ESTACIONAMIENTO")
            println("=".repeat(54))
            println("Cliente : ${primero.nombreCliente}")
            println("-".repeat(54))

            var subtotal = 0.0
            var descuento = 0.0
            for (v in delCliente) {
                println("Placa : ${v.placa}")
                println("Tipo de vehiculo: ${v.tipo}")
                v.imprimirTablaHoras()
                println()
                subtotal += v.calcularSubtotal()
                descuento += v.calcularDescuento()
            }

            val neto = subtotal - descuento
            val descuentoMonto = if (neto > 500) neto * 0.20 else 0.0
            val total = neto - descuentoMonto

            println("-".repeat(54))
            println(String.format("%-30s S/ %8.2f", "Subtotal:", subtotal))
            println(String.format("%-30s S/ %8.2f", "Descuento cliente frecuente:", descuento))
            println(String.format("%-30s S/ %8.2f", "Descuento por monto (>500):", descuentoMonto))
            println(String.format("%-30s S/ %8.2f", "MONTO TOTAL A PAGAR:", total))
        }
    }
}
