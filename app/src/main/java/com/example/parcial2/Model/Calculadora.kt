package com.example.parcial2.Model

object Calculadora {

    fun calcularTotal(expenses: List<Expense>): Double {
        var total = 0.0
        for (expense in expenses) {
            total += expense.amount
        }
        return total
    }

    fun calcularTotalViaje(expenses: List<Expense>, tripId: Int): Double {
        return calcularTotal(expenses.filter { it.tripId == tripId })
    }
}