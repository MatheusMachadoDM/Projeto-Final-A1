package com.example.reservashotel.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FormularioReservaScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()

    var nome by remember { mutableStateOf(TextFieldValue("")) }
    var quarto by remember { mutableStateOf(TextFieldValue("")) }
    var dataEntrada by remember { mutableStateOf(TextFieldValue("")) }
    var dataSaida by remember { mutableStateOf(TextFieldValue("")) }
    var mensagem by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nova Reserva", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quarto,
            onValueChange = { quarto = it },
            label = { Text("Número do Quarto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dataEntrada,
            onValueChange = { dataEntrada = it },
            label = { Text("Data de Entrada") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dataSaida,
            onValueChange = { dataSaida = it },
            label = { Text("Data de Saída") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val reserva = hashMapOf(
                    "nome" to nome.text,
                    "quarto" to quarto.text,
                    "dataEntrada" to dataEntrada.text,
                    "dataSaida" to dataSaida.text
                )
                db.collection("reservas")
                    .add(reserva)
                    .addOnSuccessListener {
                        mensagem = "✅ Reserva salva!"
                        nome = TextFieldValue("")
                        quarto = TextFieldValue("")
                        dataEntrada = TextFieldValue("")
                        dataSaida = TextFieldValue("")
                    }
                    .addOnFailureListener {
                        mensagem = "❌ Erro ao salvar reserva!"
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Reserva")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("listaReservas") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Ver Reservas")
        }

        Spacer(Modifier.height(12.dp))
        Text(mensagem)
    }
}
