package com.example.parcial2.Model

object Validador {

    fun fechaValida(fecha: String): Boolean {
        return Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(fecha)
    }

    fun validarViaje(nombre: String, destino: String, inicio: String, fin: String): String? {
        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (destino.isBlank()) return "El destino es obligatorio"
        if (inicio.isBlank()) return "La fecha de inicio es obligatoria"
        if (fin.isBlank()) return "La fecha de fin es obligatoria"
        if (!fechaValida(inicio)) return "La fecha de inicio debe ser AAAA-MM-DD"
        if (!fechaValida(fin)) return "La fecha de fin debe ser AAAA-MM-DD"
        if (fin < inicio) return "La fecha de fin no puede ser anterior a la de inicio"
        return null
    }

    fun validarGasto(
        descripcion: String,
        valorTexto: String,
        categoriaId: Int?,
        fecha: String,
        inicioViaje: String,
        finViaje: String
    ): String? {
        if (descripcion.isBlank()) return "La descripción es obligatoria"
        if (valorTexto.isBlank()) return "El valor es obligatorio"

        val valor = valorTexto.replace(",", ".").toDoubleOrNull()
        if (valor == null) return "El valor debe ser un número"
        if (valor <= 0) return "El valor debe ser mayor que cero"

        if (categoriaId == null) return "Debes elegir una categoría"
        if (fecha.isBlank()) return "La fecha del gasto es obligatoria"
        if (!fechaValida(fecha)) return "La fecha debe ser AAAA-MM-DD"
        if (fecha < inicioViaje || fecha > finViaje) {
            return "La fecha debe estar dentro del período del viaje"
        }
        return null
    }
}