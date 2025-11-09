package com.example.reservashotel.data.dao

import androidx.room.*
import com.example.reservashotel.data.model.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes")
    fun listarClientes(): Flow<List<Cliente>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(cliente: Cliente)

    @Update
    suspend fun atualizar(cliente: Cliente)

    @Delete
    suspend fun deletar(cliente: Cliente)
}
