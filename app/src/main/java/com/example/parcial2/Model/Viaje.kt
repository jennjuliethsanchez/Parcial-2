package com.example.parcial2.Model

data class Viaje(
    val id: Int,
    val nombre: String,
    val destino: String,
    val fechaInicio: String,
    val fechaFinal: String,
    val cantidadGasto: Int,
    val totalGasto: Double
)

data class NuevoViaje(
    val nombre: String,
    val destino: String,
    val fechaInicio: String,
    val fechaFinal: String
)