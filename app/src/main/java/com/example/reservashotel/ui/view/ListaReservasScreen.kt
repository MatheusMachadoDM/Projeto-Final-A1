package com.example.reservashotel.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

data class Reserva(
    val id: String = "",
    val nome: String = "",
    val quarto: String = "",
    val dataEntrada: String = "",
    val dataSaida: String = ""
)

@Composable
fun ListaReservasScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var reservas by remember { mutableStateOf(listOf<Reserva>()) }

    LaunchedEffect(true) {
        db.collection("reservas").addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                reservas = snapshot.documents.map { doc ->
                    Reserva(
                        id = doc.id,
                        nome = doc.getString("nome") ?: "",
                        quarto = doc.getString("quarto") ?: "",
                        dataEntrada = doc.getString("dataEntrada") ?: "",
                        dataSaida = doc.getString("dataSaida") ?: ""
                    )
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reservas Cadastradas", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("formularioReserva") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nova Reserva")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(reservas) { reserva ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("🧑 ${reserva.nome}")
                        Text("🏨 Quarto: ${reserva.quarto}")
                        Text("📅 Entrada: ${reserva.dataEntrada}")
                        Text("📅 Saída: ${reserva.dataSaida}")
                    }
                }
            }
        }
    }
}
