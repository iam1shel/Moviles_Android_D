package com.tecsup.huellitasvet

import com.tecsup.huellitasvet.model.Horarios
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun horarios_tieneDieciochoSlots() {
        assertEquals(18, Horarios.todos.size)
        assertEquals("09:00", Horarios.todos.first())
        assertEquals("17:30", Horarios.todos.last())
    }
}
