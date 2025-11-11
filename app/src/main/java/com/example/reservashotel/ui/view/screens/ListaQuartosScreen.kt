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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ListaQuartosScreen(
    navController: NavController,
    viewModel: QuartoViewModel
) {
    // Coleta a lista de quartos de forma reativa
    val listaQuartos by viewModel.listaQuartos.collectAsState()

    Scaffold(
        // 1. TOP BAR COM BOTÃO DE VOLTAR
        topBar = {
            TopAppBar(
                title = { Text("Quartos") },
                // Adiciona o ícone de voltar no canto superior esquerdo
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar para a página principal"
                        )
                    }
                },
                // Remove o botão "Novo" daqui
                actions = { /* Deixado vazio ou para outras ações */ }
            )
        },
        // 2. FLOATING ACTION BUTTON (FAB) na parte inferior
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("form_quarto") }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Novo Quarto")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (listaQuartos.isEmpty()) {
                Text(
                    text = "Nenhum quarto cadastrado.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaQuartos) { quarto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Número: ${quarto.numero}", style = MaterialTheme.typography.titleMedium)
                                Text("Id: ${quarto.id}")
                                Text("Tipo: ${quarto.tipo}")
                                Text("Valor diária: R$${quarto.valorDiaria}")
                                Text("Status: ${quarto.status}")

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        // Navega para a tela de formulário, passando o ID do quarto para edição
                                        onClick = { navController.navigate("form_quarto?id=${quarto.id}") },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = { viewModel.excluirQuarto(quarto) },
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
}