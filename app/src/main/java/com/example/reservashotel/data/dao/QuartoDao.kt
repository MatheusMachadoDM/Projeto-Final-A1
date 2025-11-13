package com.example.reservashotel.data.dao

import androidx.room.*
import com.example.reservashotel.data.model.Quarto
import kotlinx.coroutines.flow.Flow // Importa a classe Flow para programação reativa

/**
 * Interface Data Access Object (DAO) para a entidade Quarto.
 * O Room gera automaticamente o código necessário para essas funções
 * em tempo de compilação.
 */
@Dao
interface QuartoDao {

    /**
     * Retorna todos os quartos do banco de dados.
     * O uso de Flow<List<Quarto>> permite que a UI observe as mudanças
     * em tempo real (reatividade).
     */
    @Query("SELECT * FROM quartos")
    fun getAll(): Flow<List<Quarto>>

    /**
     * Insere um novo Quarto no banco de dados.
     * Se houver um conflito (um quarto com o mesmo ID já existe), ele é substituído.
     * Esta função é suspensa (suspend) pois deve ser chamada de uma coroutine.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quarto: Quarto)

    /**
     * Atualiza um Quarto existente.
     * Esta função é suspensa (suspend) pois deve ser chamada de uma coroutine.
     */
    @Update
    suspend fun update(quarto: Quarto)

    /**
     * Exclui um Quarto do banco de dados.
     * Esta função é suspensa (suspend) pois deve ser chamada de uma coroutine.
     */
    @Delete
    suspend fun delete(quarto: Quarto)

    /**
     * Busca um Quarto pelo seu ID.
     * @param quartoId O ID do quarto a ser buscado.
     * @return O objeto Quarto ou null se não for encontrado.
     * Esta função é suspensa (suspend) pois é uma operação de leitura síncrona.
     */
    @Query("SELECT * FROM quartos WHERE id = :quartoId")
    suspend fun getQuartoById(quartoId: String): Quarto?
}