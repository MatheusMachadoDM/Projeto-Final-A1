package com.example.reservashotel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reservashotel.data.dao.HospedeDao
import com.example.reservashotel.data.dao.QuartoDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.model.Quarto
import com.example.reservashotel.data.model.Reserva
import com.example.reservashotel.data.model.Hospede

@Database(
    entities = [Quarto::class, Reserva::class, Hospede::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quartoDao(): QuartoDao
    abstract fun reservaDao(): ReservaDao
    abstract fun hospedeDao(): HospedeDao

    companion object {
        const val DATABASE_NAME = "reservas_hotel_db"
    }
}
