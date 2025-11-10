package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.QuartoViewModel
import com.example.reservashotel.data.model.Quarto // Importe sua classe de modelo Quarto
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormQuartoScreen(
    navController: NavController,
    viewModel: QuartoViewModel,
    quartoId: String? = null
) {
    // ESTADOS DOS CAMPOS
    var numero by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    // O valorDiaria é uma String para o TextField
    var valorDiaria by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Disponível") }

    // Variável de controle para garantir que o carregamento só ocorra uma vez
    val dadosCarregados = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    // Carregamento de Dados para Edição

    // Usa LaunchedEffect para carregar os dados do Quarto ao entrar na tela (se quartoId não for nulo)
    LaunchedEffect(quartoId, dadosCarregados.value) {
        if (quartoId != null && !dadosCarregados.value) {
            scope.launch {
                // Supondo que você tem esta função no QuartoViewModel
                val quartoExistente = viewModel.carregarQuartoPorId(quartoId)

                quartoExistente?.let { quarto ->
                    // Preenche os estados com os dados carregados
                    numero = quarto.numero.toString()
                    tipo = quarto.tipo
                    // Converte o Double (valorDiaria) para String para exibição no TextField
                    valorDiaria = quarto.valorDiaria.toString()
                    status = quarto.status

                    dadosCarregados.value = true // Marca como carregado
                }
            }
        }
    }


    // Estrutura da UI

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

            // ... (Campos de Texto) ...

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

            // Campo valorDiaria
            OutlinedTextField(
                value = valorDiaria,
                onValueChange = { valorDiaria = it },
                label = { Text("Valor da Diária") },
                // DICA: Adicione keyboardOptions(keyboardType = KeyboardType.Number) para melhor UX
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status (Disponível/Ocupado)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ... (Botão Salvar) ...

            Button(
                onClick = {
                    // Validação simples de preenchimento e conversão
                    if (numero.isBlank() || tipo.isBlank() || valorDiaria.toDoubleOrNull() == null) {
                        // Adicionar lógica de validação aqui
                        return@Button
                    }

                    viewModel.salvarQuarto(
                        id = quartoId, // Passa o ID existente para atualizar
                        numero = numero,
                        tipo = tipo,
                        // Converte a String de volta para Double para salvar
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