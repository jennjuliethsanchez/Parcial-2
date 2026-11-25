package com.example.parcial2.Model

data class DetalleViaje(
    val id: Int,
    val nombre: String,
    val destino: String,
    val fechaInicio: String,
    val fechaFinal: String,
    val totalGastos: Double,
    val valorGastos: Int,
    val gastos: List<Gasto>
)