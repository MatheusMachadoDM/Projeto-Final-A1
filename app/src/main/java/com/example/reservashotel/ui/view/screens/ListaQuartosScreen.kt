package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.data.model.Quarto
import com.example.reservashotel.ui.viewmodel.QuartoViewModel

/**
 * Tela de listagem de quartos. Permite visualizar, editar e excluir quartos cadastrados.
 *
 * @param navController Controlador de navegação.
 * @param viewModel O ViewModel que gerencia os dados de Quartos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaQuartosScreen(
    navController: NavController,
    viewModel: QuartoViewModel
) {
    // Coleta a lista de quartos (Flow) do ViewModel como um State.
    // Isso garante que a UI seja atualizada automaticamente (reativamente) sempre que a lista mudar no banco de dados.
    val listaQuartos by viewModel.listaQuartos.collectAsState()

    // 🌟 NOVO ESTADO: Armazena o quarto que será excluído
    var quartoParaExcluir by remember { mutableStateOf<Quarto?>(null) }

    // 🌟 NOVO ESTADO: Controla a visibilidade do popup de confirmação
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        // O Scaffold fornece a estrutura básica (TopBar, Content, FAB, etc.).

        // 1. TOP BAR COM BOTÃO DE VOLTAR
        topBar = {
            TopAppBar(
                title = { Text("Quartos") },
                // Adiciona o ícone de voltar no canto superior esquerdo (Navigation Icon).
                navigationIcon = {
                    // Retorna para a tela anterior na pilha de navegação (HomeScreen).
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar para a página principal"
                        )
                    }
                },
                // A seção de ações (canto direito) foi deixada vazia, movendo o botão 'Novo' para o FAB.
                actions = { /* Deixado vazio ou para outras ações */ }
            )
        },

        // 2. FLOATING ACTION BUTTON (FAB) na parte inferior
        floatingActionButton = {
            // Botão flutuante para iniciar a navegação para a tela de cadastro.
            FloatingActionButton(onClick = { navController.navigate("form_quarto") }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Novo Quarto")
            }
        }
    ) { padding ->
        // O Box serve como um contêiner que pode centralizar elementos.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Aplica o padding fornecido pelo Scaffold (TopBar/FAB)
        ) {
            // Condicional: Se a lista estiver vazia, exibe uma mensagem no centro.
            if (listaQuartos.isEmpty()) {
                Text(
                    text = "Nenhum quarto cadastrado.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // LazyColumn é usado para renderizar listas grandes de forma eficiente.
                // Ele só compõe os itens que estão visíveis na tela.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Espaçamento entre os cards.
                ) {
                    // Itera sobre a lista de quartos.
                    items(listaQuartos) { quarto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                // Exibição dos dados do quarto.
                                Text("Número: ${quarto.numero}", style = MaterialTheme.typography.titleMedium)
                                Text("Id: ${quarto.id}")
                                Text("Tipo: ${quarto.tipo}")
                                Text("Valor diária: R$${quarto.valorDiaria}")
                                Text("Status: ${quarto.status}")

                                Spacer(modifier = Modifier.height(8.dp))

                                // --- Botões de Ação ---
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        // Navega para a tela de formulário, passando o ID do quarto como parâmetro
                                        // para indicar que é uma operação de EDIÇÃO.
                                        onClick = { navController.navigate("form_quarto?id=${quarto.id}") },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = {
                                            quartoParaExcluir = quarto // Armazena o quarto a ser excluído
                                            showDeleteDialog = true // Abre o diálogo
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Excluir")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    if (showDeleteDialog && quartoParaExcluir != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                quartoParaExcluir = null
            },
            title = {
                Text(text = "Confirmar Exclusão")
            },
            text = {
                // Exibe o número do quarto para confirmação
                Text(text = "Tem certeza que deseja excluir o quarto ${quartoParaExcluir!!.numero}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.excluirQuarto(quartoParaExcluir!!)
                        showDeleteDialog = false
                        quartoParaExcluir = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        quartoParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}