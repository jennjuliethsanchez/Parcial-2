package com.example.parcial2.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial2.ViewModel.GastosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViajesScreen(vm: GastosViewModel, onCrear: () -> Unit, onAbrir: (Int) -> Unit) {
    val state by vm.state.collectAsState()
    val error = state.error

    LaunchedEffect(Unit) { vm.cargarViajes() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis viajes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCrear) {
                Icon(Icons.Default.Add, contentDescription = "Crear viaje")
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                state.loading && state.trips.isEmpty() ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                error != null && state.trips.isEmpty() ->
                    Column(
                        Modifier.align(Alignment.Center).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { vm.cargarViajes() }) { Text("Reintentar") }
                    }

                state.trips.isEmpty() ->
                    Text(
                        "Aún no hay viajes. Crea el primero con el botón +",
                        Modifier.align(Alignment.Center).padding(24.dp)
                    )

                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.trips, key = { it.id }) { trip ->
                        Card(Modifier.fillMaxWidth().clickable { onAbrir(trip.id) }) {
                            Column(Modifier.padding(16.dp)) {
                                Text(trip.name, style = MaterialTheme.typography.titleMedium)
                                Text(trip.destination)
                                Text(
                                    "${trip.startDate} → ${trip.endDate}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    "${trip.expenseCount} gastos · \$%,.2f".format(trip.totalExpenses),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}