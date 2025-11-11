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
import com.example.reservashotel.ui.viewmodel.HospedeViewModel
import com.example.reservashotel.data.model.Hospede
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.KeyboardType

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

    // ... (Lógica de carregamento e estados permanecem iguais) ...
    val dadosCarregados = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(hospedeId, dadosCarregados.value) {
        if (hospedeId != null && !dadosCarregados.value) {
            scope.launch {
                val hospedeExistente = viewModel.carregarHospedePorId(hospedeId)
                hospedeExistente?.let { hospede ->
                    nome = hospede.nome
                    cpf = hospede.cpf
                    email = hospede.email
                    telefone = hospede.telefone
                    dadosCarregados.value = true
                }
            }
        }
    }


    // --- Estrutura da UI ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (hospedeId == null) "Novo Hóspede" else "Editar Hóspede") },
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

            // 1. Campo Nome (Teclado padrão)
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth()
            )

            // 2. Campo CPF
            OutlinedTextField(
                value = cpf,
                onValueChange = {
                    // DICA: Adicionar um filtro aqui para aceitar APENAS dígitos.
                    if (it.length <= 11) cpf = it
                },
                label = { Text("CPF") },
                // 🛠️ NOVO: Teclado numérico (ideal para CPF e Telefone)
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 3. Campo E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                // 🛠️ NOVO: Teclado otimizado para e-mail (inclui @ e .)
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            // 4. Campo Telefone
            OutlinedTextField(
                value = telefone,
                onValueChange = {
                    // DICA: Adicionar um filtro aqui para aceitar APENAS dígitos.
                    telefone = it
                },
                label = { Text("Telefone") },
                // 🛠️ NOVO: Teclado numérico
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Salvar
            Button(
                onClick = {
                    if (nome.isBlank() || cpf.isBlank() || telefone.isBlank()) {
                        return@Button
                    }

                    viewModel.salvarHospede(
                        id = hospedeId,
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