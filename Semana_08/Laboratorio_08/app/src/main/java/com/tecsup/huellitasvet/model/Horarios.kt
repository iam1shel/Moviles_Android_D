package com.tecsup.huellitasvet.model

import java.time.LocalTime

object Horarios {
    val todos: List<String> = buildList {
        var hora = LocalTime.of(9, 0)
        val fin = LocalTime.of(17, 30)
        while (!hora.isAfter(fin)) {
            add("%02d:%02d".format(hora.hour, hora.minute))
            hora = hora.plusMinutes(30)
        }
    }
}
