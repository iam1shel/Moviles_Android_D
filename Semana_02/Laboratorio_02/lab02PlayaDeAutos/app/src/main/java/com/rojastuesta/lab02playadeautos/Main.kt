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
    println(" PLAYA DE AUTOS - TECSUP ")
    println("=========================================")

    val aforo = leerEntero("Aforo de la playa (espacios): ")
    val playa = PlayaDeAutos(aforo)

    val nombreCliente = leerTexto("Nombre del cliente: ")
    val esClienteFrecuente = leerSiNo("Es cliente frecuente?")
    val horas = leerEntero("Cuantas horas se quedara? ")

    var seguir = true
    while (seguir && playa.hayEspacio()) {
        println()
        println("Ocupados: ${playa.ocupados()} / ${playa.aforo}  |  Libres: ${playa.espaciosLibres()}")
        println("--- Registrar vehiculo ---")
        try {
            val vehiculo = Vehiculo(
                placa = leerTexto("Placa: "),
                tipo = leerTexto("Tipo (moto, auto, camioneta, trailer): "),
                horas = horas,
                esClienteFrecuente = esClienteFrecuente,
                nombreCliente = nombreCliente
            )
            playa.agregarVehiculo(vehiculo)
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }

        if (!playa.hayEspacio()) {
            println("Sin espacio. Ya llegaste al tope.")
            seguir = false
        } else {
            seguir = leerSiNo("Registrar otro vehiculo?")
        }
    }

    playa.mostrarBoletas()
}
