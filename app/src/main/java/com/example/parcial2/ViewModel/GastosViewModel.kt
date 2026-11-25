package com.example.parcial2.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial2.API.ApiError
import com.example.parcial2.API.Clienta
import com.example.parcial2.Model.Category
import com.example.parcial2.Model.NewExpense
import com.example.parcial2.Model.NewTrip
import com.example.parcial2.Model.Trip
import com.example.parcial2.Model.TripDetail
import com.example.parcial2.Model.Validador
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

data class GastosUiState(
    val trips: List<Trip> = emptyList(),
    val detail: TripDetail? = null,
    val categories: List<Category> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class GastosViewModel : ViewModel() {

    private val api = Clienta.api
    private val _state = MutableStateFlow(GastosUiState())
    val state: StateFlow<GastosUiState> = _state.asStateFlow()

    private val sinConexion = "No se pudo conectar con el servidor"

    fun cargarViajes() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            try {
                val trips = api.getTrips()
                _state.update { it.copy(trips = trips, loading = false) }
            }  catch (e: Exception) {
            android.util.Log.e("API", "Error al cargar viajes", e)
            _state.update { it.copy(loading = false, error = "${e.javaClass.simpleName}: ${e.message}") }
            }
        }
    }

    fun cargarDetalle(id: Int) {
        viewModelScope.launch { traerDetalle(id) }
    }

    private suspend fun traerDetalle(id: Int) {
        try {
            val detail = api.getTripDetail(id)
            _state.update { it.copy(detail = detail, error = null) }
        } catch (e: Exception) {
            _state.update { it.copy(error = sinConexion) }
        }
    }

    fun cargarCategorias() {
        viewModelScope.launch {
            try {
                val cats = api.getCategories()
                _state.update { it.copy(categories = cats) }
            } catch (e: Exception) {
                _state.update { it.copy(error = sinConexion) }
            }
        }
    }

    // onResult recibe null si salió bien, o el mensaje de error
    fun crearViaje(
        nombre: String, destino: String, inicio: String, fin: String,
        onResult: (String?) -> Unit
    ) {
        val error = Validador.validarViaje(nombre, destino, inicio, fin)
        if (error != null) {
            onResult(error)
            return
        }
        viewModelScope.launch {
            try {
                val r = api.createTrip(NewTrip(nombre.trim(), destino.trim(), inicio, fin))
                if (r.isSuccessful) onResult(null) else onResult(mensajeError(r))
            } catch (e: Exception) {
                onResult(sinConexion)
            }
        }
    }

    fun registrarGasto(
        tripId: Int, descripcion: String, valorTexto: String,
        categoriaId: Int?, fecha: String,
        onResult: (String?) -> Unit
    ) {
        val detalle = _state.value.detail?.takeIf { it.id == tripId }
        if (detalle == null) {
            onResult("El viaje aún no se ha cargado")
            return
        }
        val error = Validador.validarGasto(
            descripcion, valorTexto, categoriaId, fecha, detalle.startDate, detalle.endDate
        )
        if (error != null) {
            onResult(error)
            return
        }
        val valor = valorTexto.replace(",", ".").toDouble()
        viewModelScope.launch {
            try {
                val r = api.createExpense(
                    NewExpense(tripId, descripcion.trim(), valor, categoriaId!!, fecha)
                )
                if (r.isSuccessful) {
                    traerDetalle(tripId) // refresca lista y total
                    onResult(null)
                } else {
                    onResult(mensajeError(r))
                }
            } catch (e: Exception) {
                onResult(sinConexion)
            }
        }
    }

    private fun <T> mensajeError(r: Response<T>): String {
        return try {
            Gson().fromJson(r.errorBody()?.string(), ApiError::class.java)?.message
                ?: "Error ${r.code()}"
        } catch (e: Exception) {
            "Error ${r.code()}"
        }
    }
}