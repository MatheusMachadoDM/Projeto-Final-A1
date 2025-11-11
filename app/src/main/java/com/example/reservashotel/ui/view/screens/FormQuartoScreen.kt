package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.QuartoViewModel
import com.example.reservashotel.data.model.Quarto
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.KeyboardType


@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormQuartoScreen(
    navController: NavController,
    viewModel: QuartoViewModel,
    quartoId: String? = null
) {
    // ... (Estados, Dropdowns, e Lógica de Carregamento permanecem iguais) ...
    var numero by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Casal") }
    var valorDiaria by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Disponível") }
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }
    val tipos = listOf("Casal", "Solteiro")
    val statusOptions = listOf("Disponível", "Ocupado")
    val dadosCarregados = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(quartoId, dadosCarregados.value) {
        if (quartoId != null && !dadosCarregados.value) {
            scope.launch {
                val quartoExistente = viewModel.carregarQuartoPorId(quartoId)
                quartoExistente?.let { quarto ->
                    numero = quarto.numero.toString()
                    tipo = quarto.tipo
                    valorDiaria = quarto.valorDiaria.toString()
                    status = quarto.status
                    dadosCarregados.value = true
                }
            }
        }
    }


    // --- Estrutura da UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (quartoId == null) "Novo Quarto" else "Editar Quarto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Cancelar e Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // 1. CAMPO NÚMERO DO QUARTO
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número do Quarto") },
                // 🛠️ NOVO: Define o teclado como numérico
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 2. CAMPO TIPO (Menu Suspenso - Permanece igual)
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = tipo,
                    onValueChange = { },
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    tipos.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                tipo = selectionOption
                                expandedTipo = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            // 3. CAMPO VALOR DA DIÁRIA
            OutlinedTextField(
                value = valorDiaria,
                onValueChange = { valorDiaria = it },
                label = { Text("Valor da Diária") },
                // Define o teclado como numérico (com decimal)
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // 4. CAMPO STATUS (Menu Suspenso - Permanece igual)
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = status,
                    onValueChange = { },
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    statusOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                status = selectionOption
                                expandedStatus = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ... (Botão Salvar) ...
            Button(
                onClick = {
                    if (numero.isBlank() || tipo.isBlank() || valorDiaria.toDoubleOrNull() == null) {
                        return@Button
                    }

                    viewModel.salvarQuarto(
                        id = quartoId,
                        numero = numero,
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