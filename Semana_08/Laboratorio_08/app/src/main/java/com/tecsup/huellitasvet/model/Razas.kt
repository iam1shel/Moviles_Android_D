package com.tecsup.huellitasvet.model

object Razas {
    private val porEspecie = mapOf(
        Especie.PERRO.name to listOf(
            "Labrador",
            "Bulldog",
            "Pastor alemán",
            "Golden Retriever",
            "Beagle",
            "Poodle",
            "Chihuahua",
            "Husky",
            "Mestizo"
        ),
        Especie.GATO.name to listOf(
            "Siamesa",
            "Persa",
            "Maine Coon",
            "Bengalí",
            "Angora",
            "Mestizo"
        ),
        Especie.OTRO.name to listOf(
            "Conejo",
            "Hámster",
            "Ave",
            "Tortuga",
            "Mestizo"
        )
    )

    fun de(especie: String): List<String> = porEspecie[especie].orEmpty()
}
