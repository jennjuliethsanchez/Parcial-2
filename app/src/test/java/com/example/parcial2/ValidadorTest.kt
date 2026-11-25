package com.example.parcial2

import com.example.parcial2.Model.Validador
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pruebas de los requerimientos 2 (crear viaje), 5 (registrar gasto)
 * y 8 (validar información).
 */
class ValidadorTest {

    // ---------- Formato de fecha ----------

    @Test
    fun fechaValida_formatoCorrecto_retornaTrue() {
        assertTrue(Validador.fechaValida("2026-06-12"))
    }

    @Test
    fun fechaValida_formatoIncorrecto_retornaFalse() {
        assertFalse(Validador.fechaValida("12/06/2026"))
        assertFalse(Validador.fechaValida("2026-6-12"))
        assertFalse(Validador.fechaValida(""))
    }

    // ---------- Requerimiento 2: crear viaje ----------

    @Test
    fun validarViaje_datosCorrectos_retornaNull() {
        val error = Validador.validarViaje("Europa 2026", "Italia", "2026-06-10", "2026-06-20")
        assertNull(error)
    }

    @Test
    fun validarViaje_mismaFechaInicioYFin_esValido() {
        assertNull(Validador.validarViaje("Paseo", "Girardot", "2026-06-10", "2026-06-10"))
    }

    @Test
    fun validarViaje_nombreVacio_retornaError() {
        val error = Validador.validarViaje("  ", "Italia", "2026-06-10", "2026-06-20")
        assertEquals("El nombre es obligatorio", error)
    }

    @Test
    fun validarViaje_destinoVacio_retornaError() {
        val error = Validador.validarViaje("Europa 2026", "", "2026-06-10", "2026-06-20")
        assertEquals("El destino es obligatorio", error)
    }

    @Test
    fun validarViaje_fechaInicioVacia_retornaError() {
        val error = Validador.validarViaje("Europa 2026", "Italia", "", "2026-06-20")
        assertEquals("La fecha de inicio es obligatoria", error)
    }

    @Test
    fun validarViaje_fechaFinVacia_retornaError() {
        val error = Validador.validarViaje("Europa 2026", "Italia", "2026-06-10", "")
        assertEquals("La fecha de fin es obligatoria", error)
    }

    @Test
    fun validarViaje_fechaInicioMalFormato_retornaError() {
        val error = Validador.validarViaje("Europa 2026", "Italia", "10/06/2026", "2026-06-20")
        assertEquals("La fecha de inicio debe ser AAAA-MM-DD", error)
    }

    @Test
    fun validarViaje_fechaFinMalFormato_retornaError() {
        val error = Validador.validarViaje("Europa 2026", "Italia", "2026-06-10", "20/06/2026")
        assertEquals("La fecha de fin debe ser AAAA-MM-DD", error)
    }

    @Test
    fun validarViaje_fechaFinAntesDeInicio_retornaError() {
        val error = Validador.validarViaje("Europa 2026", "Italia", "2026-06-20", "2026-06-10")
        assertEquals("La fecha de fin no puede ser anterior a la de inicio", error)
    }

    // ---------- Requerimientos 5 y 8: registrar gasto ----------

    private val inicio = "2026-06-10"
    private val fin = "2026-06-20"

    @Test
    fun validarGasto_datosCorrectos_retornaNull() {
        val error = Validador.validarGasto("Cena en Trastevere", "28.50", 1, "2026-06-12", inicio, fin)
        assertNull(error)
    }

    @Test
    fun validarGasto_valorConComa_esValido() {
        val error = Validador.validarGasto("Cena", "28,50", 1, "2026-06-12", inicio, fin)
        assertNull(error)
    }

    @Test
    fun validarGasto_fechaEnLimitesDelViaje_esValida() {
        assertNull(Validador.validarGasto("Hotel", "100", 2, inicio, inicio, fin))
        assertNull(Validador.validarGasto("Taxi", "10", 3, fin, inicio, fin))
    }

    @Test
    fun validarGasto_descripcionVacia_retornaError() {
        val error = Validador.validarGasto(" ", "28.50", 1, "2026-06-12", inicio, fin)
        assertEquals("La descripción es obligatoria", error)
    }

    @Test
    fun validarGasto_valorVacio_retornaError() {
        val error = Validador.validarGasto("Cena", "", 1, "2026-06-12", inicio, fin)
        assertEquals("El valor es obligatorio", error)
    }

    @Test
    fun validarGasto_valorNoNumerico_retornaError() {
        val error = Validador.validarGasto("Cena", "abc", 1, "2026-06-12", inicio, fin)
        assertEquals("El valor debe ser un número", error)
    }

    @Test
    fun validarGasto_valorCero_retornaError() {
        val error = Validador.validarGasto("Cena", "0", 1, "2026-06-12", inicio, fin)
        assertEquals("El valor debe ser mayor que cero", error)
    }

    @Test
    fun validarGasto_valorNegativo_retornaError() {
        val error = Validador.validarGasto("Cena", "-15", 1, "2026-06-12", inicio, fin)
        assertEquals("El valor debe ser mayor que cero", error)
    }

    @Test
    fun validarGasto_sinCategoria_retornaError() {
        val error = Validador.validarGasto("Cena", "28.50", null, "2026-06-12", inicio, fin)
        assertEquals("Debes elegir una categoría", error)
    }

    @Test
    fun validarGasto_fechaVacia_retornaError() {
        val error = Validador.validarGasto("Cena", "28.50", 1, "", inicio, fin)
        assertEquals("La fecha del gasto es obligatoria", error)
    }

    @Test
    fun validarGasto_fechaMalFormato_retornaError() {
        val error = Validador.validarGasto("Cena", "28.50", 1, "12-06-2026", inicio, fin)
        assertEquals("La fecha debe ser AAAA-MM-DD", error)
    }

    @Test
    fun validarGasto_fechaAntesDelViaje_retornaError() {
        val error = Validador.validarGasto("Cena", "28.50", 1, "2026-06-09", inicio, fin)
        assertEquals("La fecha debe estar dentro del período del viaje", error)
    }

    @Test
    fun validarGasto_fechaDespuesDelViaje_retornaError() {
        val error = Validador.validarGasto("Cena", "28.50", 1, "2026-06-21", inicio, fin)
        assertEquals("La fecha debe estar dentro del período del viaje", error)
    }
}
