package com.example.reservashotel.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hospedes")
data class Hospede(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String
)
