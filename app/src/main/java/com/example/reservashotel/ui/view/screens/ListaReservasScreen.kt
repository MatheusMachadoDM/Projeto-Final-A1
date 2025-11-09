package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.ReservaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ListaReservasScreen(
    navController: NavController,
    viewModel: ReservaViewModel
) {
    val reservas by viewModel.listaReservas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reservas") },
                actions = {
                    Button(onClick = { navController.navigate("form_reserva") }) {
                        Text("Nova")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (reservas.isEmpty()) {
                Text(
                    text = "Nenhuma reserva encontrada.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(reservas) { reserva ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Cliente: ${reserva.nomeCliente}", style = MaterialTheme.typography.titleMedium)
                                Text("Quarto ID: ${reserva.quartoId}")
                                Text("Check-in: ${formatMillisToDate(reserva.dataCheckIn)}")
                                Text("Check-out: ${formatMillisToDate(reserva.dataCheckOut)}")
                                Text("Status: ${reserva.status}")

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = { navController.navigate("form_reserva?id=${reserva.id}") }) {
                                        Text("Editar")
                                    }
                                    Button(onClick = { viewModel.excluirReserva(reserva) }) {
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

fun formatMillisToDate(millis: Long): String {
    return try {
        if (millis <= 0L) return ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.format(Date(millis))
    } catch (e: Exception) {
        ""
    }
}
