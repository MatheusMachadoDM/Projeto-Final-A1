package com.example.reservashotel.data.dao

import androidx.room.*
import com.example.reservashotel.data.model.Quarto
import kotlinx.coroutines.flow.Flow

@Dao
interface QuartoDao {

    // CORRIGIDO: Retorna Flow<List<Quarto>> para reatividade
    @Query("SELECT * FROM quartos")
    fun getAll(): Flow<List<Quarto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quarto: Quarto)

    @Update
    suspend fun update(quarto: Quarto)

    @Delete
    suspend fun delete(quarto: Quarto)
}