package com.example.parcial2.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.parcial2.ViewModel.GastosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearViajeScreen(vm: GastosViewModel, onVolver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var inicio by remember { mutableStateOf("") }
    var fin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo viaje") },
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
            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = destino, onValueChange = { destino = it },
                label = { Text("Destino") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = inicio, onValueChange = { inicio = it },
                label = { Text("Fecha de inicio (AAAA-MM-DD)") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fin, onValueChange = { fin = it },
                label = { Text("Fecha de fin (AAAA-MM-DD)") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    guardando = true
                    error = null
                    vm.crearViaje(nombre, destino, inicio, fin) { err ->
                        guardando = false
                        if (err == null) onVolver() else error = err
                    }
                },
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar viaje") }
        }
    }
}