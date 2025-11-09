package com.example.reservashotel.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reserva")
data class Reserva(
    @PrimaryKey
    val id: String = "",       // String para casar com Firestore docId / UUID
    val quartoId: String,      // id do quarto (String)
    val hospedeId: String,     // id do hóspede (String) — pode ficar vazio se não usar
    val nomeCliente: String,
    val dataCheckIn: Long,
    val dataCheckOut: Long,
    val status: String
)
