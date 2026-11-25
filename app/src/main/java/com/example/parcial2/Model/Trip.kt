package com.example.parcial2.Model

data class Trip(
    val id: Int,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val expenseCount: Int,
    val totalExpenses: Double
)

data class NewTrip(
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String
)

data class TripDetail(
    val id: Int,
    val name: String,
    val destination: String,
    val startDate: String,
    val endDate: String,
    val totalExpenses: Double,
    val expenseCount: Int,
    val expenses: List<Expense>
)