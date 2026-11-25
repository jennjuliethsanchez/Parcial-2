package com.example.parcial2.Model

object Calculadora {

    fun calcularTotal(gastos: List<Gasto>): Double {
        var total = 0.0
        for (gasto in gastos) {
            total = total + gasto.valor
        }
        return total
    }
}