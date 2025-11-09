package com.example.reservashotel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reservashotel.data.dao.QuartoDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.model.Quarto
import com.example.reservashotel.data.model.Reserva

@Database(
    entities = [Quarto::class, Reserva::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quartoDao(): QuartoDao
    abstract fun reservaDao(): ReservaDao

    companion object {
        const val DATABASE_NAME = "reservas_hotel_db"
    }
}
