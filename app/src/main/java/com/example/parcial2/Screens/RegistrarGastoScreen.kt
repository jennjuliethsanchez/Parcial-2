package com.example.parcial2.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.parcial2.Model.Category
import com.example.parcial2.ViewModel.GastosViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarGastoScreen(vm: GastosViewModel, tripId: Int, onVolver: () -> Unit) {
    val state by vm.state.collectAsState()
    val detail = state.detail?.takeIf { it.id == tripId }

    var descripcion by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf<Category?>(null) }
    var menuAbierto by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }

    LaunchedEffect(tripId) {
        vm.cargarDetalle(tripId)
        vm.cargarCategorias()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar gasto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (detail != null) {
                Text(
                    "${detail.name} (${detail.startDate} → ${detail.endDate})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            OutlinedTextField(
                value = descripcion, onValueChange = { descripcion = it },
                label = { Text("Descripción") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = valor, onValueChange = { valor = it },
                label = { Text("Valor") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Box {
                OutlinedButton(
                    onClick = { menuAbierto = true },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(categoria?.name ?: "Selecciona una categoría") }

                DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                    state.categories.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c.name) },
                            onClick = {
                                categoria = c
                                menuAbierto = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = fecha, onValueChange = { fecha = it },
                label = { Text("Fecha del gasto (AAAA-MM-DD)") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    guardando = true
                    error = null
                    vm.registrarGasto(tripId, descripcion, valor, categoria?.id, fecha) { err ->
                        guardando = false
                        if (err == null) onVolver() else error = err
                    }
                },
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar gasto") }
        }
    }
}