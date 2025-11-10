package com.example.reservashotel.data.dao

import androidx.room.*
import com.example.reservashotel.data.model.Hospede
import kotlinx.coroutines.flow.Flow

@Dao
interface HospedeDao {

    @Query("SELECT * FROM hospedes")
    fun getAll(): Flow<List<Hospede>>


    @Query("SELECT * FROM hospedes WHERE id = :hospedeId")
    suspend fun getHospedeById(hospedeId: Int): Hospede?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hospede: Hospede)

    @Update // ⬅️ Adicione isto!
    suspend fun update(hospede: Hospede)

    @Delete
    suspend fun delete(hospede: Hospede)
}