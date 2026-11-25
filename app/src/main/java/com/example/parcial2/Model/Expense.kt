package com.example.parcial2.Model


data class Expense(
    val id: Int,
    val tripId: Int,
    val description: String,
    val amount: Double,
    val expenseDate: String,
    val categoryId: Int,
    val categoryName: String
)

data class NewExpense(
    val tripId: Int,
    val description: String,
    val amount: Double,
    val categoryId: Int,
    val expenseDate: String
)