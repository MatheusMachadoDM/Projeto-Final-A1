package com.example.reservashotel.data.dao

import androidx.room.*
import com.example.reservashotel.data.model.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Query("SELECT * FROM reservas")
    fun listarReservas(): Flow<List<Reserva>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(reserva: Reserva)

    @Update
    suspend fun atualizar(reserva: Reserva)

    @Delete
    suspend fun deletar(reserva: Reserva)
}
