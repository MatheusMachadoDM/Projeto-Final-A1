package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HotelApp - Início") }
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
                onClick = { navController.navigate("lista_reservas") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gerenciar Reservas")
            }
        }
    }
}
