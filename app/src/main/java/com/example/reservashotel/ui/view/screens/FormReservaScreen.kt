package com.example.reservashotel.ui.view.screens
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.ReservaViewModel

// =========================================================================
// FUNÇÕES AUXILIARES (DE FORA DO @Composable)
// =========================================================================

fun parseDateStringToMillis(dateStr: String): Long {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.parse(dateStr)
        date?.time ?: 0L
    } catch (e: Exception) {
        0L
    }
}

fun formatMillisToDateString(millis: Long): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.format(Date(millis))
    } catch (e: Exception) {
        ""
    }
}

// =========================================================================
// TELA COMPOSABLE
// =========================================================================

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FormReservaScreen(
    navController: NavController,
    viewModel: ReservaViewModel,
    reservaId: String? = null
) {
    // ESTADOS DOS CAMPOS (serão preenchidos se for edição)
    var quartoId by remember { mutableStateOf("") }
    var hospedeId by remember { mutableStateOf("") }
    var nomeCliente by remember { mutableStateOf("") }
    var dataCheckIn by remember { mutableStateOf("") }
    var dataCheckOut by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("ativa") }

    // VARIÁVEL DE CONTROLE para evitar que o carregamento se repita
    val dadosCarregados = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope() // CoroutineScope, embora LaunchEffect já crie um, é bom para consistência.

    // LÓGICA DE CARREGAMENTO DE DADOS (USANDO LaunchedEffect)
    LaunchedEffect(reservaId, dadosCarregados.value) {
        // Se estamos em modo de edição E os dados ainda não foram carregados...
        if (reservaId != null && !dadosCarregados.value) {
            // Chamada de suspensão dentro do LaunchedEffect
            val reservaExistente = viewModel.carregarReservaPorId(reservaId)

            // Se a reserva for encontrada, preenche os estados
            reservaExistente?.let { reserva ->
                quartoId = reserva.quartoId
                hospedeId = reserva.hospedeId
                nomeCliente = reserva.nomeCliente
                dataCheckIn = formatMillisToDateString(reserva.dataCheckIn)
                dataCheckOut = formatMillisToDateString(reserva.dataCheckOut)
                status = reserva.status

                dadosCarregados.value = true // Marca como carregado
            }
        }
    }

    // ESTRUTURA DA UI
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

            // =========================
            // CAMPOS DE TEXTO
            // =========================

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
                label = { Text("Status (Ativa/Concluída/Cancelada)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // =========================
            // BOTÃO SALVAR
            // =========================

            Button(
                onClick = {
                    val inMillis = parseDateStringToMillis(dataCheckIn)
                    val outMillis = parseDateStringToMillis(dataCheckOut)

                    if (nomeCliente.isBlank() || quartoId.isBlank() || inMillis == 0L || outMillis == 0L) {
                        // (Adicionar lógica de validação aqui, ex: um Snackbar)
                        return@Button
                    }

                    // Chama a função de salvar no ViewModel
                    viewModel.salvarReserva(
                        id = reservaId, // Se for edição, o ID é passado; se for novo, é null
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