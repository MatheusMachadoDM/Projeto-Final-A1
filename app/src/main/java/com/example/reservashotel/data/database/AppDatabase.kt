package com.example.reservashotel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reservashotel.data.dao.HospedeDao
import com.example.reservashotel.data.dao.QuartoDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.dao.UsuarioDao
import com.example.reservashotel.data.model.Quarto
import com.example.reservashotel.data.model.Reserva
import com.example.reservashotel.data.model.Hospede
import com.example.reservashotel.data.model.Usuario

@Database(
    entities = [Quarto::class, Reserva::class, Hospede::class, Usuario::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun quartoDao(): QuartoDao
    abstract fun reservaDao(): ReservaDao
    abstract fun hospedeDao(): HospedeDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        const val DATABASE_NAME = "reservas_hotel_db"
    }
}
