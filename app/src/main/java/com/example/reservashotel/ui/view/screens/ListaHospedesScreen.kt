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
import com.example.reservashotel.ui.viewmodel.HospedeViewModel
import com.example.reservashotel.data.model.Hospede

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaHospedesScreen(
    navController: NavController,
    viewModel: HospedeViewModel
) {
    val listaHospedes by viewModel.listaHospedes.collectAsState()

    var hospedeParaExcluir by remember { mutableStateOf<Hospede?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hóspedes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar para a página principal"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("form_hospede") }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Novo Hóspede")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (listaHospedes.isEmpty()) {
                Text(
                    text = "Nenhum hóspede cadastrado.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaHospedes) { hospede ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Nome: ${hospede.nome}", style = MaterialTheme.typography.titleMedium)
                                Text("Id: ${hospede.id}")
                                Text("CPF: ${hospede.cpf}")
                                Text("Telefone: ${hospede.telefone}")

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { navController.navigate("form_hospede?id=${hospede.id}") },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = {
                                            hospedeParaExcluir = hospede
                                            showDeleteDialog = true
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

    if (showDeleteDialog && hospedeParaExcluir != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                hospedeParaExcluir = null
            },
            title = {
                Text(text = "Confirmar Exclusão")
            },
            text = {
                Text(text = "Tem certeza que deseja excluir o hóspede ${hospedeParaExcluir!!.nome}?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.excluirHospede(hospedeParaExcluir!!)
                        showDeleteDialog = false
                        hospedeParaExcluir = null
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
                        hospedeParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}