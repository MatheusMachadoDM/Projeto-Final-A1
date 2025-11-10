package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.ReservaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormReservaScreen(
    navController: NavController,
    viewModel: ReservaViewModel,
    reservaId: String? = null
) {
    var quartoId by remember { mutableStateOf("") }
    var hospedeId by remember { mutableStateOf("") }
    var nomeCliente by remember { mutableStateOf("") }
    var dataCheckIn by remember { mutableStateOf("") }
    var dataCheckOut by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("ativa") }

    // opcional: carregar reserva existente se reservaId != null (pode ser adicionada)
    // exemplo simples: scope launch { val r = viewModel.carregarReservaPorId(reservaId!!); preencher campos }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (reservaId == null) "Nova Reserva" else "Editar Reserva") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = quartoId,
                onValueChange = { quartoId = it },
                label = { Text("ID do Quarto") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = hospedeId,
                onValueChange = { hospedeId = it },
                label = { Text("ID do Hóspede") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nomeCliente,
                onValueChange = { nomeCliente = it },
                label = { Text("Nome do Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dataCheckIn,
                onValueChange = { dataCheckIn = it },
                label = { Text("Data Check-in (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dataCheckOut,
                onValueChange = { dataCheckOut = it },
                label = { Text("Data Check-out (yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status (ativa/concluída/cancelada)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val inMillis = parseDateStringToMillis(dataCheckIn)
                    val outMillis = parseDateStringToMillis(dataCheckOut)

                    if (nomeCliente.isBlank() || quartoId.isBlank() || inMillis == 0L || outMillis == 0L) {
                        // mostrar validação (Snackbar/Toast)
                        return@Button
                    }

                    viewModel.salvarReserva(
                        id = reservaId,
                        quartoId = quartoId,
                        hospedeId = hospedeId,
                        nomeCliente = nomeCliente,
                        dataCheckIn = inMillis,
                        dataCheckOut = outMillis,
                        status = status
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

fun parseDateStringToMillis(dateStr: String): Long {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(dateStr)
        date?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}
