package com.example.reservashotel.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quartos")
data class Quarto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val numero: Int,
    val tipo: String,
    val valorDiaria: Double,
    val status: String
)
