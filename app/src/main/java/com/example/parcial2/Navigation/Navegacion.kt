package com.example.parcial2.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.parcial2.Screens.CrearViajeScreen
import com.example.parcial2.Screens.DetalleViajeScreen
import com.example.parcial2.Screens.RegistrarGastoScreen
import com.example.parcial2.Screens.ViajesScreen
import com.example.parcial2.ViewModel.GastosViewModel

@Composable
fun Navegacion(vm: GastosViewModel = viewModel()) {
    val nav = rememberNavController()
    val argId = listOf(navArgument("id") { type = NavType.IntType })

    NavHost(navController = nav, startDestination = "viajes") {
        composable("viajes") {
            ViajesScreen(
                vm = vm,
                onCrear = { nav.navigate("crearViaje") },
                onAbrir = { id -> nav.navigate("detalle/$id") }
            )
        }
        composable("crearViaje") {
            CrearViajeScreen(vm = vm, onVolver = { nav.popBackStack() })
        }
        composable("detalle/{id}", arguments = argId) { entry ->
            val id = entry.arguments!!.getInt("id")
            DetalleViajeScreen(
                vm = vm,
                tripId = id,
                onVolver = { nav.popBackStack() },
                onRegistrar = { nav.navigate("registrarGasto/$id") }
            )
        }
        composable("registrarGasto/{id}", arguments = argId) { entry ->
            val id = entry.arguments!!.getInt("id")
            RegistrarGastoScreen(vm = vm, tripId = id, onVolver = { nav.popBackStack() })
        }
    }
}