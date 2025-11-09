package com.example.reservashotel.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.reservashotel.data.dao.ClienteDao
import com.example.reservashotel.data.dao.ReservaDao
import com.example.reservashotel.data.model.Cliente
import com.example.reservashotel.data.model.Reserva

@Database(entities = [Cliente::class, Reserva::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clienteDao(): ClienteDao
    abstract fun reservaDao(): ReservaDao
}
