package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.QuartoViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormQuartoScreen(
    navController: NavController,
    viewModel: QuartoViewModel,
    quartoId: String? = null
) {
    var numero by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var valorDiaria by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Disponível") }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (quartoId == null) "Novo Quarto" else "Editar Quarto") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número do Quarto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo (ex: Casal, Solteiro)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = valorDiaria,
                onValueChange = { valorDiaria = it },
                label = { Text("Valor da Diária") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status (Disponível/Ocupado)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Chamada ajustada para o ViewModel corrigido
                    viewModel.salvarQuarto(
                        id = quartoId,
                        numero = numero, // Passa String
                        tipo = tipo,
                        valorDiaria = valorDiaria.toDoubleOrNull() ?: 0.0,
                        status = status
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}