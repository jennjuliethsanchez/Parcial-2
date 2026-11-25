package com.example.parcial2

import com.example.parcial2.Model.Calculadora
import com.example.parcial2.Model.Expense
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pruebas del requerimiento 7: calcular el total de gastos de cada viaje.
 */
class CalculadoraTest {

    private val delta = 0.001

    private fun gasto(id: Int, tripId: Int, amount: Double) = Expense(
        id = id,
        tripId = tripId,
        description = "Gasto $id",
        amount = amount,
        expenseDate = "2026-06-12",
        categoryId = 1,
        categoryName = "Alimentación"
    )

    @Test
    fun calcularTotal_listaVacia_retornaCero() {
        assertEquals(0.0, Calculadora.calcularTotal(emptyList()), delta)
    }

    @Test
    fun calcularTotal_unSoloGasto_retornaSuValor() {
        val gastos = listOf(gasto(1, 1, 28.50))
        assertEquals(28.50, Calculadora.calcularTotal(gastos), delta)
    }

    @Test
    fun calcularTotal_variosGastos_retornaLaSuma() {
        // Mismos valores del mockup: 28,50 + 260,00 + 7,00 = 295,50
        val gastos = listOf(
            gasto(1, 1, 28.50),
            gasto(2, 1, 260.00),
            gasto(3, 1, 7.00)
        )
        assertEquals(295.50, Calculadora.calcularTotal(gastos), delta)
    }

    @Test
    fun calcularTotalViaje_soloSumaLosGastosDeEseViaje() {
        val gastos = listOf(
            gasto(1, 1, 28.50),
            gasto(2, 2, 500.00),
            gasto(3, 1, 7.00),
            gasto(4, 3, 99.99)
        )
        assertEquals(35.50, Calculadora.calcularTotalViaje(gastos, 1), delta)
        assertEquals(500.00, Calculadora.calcularTotalViaje(gastos, 2), delta)
    }

    @Test
    fun calcularTotalViaje_viajeSinGastos_retornaCero() {
        val gastos = listOf(gasto(1, 1, 28.50))
        assertEquals(0.0, Calculadora.calcularTotalViaje(gastos, 99), delta)
    }
}
