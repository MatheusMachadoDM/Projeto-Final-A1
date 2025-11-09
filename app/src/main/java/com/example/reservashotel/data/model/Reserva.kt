package com.example.reservashotel.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class Reserva(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clienteId: Int,
    val quartoNumero: Int,
    val dataCheckIn: String,
    val dataCheckOut: String,
    val status: String
)
