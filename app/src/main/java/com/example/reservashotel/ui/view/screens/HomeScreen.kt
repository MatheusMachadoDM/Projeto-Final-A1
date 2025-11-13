package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, usuarioViewModel: UsuarioViewModel) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HotelApp - Início") },
                actions = {
                    IconButton(
                        onClick = { showLogoutDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Logout"
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bem-vindo ao sistema de reservas do hotel!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.navigate("lista_quartos") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gerenciar Quartos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("lista_hospedes") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gerenciar Hóspedes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("lista_reservas") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gerenciar Reservas")
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text(text = "Confirmar Saída")
            },
            text = {
                Text(text = "Tem certeza de que deseja sair do sistema?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        usuarioViewModel.logout()
                        navController.navigate("login_screen") {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                ) {
                    Text("Sair")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}