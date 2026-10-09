package com.saludplus.citas.ui.screens.agendamiento

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object CalendarioUtils {
    private val localeEs = Locale.forLanguageTag("es-PE")
    private val iso = DateTimeFormatter.ISO_LOCAL_DATE

    fun parseIso(fecha: String): LocalDate = LocalDate.parse(fecha, iso)

    fun toIso(fecha: LocalDate): String = fecha.format(iso)

    /** Inicio de semana laboral (lunes) acotado a no ir antes de la semana actual. */
    fun lunesDeSemana(referencia: LocalDate): LocalDate {
        val hoy = LocalDate.now()
        val lunesActual = hoy.with(DayOfWeek.MONDAY)
        val lunes = referencia.with(DayOfWeek.MONDAY)
        return if (lunes.isBefore(lunesActual)) lunesActual else lunes
    }

    fun puedeRetroceder(semanaInicio: LocalDate): Boolean {
        val lunesActual = LocalDate.now().with(DayOfWeek.MONDAY)
        return semanaInicio.isAfter(lunesActual)
    }

    /** Próximos 5 días hábiles a partir de `desde` (sin sábados/domingos ni días pasados). */
    fun diasHabilesDesde(desde: LocalDate, cantidad: Int = 5): List<LocalDate> {
        val hoy = LocalDate.now()
        val resultado = mutableListOf<LocalDate>()
        var cursor = if (desde.isBefore(hoy)) hoy else desde
        while (resultado.size < cantidad) {
            if (cursor.dayOfWeek != DayOfWeek.SATURDAY && cursor.dayOfWeek != DayOfWeek.SUNDAY) {
                if (!cursor.isBefore(hoy)) resultado.add(cursor)
            }
            cursor = cursor.plusDays(1)
        }
        return resultado
    }

    /** Cinco hábiles visibles en la semana mostrada (lunes..), filtrando pasados. */
    fun diasHabilesDeSemana(semanaInicio: LocalDate): List<LocalDate> {
        val lunes = lunesDeSemana(semanaInicio)
        val candidatos = (0..6).map { lunes.plusDays(it.toLong()) }
            .filter { it.dayOfWeek != DayOfWeek.SATURDAY && it.dayOfWeek != DayOfWeek.SUNDAY }
            .filter { !it.isBefore(LocalDate.now()) }
        return if (candidatos.size >= 5) candidatos.take(5) else diasHabilesDesde(lunes, 5)
    }

    fun tituloMesAnio(semanaInicio: LocalDate): String {
        val lunes = lunesDeSemana(semanaInicio)
        val mes = lunes.month.getDisplayName(TextStyle.FULL, localeEs)
        val mesCap = mes.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeEs) else it.toString() }
        return "$mesCap ${lunes.year}"
    }

    fun etiquetaCorta(fecha: LocalDate): String {
        val dia = fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, localeEs)
        return "${dia.replaceFirstChar { it.titlecase(localeEs) }} ${fecha.dayOfMonth}"
    }

    /** Ej: "Martes 16 de setiembre 2026" */
    fun fechaEnEspanol(fechaIso: String): String {
        val fecha = parseIso(fechaIso)
        val diaSemana = fecha.dayOfWeek.getDisplayName(TextStyle.FULL, localeEs)
            .replaceFirstChar { it.titlecase(localeEs) }
        val mes = fecha.month.getDisplayName(TextStyle.FULL, localeEs)
        return "$diaSemana ${fecha.dayOfMonth} de $mes ${fecha.year}"
    }
}
