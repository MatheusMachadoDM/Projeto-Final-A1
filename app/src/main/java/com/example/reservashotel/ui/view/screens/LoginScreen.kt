package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.UsuarioViewModel // Assumindo que você criou este ViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel // Recebe o ViewModel de login
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val loginSucesso by viewModel.loginSucesso.collectAsState()
    val mensagemErro by viewModel.mensagemErro.collectAsState()

    // Efeito colateral de navegação após o sucesso do login
    LaunchedEffect(loginSucesso) {
        if (loginSucesso) {
            // Navega para a tela principal e remove a tela de login da back stack
            navController.navigate("home") {
                popUpTo("login_screen") { inclusive = true }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Acesso ao Sistema", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Senha") },
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            mensagemErro?.let { erro ->
                Text(
                    text = erro,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // ⬅️ Chama a função de login no ViewModel
                    viewModel.realizarLogin(email, senha)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && senha.isNotBlank()
            ) {
                Text("Entrar")
            }

            val loginSucesso by viewModel.loginSucesso.collectAsState()

            LaunchedEffect(loginSucesso) {
                if (loginSucesso) {
                    navController.navigate("home") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            }
        }
    }
}
