package com.example.reservashotel.ui.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reservashotel.ui.viewmodel.ReservaViewModel
import com.example.reservashotel.data.model.Reserva
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaReservasScreen(
    navController: NavController,
    viewModel: ReservaViewModel
) {
    val todasReservas by viewModel.listaReservas.collectAsState()

    var filtroStatus by remember { mutableStateOf("ativas") }
    var expandedFilter by remember { mutableStateOf(false) }
    val opcoesFiltro = mapOf(
        "ativas" to "Reservas Ativas",
        "outras" to "Concluídas/Canceladas"
    )

    val reservasFiltradas = remember(todasReservas, filtroStatus) {
        todasReservas.filter { reserva ->
            when (filtroStatus) {
                "ativas" -> reserva.status.lowercase(Locale.getDefault()) == "ativa"
                "outras" -> reserva.status.lowercase(Locale.getDefault()) != "ativa"
                else -> true
            }
        }
    }

    // Estados de exclusão (mantidos)
    var reservaParaExcluir by remember { mutableStateOf<Reserva?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        // 1. TOP BAR COM BOTÃO DE VOLTAR E FILTRO
        topBar = {
            TopAppBar(
                title = { Text("${opcoesFiltro[filtroStatus]}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar para a página principal"
                        )
                    }
                },
                actions = {
                    ExposedDropdownMenuBox(
                        expanded = expandedFilter,
                        onExpandedChange = { expandedFilter = !expandedFilter },
                    ) {
                        Text(
                            text = opcoesFiltro[filtroStatus] ?: "Filtro",
                            modifier = Modifier
                                .menuAnchor()
                                .padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFilter,
                            onDismissRequest = { expandedFilter = false }
                        ) {
                            opcoesFiltro.forEach { (key, value) ->
                                DropdownMenuItem(
                                    text = { Text(value) },
                                    onClick = {
                                        filtroStatus = key
                                        expandedFilter = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        // 2. FLOATING ACTION BUTTON (FAB) na parte inferior
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("form_reserva") }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Nova Reserva")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (reservasFiltradas.isEmpty()) { // Usa a lista filtrada
                Text(
                    text = if (todasReservas.isEmpty()) "Nenhuma reserva cadastrada." else "Nenhuma reserva encontrada com o filtro atual.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(reservasFiltradas) { reserva -> // Usa a lista filtrada
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Cliente: ${reserva.nomeCliente}", style = MaterialTheme.typography.titleMedium)
                                Text("Quarto ID: ${reserva.quartoId}")
                                Text("Check-in: ${formatMillisToDate(reserva.dataCheckIn)}")
                                Text("Check-out: ${formatMillisToDate(reserva.dataCheckOut)}")
                                Text("Status: ${reserva.status}")
                                Text("Id: ${reserva.id}")

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { navController.navigate("form_reserva?id=${reserva.id}") },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = {
                                            reservaParaExcluir = reserva
                                            showDeleteDialog = true
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
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

    // --- Diálogo de Confirmação (Inalterado) ---
    if (showDeleteDialog && reservaParaExcluir != null) {
        val reserva = reservaParaExcluir!!
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                reservaParaExcluir = null
            },
            title = {
                Text(text = "Confirmar Exclusão de Reserva")
            },
            text = {
                Text(text = "Tem certeza que deseja excluir a reserva do cliente ${reserva.nomeCliente} (Check-in: ${formatMillisToDate(reserva.dataCheckIn)})?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.excluirReserva(reserva)
                        showDeleteDialog = false
                        reservaParaExcluir = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        reservaParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
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