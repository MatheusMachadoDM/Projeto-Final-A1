package com.example.reservashotel.data.dao

import androidx.room.*
import com.example.reservashotel.data.model.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Query("SELECT * FROM reserva ORDER BY dataCheckIn ASC")
    fun getAllReservas(): Flow<List<Reserva>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReserva(reserva: Reserva)

    @Update
    suspend fun updateReserva(reserva: Reserva)

    @Delete
    suspend fun deleteReserva(reserva: Reserva)

    @Query("SELECT * FROM reserva WHERE id = :id LIMIT 1")
    suspend fun getReservaById(id: String): Reserva?
}
