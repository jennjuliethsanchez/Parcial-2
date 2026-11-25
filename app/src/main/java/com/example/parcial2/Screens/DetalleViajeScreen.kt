package com.example.parcial2.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.parcial2.Model.Calculadora
import com.example.parcial2.ViewModel.GastosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleViajeScreen(
    vm: GastosViewModel,
    tripId: Int,
    onVolver: () -> Unit,
    onRegistrar: () -> Unit
) {
    val state by vm.state.collectAsState()
    val error = state.error
    val detail = state.detail?.takeIf { it.id == tripId }

    LaunchedEffect(tripId) { vm.cargarDetalle(tripId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del viaje") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (detail != null) {
                ExtendedFloatingActionButton(
                    onClick = onRegistrar,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Registrar gasto") }
                )
            }
        }
    ) { padding ->
        if (detail == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
                else CircularProgressIndicator()
            }
        } else {
            val total = Calculadora.calcularTotal(detail.expenses)

            LazyColumn(
                Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(detail.name, style = MaterialTheme.typography.headlineSmall)
                            Text(detail.destination)
                            Text(
                                "${detail.startDate} → ${detail.endDate}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(12.dp))
                            Text("Total de gastos", style = MaterialTheme.typography.labelMedium)
                            Text(
                                "\$%,.2f".format(total),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Text(
                                "${detail.expenses.size} gastos registrados",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                item { Text("Gastos", style = MaterialTheme.typography.titleMedium) }

                if (detail.expenses.isEmpty()) {
                    item { Text("Este viaje aún no tiene gastos registrados.") }
                }

                items(detail.expenses, key = { it.id }) { g ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(g.description, style = MaterialTheme.typography.titleSmall)
                                Text(
                                    "${g.categoryName} · ${g.expenseDate}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text("\$%,.2f".format(g.amount))
                        }
                    }
                }
            }
        }
    }
}