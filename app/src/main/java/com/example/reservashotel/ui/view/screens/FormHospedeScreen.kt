package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.HospedeViewModel
import com.example.reservashotel.data.model.Hospede // Certifique-se de que este import está correto
import kotlinx.coroutines.launch

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormHospedeScreen(
    navController: NavController,
    viewModel: HospedeViewModel,
    hospedeId: String? = null
) {
    // ESTADOS DOS CAMPOS
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    // Variável de controle para garantir que o carregamento só ocorra uma vez
    val dadosCarregados = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    // --- Carregamento de Dados para Edição ---
    LaunchedEffect(hospedeId, dadosCarregados.value) {
        if (hospedeId != null && !dadosCarregados.value) {
            scope.launch {
                // Presumimos que o ViewModel tem a função carregarHospedePorId(id: String)
                val hospedeExistente = viewModel.carregarHospedePorId(hospedeId)

                hospedeExistente?.let { hospede ->
                    // Preenche os estados com os dados carregados
                    nome = hospede.nome
                    cpf = hospede.cpf
                    email = hospede.email
                    telefone = hospede.telefone

                    dadosCarregados.value = true // Marca como carregado
                }
            }
        }
    }


    // --- Estrutura da UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (hospedeId == null) "Novo Hóspede" else "Editar Hóspede") },
                // 🌟 NOVO: Botão de Cancelar/Voltar (Navigation Icon)
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Cancelar e Voltar"
                        )
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
            // Campos de texto...
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = cpf,
                onValueChange = { cpf = it },
                label = { Text("CPF") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefone,
                onValueChange = { telefone = it },
                label = { Text("Telefone") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Salvar
            Button(
                onClick = {
                    // Validação simples
                    if (nome.isBlank() || cpf.isBlank() || telefone.isBlank()) {
                        // Adicionar lógica de validação visual aqui
                        return@Button
                    }

                    // Chama a função de salvar no ViewModel
                    viewModel.salvarHospede(
                        id = hospedeId, // Passa o ID existente para atualização
                        nome = nome,
                        cpf = cpf,
                        email = email,
                        telefone = telefone
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