package com.example.reservashotel.ui.view.screens

import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.clickable
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

// =========================================================================
// FUNÇÕES AUXILIARES
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

// Função de extensão para capitalizar a primeira letra (Substitui capitalize())
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
    // ESTADOS DOS CAMPOS
    var quartoId by remember { mutableStateOf("") }
    var hospedeId by remember { mutableStateOf("") }
    var nomeCliente by remember { mutableStateOf("") }
    var dataCheckIn by remember { mutableStateOf("") }
    var dataCheckOut by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("ativa") }

    // ESTADOS PARA DROPDOWN E DATE PICKERS
    var expandedStatus by remember { mutableStateOf(false) }
    val statusOptions = listOf("ativa", "concluída", "cancelada")

    var showCheckInPicker by remember { mutableStateOf(false) }
    var showCheckOutPicker by remember { mutableStateOf(false) }

    val dadosCarregados = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // --- 1. Estado para os Milissegundos Iniciais (para o DatePicker) ---
    val initialCheckInMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val initialCheckOutMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }

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

                // Atualiza o estado de Long para re-inicializar o DatePickerState
                initialCheckInMillis.longValue = reserva.dataCheckIn
                initialCheckOutMillis.longValue = reserva.dataCheckOut

                status = reserva.status
                dadosCarregados.value = true
            }
        }
    }

    // --- 2. Date Picker Dialogs (Correção do Locale e do remember(key)) ---

    // CORREÇÃO: Usamos remember(key) e adicionamos locale = Locale.getDefault()
    val datePickerStateIn = remember(initialCheckInMillis.longValue) {
        DatePickerState(
            initialSelectedDateMillis = initialCheckInMillis.longValue,
            initialDisplayMode = DisplayMode.Picker,
            initialDisplayedMonthMillis = initialCheckInMillis.longValue,
            locale = Locale.getDefault()
        )
    }

    // CORREÇÃO: Repetimos a lógica para o Check-out
    val datePickerStateOut = remember(initialCheckOutMillis.longValue) {
        DatePickerState(
            initialSelectedDateMillis = initialCheckOutMillis.longValue,
            initialDisplayMode = DisplayMode.Picker,
            initialDisplayedMonthMillis = initialCheckOutMillis.longValue,
            locale = Locale.getDefault()
        )
    }

    // --- 3. Diálogos de Calendário ---

    if (showCheckInPicker) {
        DatePickerDialog(
            onDismissRequest = { showCheckInPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerStateIn.selectedDateMillis?.let { millis ->
                            dataCheckIn = formatMillisToDateString(millis)
                            initialCheckInMillis.longValue = millis
                        }
                        showCheckInPicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCheckInPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerStateIn) }
    }

    if (showCheckOutPicker) {
        DatePickerDialog(
            onDismissRequest = { showCheckOutPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerStateOut.selectedDateMillis?.let { millis ->
                            dataCheckOut = formatMillisToDateString(millis)
                            initialCheckOutMillis.longValue = millis
                        }
                        showCheckOutPicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showCheckOutPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerStateOut) }
    }


    // --- ESTRUTURA DA UI ---
    Scaffold(
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

            // 4. DATA CHECK-IN (Seletor de Data)
            OutlinedTextField(
                value = dataCheckIn,
                onValueChange = { /* Não permite edição direta */ },
                readOnly = true,
                label = { Text("Data Check-in: ${dataCheckIn}") },
                trailingIcon = { Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(0.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCheckInPicker = true }
            )

            // 5. DATA CHECK-OUT (Seletor de Data)
            OutlinedTextField(
                value = dataCheckOut,
                onValueChange = { /* Não permite edição direta */ },
                readOnly = true,
                label = { Text("Data Check-out: ${dataCheckOut}") },
                trailingIcon = { Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(0.dp)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCheckOutPicker = true }
            )

            // 6. STATUS (Menu Suspenso)
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    // Usando a função toTitleCase()
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
                            // Usando a função toTitleCase()
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

            // =========================
            // BOTÃO SALVAR
            // =========================
            Button(
                onClick = {
                    val inMillis = parseDateStringToMillis(dataCheckIn)
                    val outMillis = parseDateStringToMillis(dataCheckOut)

                    if (nomeCliente.isBlank() || quartoId.isBlank() || hospedeId.isBlank() || inMillis == 0L || outMillis == 0L) {
                        return@Button
                    }

                    viewModel.salvarReserva(
                        id = reservaId,
                        quartoId = quartoId,
                        hospedeId = hospedeId,
                        nomeCliente = nomeCliente,
                        dataCheckIn = inMillis,
                        dataCheckOut = outMillis,
                        status = status.lowercase(Locale.getDefault())
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