package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.clickable // Importação não utilizada, mas mantida.
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions // Importação para configurar o teclado de entrada.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Ícone para navegação de volta.
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importações essenciais para o Compose.
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // Gerenciamento de navegação.
import com.example.reservashotel.ui.viewmodel.QuartoViewModel // ViewModel da camada de lógica.
import com.example.reservashotel.data.model.Quarto // Modelo de dados Quarto.
import kotlinx.coroutines.launch // Uso de coroutines para operações assíncronas.
import androidx.compose.ui.text.input.KeyboardType // Tipos de teclado de entrada.


/**
 * Tela de formulário para cadastro e edição de Quartos.
 *
 * @param navController Controlador de navegação para mudar de tela.
 * @param viewModel ViewModel que contém a lógica de salvar e carregar quartos.
 * @param quartoId ID do quarto a ser editado (String, pode ser nulo para cadastro).
 */
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormQuartoScreen(
    navController: NavController,
    viewModel: QuartoViewModel,
    quartoId: String? = null
) {
    // --- Variáveis de Estado (Campos de Entrada) ---
    // Usam remember/mutableStateOf para que a UI se recomponha quando os valores mudarem.
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
    val scope = rememberCoroutineScope() // Escopo para chamar funções suspensas

    /**
     * Efeito que é lançado quando a tela é composta (ou quando quartoId muda).
     * Responsável por carregar dados para EDICAO (se quartoId não for nulo).
     */
    LaunchedEffect(quartoId, dadosCarregados.value) {
        // Verifica se é uma tela de edição e se os dados ainda não foram carregados.
        if (quartoId != null && !dadosCarregados.value) {
            scope.launch {
                // Chama a função suspensa no ViewModel para buscar o quarto.
                val quartoExistente = viewModel.carregarQuartoPorId(quartoId)

                quartoExistente?.let { quarto ->
                    // Preenche os estados locais com os dados do quarto existente.
                    numero = quarto.numero.toString()
                    tipo = quarto.tipo
                    valorDiaria = quarto.valorDiaria.toString()
                    status = quarto.status
                    dadosCarregados.value = true // Marca como carregado para evitar recarga infinita.
                }
            }
        }
    }


    // --- Estrutura da UI (Scaffold e TopBar) ---
    Scaffold(
        topBar = {
            TopAppBar(
                // O título reflete se é cadastro ou edição.
                title = { Text(if (quartoId == null) "Novo Quarto" else "Editar Quarto") },
                navigationIcon = {
                    // Botão de voltar que retorna à tela anterior na pilha de navegação.
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
                .padding(padding) // Aplica o padding fornecido pelo Scaffold (TopBar)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Espaçamento entre os campos.
        ) {

            // 1. CAMPO NÚMERO DO QUARTO
            OutlinedTextField(
                value = numero,
                onValueChange = { numero = it },
                label = { Text("Número do Quarto") },
                // Define o teclado para aceitar apenas números (Integer).
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
                // Define o teclado como numérico com suporte a casas decimais.
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
                    // Validação básica: verifica se campos essenciais não estão vazios/nulos.
                    if (numero.isBlank() || tipo.isBlank() || valorDiaria.toDoubleOrNull() == null) {
                        // Poderia adicionar um Snackbar aqui para alertar o usuário.
                        return@Button
                    }

                    // Chama a função de salvar no ViewModel.
                    viewModel.salvarQuarto(
                        id = quartoId, // Passa null para novo ou o ID para edição.
                        numero = numero,
                        tipo = tipo,
                        valorDiaria = valorDiaria.toDoubleOrNull() ?: 0.0, // Converte String para Double.
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