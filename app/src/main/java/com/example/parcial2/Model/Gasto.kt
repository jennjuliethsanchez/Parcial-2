package com.example.parcial2.Model

data class Gasto(
    val id: Int,
    val idViaje: Int,
    val descripcion: String,
    val valor: Double,
    val fechaGasto: String,
    val idCategoria: Int,
    val nombreCategoria: String
)

data class NuevoGasto(
    val idViaje: Int,
    val descripcion: String,
    val valor: Double,
    val idCategoria: Int,
    val fechaGasto: String
)