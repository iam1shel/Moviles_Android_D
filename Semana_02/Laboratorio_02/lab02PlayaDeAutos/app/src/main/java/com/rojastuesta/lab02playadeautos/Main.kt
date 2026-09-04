package com.rojastuesta.lab02playadeautos

fun leerTexto(mensaje: String): String {
    while (true) {
        print(mensaje)
        val valor = readln().trim()
        if (valor.isNotEmpty()) return valor
        println("Este campo no puede estar vacio.")
    }
}

fun leerEntero(mensaje: String): Int {
    while (true) {
        print(mensaje)
        val valor = readln().trim().toIntOrNull()
        if (valor != null && valor >= 1) return valor
        println("Ingrese un numero entero mayor o igual a 1.")
    }
}

fun leerSiNo(mensaje: String): Boolean {
    while (true) {
        print("$mensaje (s/n): ")
        val valor = readln().trim().lowercase()
        if (valor == "s" || valor == "si" || valor == "sí") return true
        if (valor == "n" || valor == "no") return false
        println("Escriba s o n.")
    }
}

fun main() {
    println("=========================================")
    println("   PLAYA DE AUTOS - TECSUP   ")
    println("=========================================")

    val playa = PlayaDeAutos()
    val nombreCliente = leerTexto("Nombre del cliente: ")
    val esClienteFrecuente = leerSiNo("Es cliente frecuente?")
    val horas = leerEntero("Cuantas horas se quedara? ")
    val cantidad = leerEntero("Cuantos vehiculos se van a registrar? ")

    var indice = 0
    while (indice < cantidad) {
        println()
        println("--- Vehiculo ${indice + 1} de $cantidad ---")
        try {
            val vehiculo = Vehiculo(
                placa = leerTexto("Placa: "),
                tipo = leerTexto("Tipo (moto, auto, camioneta): "),
                horas = horas,
                esClienteFrecuente = esClienteFrecuente,
                nombreCliente = nombreCliente
            )
            playa.agregarVehiculo(vehiculo)
            indice++
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }
    }

    playa.mostrarBoletas()
}
