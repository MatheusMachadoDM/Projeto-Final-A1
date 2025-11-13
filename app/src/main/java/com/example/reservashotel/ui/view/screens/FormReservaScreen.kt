package com.example.reservashotel.ui.view.screens

import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.ReservaViewModel
import kotlinx.coroutines.flow.collectLatest

// =========================================================================
// FUNÇÕES AUXILIARES (Inalteradas)
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

fun String.toTitleCase(): String {
    return if (this.isNotBlank()) {
        this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    } else this
}

// =========================================================================
// TELA COMPOSABLE
// =========================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormReservaScreen(
    navController: NavController,
    viewModel: ReservaViewModel,
    reservaId: String? = null
) {
    // ESTADOS DOS CAMPOS (Inalterados)
    var quartoId by remember { mutableStateOf("") }
    var hospedeId by remember { mutableStateOf("") }
    var nomeCliente by remember { mutableStateOf("") }
    var dataCheckIn by remember { mutableStateOf("") }
    var dataCheckOut by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Ativa") }

    // ESTADOS PARA DROPDOWN E DATE PICKERS
    var expandedStatus by remember { mutableStateOf(false) }
    val statusOptions = listOf("Ativa", "Concluída", "Cancelada", "Em Andamento")

    val dadosCarregados = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val mensagemErro by viewModel.mensagemErro.collectAsState()


    // LÓGICA DE CARREGAMENTO DE DADOS (Edição)
    LaunchedEffect(reservaId, dadosCarregados.value) {
        if (reservaId != null && !dadosCarregados.value) {
            val reservaExistente = viewModel.carregarReservaPorId(reservaId)

            reservaExistente?.let { reserva ->
                quartoId = reserva.quartoId
                hospedeId = reserva.hospedeId
                nomeCliente = reserva.nomeCliente

                dataCheckIn = formatMillisToDateString(reserva.dataCheckIn)
                dataCheckOut = formatMillisToDateString(reserva.dataCheckOut)

                status = reserva.status
                dadosCarregados.value = true
            }
        }
    }

    LaunchedEffect(mensagemErro) {
        mensagemErro?.let { erro ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = erro,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
                viewModel.limparMensagemErro()
            }
        }
    }
    // Observa o SharedFlow do ViewModel e retorna APENAS se o salvamento for bem-sucedido.
    LaunchedEffect(Unit) {
        viewModel.navegarDeVolta.collectLatest {
            navController.popBackStack()
        }
    }


    // --- ESTRUTURA DA UI ---
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (reservaId == null) "Nova Reserva" else "Editar Reserva") },
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
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ... (Campos do formulário) ...

            // 1. ID DO QUARTO
            OutlinedTextField(
                value = quartoId,
                onValueChange = { quartoId = it },
                label = { Text("ID do Quarto") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 2. ID DO HÓSPEDE
            OutlinedTextField(
                value = hospedeId,
                onValueChange = {
                    hospedeId = it
                    // LÓGICA DE BUSCA REATIVA
                    if (it.isNotBlank() && reservaId == null) {
                        scope.launch {
                            val nome = viewModel.buscarNomeHospede(it)
                            if (nome != null) nomeCliente = nome
                        }
                    }
                },
                label = { Text("ID do Hóspede") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 3. NOME DO CLIENTE
            OutlinedTextField(
                value = nomeCliente,
                onValueChange = { nomeCliente = it },
                label = { Text("Nome do Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            // 4. DATA CHECK-IN
            OutlinedTextField(
                value = dataCheckIn,
                onValueChange = { dataCheckIn = it },
                label = { Text("Data Check-in (AAAA-MM-DD)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 5. DATA CHECK-OUT
            OutlinedTextField(
                value = dataCheckOut,
                onValueChange = { dataCheckOut = it },
                label = { Text("Data Check-out (AAAA-MM-DD)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // 6. STATUS (Menu Suspenso - Inalterado)
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = status.toTitleCase(),
                    onValueChange = { },
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false }
                ) {
                    statusOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.toTitleCase()) },
                            onClick = {
                                status = selectionOption
                                expandedStatus = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = {
                    val inMillis = parseDateStringToMillis(dataCheckIn)
                    val outMillis = parseDateStringToMillis(dataCheckOut)

                    // Validação de UI (Campos vazios)
                    if (nomeCliente.isBlank() || quartoId.isBlank() || hospedeId.isBlank() || inMillis == 0L || outMillis == 0L) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Preencha todos os campos e use o formato AAAA-MM-DD para as datas.",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                        }
                        return@Button
                    }

                    scope.launch {
                        viewModel.salvarReserva(
                            id = reservaId,
                            quartoId = quartoId,
                            hospedeId = hospedeId,
                            nomeCliente = nomeCliente,
                            dataCheckIn = inMillis,
                            dataCheckOut = outMillis,
                            status = status.lowercase(Locale.getDefault())
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}