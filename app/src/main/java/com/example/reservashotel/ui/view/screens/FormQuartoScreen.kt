package com.example.reservashotel.ui.view.screens

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
import androidx.compose.ui.Alignment

/**
 * Tela de formulário para cadastro e edição de Quartos.
 *
 * @param navController Controlador de navegação para mudar de tela.
 * @param viewModel ViewModel que contém a lógica de salvar e carregar quartos.
 * @param quartoId ID do quarto a ser editado (String, pode ser nulo para cadastro).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormQuartoScreen(
    navController: NavController,
    viewModel: QuartoViewModel,
    quartoId: String? = null
) {
    // --- Variáveis de Estado (Campos de Entrada) ---
    var numero by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Casal") }
    var valorDiaria by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Disponível") }

    // --- Variáveis de Estado (Controle de Dropdown) ---
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }

    // --- Opções de Dropdown ---
    val tipos = listOf("Casal", "Solteiro")
    val statusOptions = listOf("Disponível", "Ocupado")

    // --- Variáveis de Estado (Controle de Carregamento) ---
    val dadosCarregados = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }


    /**
     * Efeito que é lançado quando a tela é composta (ou quando quartoId muda).
     * Responsável por carregar dados para EDICAO (se quartoId não for nulo).
     */
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


    // --- Estrutura da UI (Scaffold e TopBar) ---
    Scaffold(
        // 🌟 ADICIONADO: Define o SnackbarHost no Scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
        // Coluna principal para organizar os campos do formulário verticalmente.
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 2. CAMPO TIPO (Menu Suspenso)
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
                // Cria as opções do menu suspenso.
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // 4. CAMPO STATUS (Menu Suspenso)
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
                // Cria as opções de status do menu suspenso.
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

            // --- Botão Salvar ---
            Button(
                onClick = {
                    val valorDiariaDouble = valorDiaria.toDoubleOrNull()

                    if (numero.isBlank() || tipo.isBlank() || valorDiariaDouble == null || valorDiariaDouble <= 0.0) {

                        // Exibe o Snackbar se a validação falhar
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Preencha o Número e o Valor da Diária corretamente (Valor deve ser maior que zero).",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@Button
                    }

                    // Chama a função de salvar no ViewModel.
                    viewModel.salvarQuarto(
                        id = quartoId,
                        numero = numero,
                        tipo = tipo,
                        valorDiaria = valorDiariaDouble,
                        status = status
                    )
                    // Volta para a tela anterior (ListaQuartosScreen).
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}